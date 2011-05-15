package com.googlecode.rubex.orderbook;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Simple implementation of {@link OrderBook} interface based on 
 * {@link TreeSet}.
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleOrderBook extends AbstractOrderBook
{
    private final SortedSet<OrderBookEntry> bids = 
        new TreeSet<OrderBookEntry> (
            new EntryComparator (OrderBookEntrySide.BID));
    
    private final SortedSet<OrderBookEntry> asks = 
        new TreeSet<OrderBookEntry> (
            new EntryComparator (OrderBookEntrySide.ASK));
    
    private long sequentialNumber = 0L;
    
    /**
     * Create new empty order book.
     */
    public SimpleOrderBook ()
    {
        // Do nothing
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public OrderBookEntryHandler placeEntry (long timestamp,
        OrderBookEntrySide side, long quantity, long limitPrice,
        OrderBookEntryCallback callback, Object closure)
        throws OrderBookException
    {
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (limitPrice < 0)
            throw new IllegalArgumentException ("Limit price < 0");
        
        if (callback == null)
            throw new IllegalArgumentException ("Callback is null");
        
        OrderBookEntry newEntry = 
            new OrderBookEntry (
                sequentialNumber++, 
                side, limitPrice, quantity, 
                callback, closure);
        
        Iterator <OrderBookEntry> i;
        
        switch (side)
        {
        case BID:
            i = asks.iterator ();
            break;
        case ASK:
            i = bids.iterator ();
            break;
        default:
            throw new Error ("Unknown order book entry side: " + side);
        }
        
        while (i.hasNext () && newEntry.active)
        {
            OrderBookEntry entry = i.next ();
            long entryLimitPrice = entry.limitPrice; 
            
            boolean fits;
            
            if (limitPrice > 0)
            {
                switch (side)
                {
                case BID:
                    fits = limitPrice >= entryLimitPrice; 
                    break;
                case ASK:
                    fits = limitPrice <= entryLimitPrice; 
                    break;
                default:
                    throw new Error (
                        "Unknown order book entry side: " + side);
                }
            }
            else fits = true;
            
            if (fits)
            {
                long tradeQuantity = 
                    Math.min (
                        newEntry.unfilledQuantity, entry.unfilledQuantity);
                
                fillEntry (
                    timestamp, newEntry, tradeQuantity, entryLimitPrice);
                fillEntry (
                    timestamp, entry, tradeQuantity, entryLimitPrice);
                
                fireOnTrade (timestamp, tradeQuantity, entryLimitPrice);
                fireOnQuote (timestamp, entry.side, entryLimitPrice, -tradeQuantity);
            }
            else break;
        }
        
        if (newEntry.active)
        {
            if (limitPrice > 0)
            {
                switch (side)
                {
                case BID:
                    bids.add (newEntry); 
                    fireOnQuote (timestamp, OrderBookEntrySide.BID, limitPrice, newEntry.unfilledQuantity);
                    break;
                case ASK:
                    asks.add (newEntry); 
                    fireOnQuote (timestamp, OrderBookEntrySide.ASK, limitPrice, newEntry.unfilledQuantity);
                    break;
                default:
                    throw new Error ("Unknown order book entry side: " + side);
                }
            }
            else 
                cancelEntry (timestamp, newEntry);
        }

        switch (side)
        {
        case BID:
            cleanup (asks); 
            break;
        case ASK:
            cleanup (bids); 
            break;
        default:
            throw new Error ("Unknown order book entry side: " + side);
        }
        
        return newEntry;
    }
    
    private void fillEntry (
        long timestamp, OrderBookEntry entry, long quantity, long price)
    {
        if (entry == null)
            throw new IllegalArgumentException ("Entry is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
            
        if (entry.getOrderBook () != this)
            throw new IllegalArgumentException (
                "This order book entry is not mine");

        if (!entry.active)
            throw new IllegalStateException ("Entry is not active");

        if (quantity > entry.unfilledQuantity)
            throw new IllegalStateException ("Not enough unfilled quantity");
        
        entry.unfilledQuantity -= quantity;
        
        entry.callback.onFill (timestamp, entry, quantity, price);
        
        if (entry.unfilledQuantity == 0)
        {
            entry.active = false;
            entry.callback.onFilled (timestamp, entry);
        }
    }
    
    private void cancelEntry (long timestamp, OrderBookEntry entry) 
        throws OrderBookException
    {
        if (entry == null)
            throw new IllegalArgumentException ("Entry is null");
        
        if (entry.getOrderBook () != this)
            throw new IllegalArgumentException (
                "This order book entry is not mine");

        if (!entry.active)
            throw new OrderBookException ("Entry is not active");
        
        entry.active = false;
        entry.callback.onCanceled (timestamp, entry);
    }
    
    private static void cleanup (SortedSet<OrderBookEntry> set)
    {
        if (set == null)
            throw new IllegalArgumentException ("Set is null");
        
        Iterator<OrderBookEntry> i = set.iterator ();
        while (i.hasNext ())
        {
            OrderBookEntry entry = i.next ();
            
            if (!entry.active)
                i.remove ();
            else break;
        }
    }
    
    private class OrderBookEntry implements OrderBookEntryHandler
    {
        public final OrderBookEntrySide side;
        public final long sequentialNumber;
        public final long limitPrice;
        public long unfilledQuantity;
        public final OrderBookEntryCallback callback;
        public final Object closure;
        public boolean active = true;
        
        public OrderBookEntry (
            long sequentialNumber, 
            OrderBookEntrySide side, long limitPrice, long unfilledQuantity, 
            OrderBookEntryCallback callback, Object closure)
        {
            if (side == null)
                throw new IllegalArgumentException ("Side is null");
            
            if (limitPrice < 0)
                throw new IllegalArgumentException ("Price < 0");
            
            if (unfilledQuantity <= 0)
                throw new IllegalArgumentException ("Unfilled quantity <= 0");
            
            if (callback == null)
                throw new IllegalArgumentException ("Callback is null");
            
            this.sequentialNumber = sequentialNumber;
            this.side = side;
            this.limitPrice = limitPrice;
            this.unfilledQuantity = unfilledQuantity;
            this.closure = closure;
            this.callback = callback;
        }

        @Override
        public OrderBookEntrySide getEntrySide ()
        {
            return side;
        }

        @Override
        public long getUnfilledQuantity ()
        {
            return unfilledQuantity;
        }

        @Override
        public long getLimitPrice ()
        {
            return limitPrice;
        }

        @Override
        public Object getClosure ()
        {
            return closure;
        }

        @Override
        public void cancel (long timestamp) throws OrderBookException
        {
            cancelEntry (timestamp, this);
            
            switch (side)
            {
            case BID:
                cleanup (bids);
                fireOnQuote (timestamp, OrderBookEntrySide.BID, limitPrice, -unfilledQuantity);
                break;
            case ASK:
                cleanup (asks);
                fireOnQuote (timestamp, OrderBookEntrySide.ASK, limitPrice, -unfilledQuantity);
                break;
            default:
                throw new Error ("Unknown order book entry side: " + side);
            }
        }
        
        public SimpleOrderBook getOrderBook ()
        {
            return SimpleOrderBook.this;
        }
    }
    
    private static class EntryComparator implements Comparator<OrderBookEntry>
    {
        private final boolean isBid;
        
        public EntryComparator (OrderBookEntrySide side)
        {
            if (side == null)
                throw new IllegalArgumentException ();
            
            switch (side)
            {
            case BID:
                isBid = true;
                break;
            case ASK:
                isBid = false;
                break;
            default:
                throw new Error ("Unknown order book entry side: " + side);
            }
        }
        
        @Override
        public int compare (
            OrderBookEntry leftEntry, OrderBookEntry rightEntry)
        {
            if (leftEntry == null)
                throw new IllegalArgumentException ("Left entry is null");
            
            if (rightEntry == null)
                throw new IllegalArgumentException ("Right entry is null");
            
            if (leftEntry == rightEntry) return 0;
            
            long leftPrice = leftEntry.limitPrice;
            long rightPrice = rightEntry.limitPrice;
            
            if (leftPrice > rightPrice) return isBid ? -1 : 1;
            else if (leftPrice < rightPrice) return isBid ? 1 : -1;
            else
            {
                long leftSequentialNumber = leftEntry.sequentialNumber;
                long rightSequentialNumber = rightEntry.sequentialNumber;
                
                if (leftSequentialNumber < rightSequentialNumber) 
                    return -1;
                else if (leftSequentialNumber > rightSequentialNumber) 
                    return 1;
                else return 0;
            }
        }
    }
}
