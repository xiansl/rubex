package com.googlecode.rubex;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SimpleOrderBook implements OrderBook
{
    private final SortedSet<OrderBookEntry> bids = new TreeSet<OrderBookEntry> (new BidComparator ());
    private final SortedSet<OrderBookEntry> asks = new TreeSet<OrderBookEntry> (new AskComparator ());
    
    private long sequentialNumber = 0L;
    
    @Override
    public OrderBookEntryHandler placeEntry (long timestamp,
        OrderBookEntryType type, long quantity, long limitPrice,
        OrderBookEntryCallback callback, Object closure)
        throws OrderBookException
    {
        if (type == null)
            throw new IllegalArgumentException ("Type is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (limitPrice < 0)
            throw new IllegalArgumentException ("Limit price < 0");
        
        if (callback == null)
            throw new IllegalArgumentException ("Callback is null");
        
        OrderBookEntry newEntry = new OrderBookEntry (sequentialNumber++, type, limitPrice, quantity, callback, closure);
        
        Iterator <OrderBookEntry> i;
        
        switch (type)
        {
        case BID:
            i = asks.iterator ();
            break;
        case ASK:
            i = bids.iterator ();
            break;
        default:
            throw new Error ("Unknown order book entry type: " + type);
        }
        
        while (i.hasNext () && newEntry.active)
        {
            OrderBookEntry entry = i.next ();
            
            boolean fits;
            
            if (limitPrice > 0)
            {
                switch (type)
                {
                case BID:
                    fits = limitPrice >= entry.limitPrice; 
                    break;
                case ASK:
                    fits = limitPrice <= entry.limitPrice; 
                    break;
                default:
                    throw new Error ("Unknown order book entry type: " + type);
                }
            }
            else fits = true;
            
            if (fits)
            {
                long tradeQuantity = Math.min (newEntry.unfilledQuantity, entry.unfilledQuantity);
                
                fillEntry (timestamp, newEntry, tradeQuantity, entry.limitPrice);
                fillEntry (timestamp, entry, tradeQuantity, entry.limitPrice);
            }
            else break;
        }
        
        if (newEntry.active)
        {
            if (newEntry.limitPrice > 0)
            {
                switch (type)
                {
                case BID:
                    bids.add (newEntry); 
                    break;
                case ASK:
                    asks.add (newEntry); 
                    break;
                default:
                    throw new Error ("Unknown order book entry type: " + type);
                }
            }
            else 
                cancelEntry (timestamp, newEntry);
        }

        switch (type)
        {
        case BID:
            cleanup (asks); 
            break;
        case ASK:
            cleanup (bids); 
            break;
        default:
            throw new Error ("Unknown order book entry type: " + type);
        }
        
        return newEntry;
    }
    
    private void fillEntry (long timestamp, OrderBookEntry entry, long quantity, long price)
    {
        if (entry == null)
            throw new IllegalArgumentException ("Entry is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
            
        if (entry.getOrderBook () != this)
            throw new IllegalArgumentException ("This order book entry is not mine");

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
    {
        if (entry == null)
            throw new IllegalArgumentException ("Entry is null");
        
        if (entry.getOrderBook () != this)
            throw new IllegalArgumentException ("This order book entry is not mine");

        if (!entry.active)
            throw new IllegalStateException ("Entry is not active");
        
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
        public final OrderBookEntryType type;
        public final long sequentialNumber;
        public final long limitPrice;
        public long unfilledQuantity;
        public final OrderBookEntryCallback callback;
        public final Object closure;
        public boolean active = true;
        
        public OrderBookEntry (long sequentialNumber, OrderBookEntryType type, long limitPrice,
                long unfilledQuantity, OrderBookEntryCallback callback, Object closure)
        {
            if (type == null)
                throw new IllegalArgumentException ("Type is null");
            
            if (limitPrice < 0)
                throw new IllegalArgumentException ("Price < 0");
            
            if (unfilledQuantity <= 0)
                throw new IllegalArgumentException ("Unfilled quantity <= 0");
            
            if (callback == null)
                throw new IllegalArgumentException ("Callback is null");
            
            this.sequentialNumber = sequentialNumber;
            this.type = type;
            this.limitPrice = limitPrice;
            this.unfilledQuantity = unfilledQuantity;
            this.closure = closure;
            this.callback = callback;
        }

        @Override
        public OrderBookEntryType getEntryType ()
        {
            return type;
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
        public void cancel (long timestamp)
        {
            cancelEntry (timestamp, this);
            
            switch (type)
            {
            case BID:
                cleanup (bids);
                break;
            case ASK:
                cleanup (asks);
                break;
            default:
                throw new Error ("Unknown order book entry type: " + type);
            }
        }
        
        public SimpleOrderBook getOrderBook ()
        {
            return SimpleOrderBook.this;
        }
    }
    
    private static class BidComparator implements Comparator<OrderBookEntry>
    {
        @Override
        public int compare (OrderBookEntry leftEntry, OrderBookEntry rightEntry)
        {
            if (leftEntry == null)
                throw new IllegalArgumentException ("Left entry is null");
            
            if (rightEntry == null)
                throw new IllegalArgumentException ("Right entry is null");
            
            if (leftEntry == rightEntry) return 0;
            
            long leftPrice = leftEntry.limitPrice;
            long rightPrice = rightEntry.limitPrice;
            
            if (leftPrice > rightPrice) return -1;
            else if (leftPrice < rightPrice) return 1;
            else
            {
                long leftSequentialNumber = leftEntry.sequentialNumber;
                long rightSequentialNumber = rightEntry.sequentialNumber;
                
                if (leftSequentialNumber < rightSequentialNumber) return -1;
                else if (leftSequentialNumber > rightSequentialNumber) return 1;
                else return 0;
            }
        }
    }
    
    private static class AskComparator implements Comparator<OrderBookEntry>
    {
        @Override
        public int compare (OrderBookEntry leftEntry, OrderBookEntry rightEntry)
        {
            if (leftEntry == null)
                throw new IllegalArgumentException ("Left entry is null");
            
            if (rightEntry == null)
                throw new IllegalArgumentException ("Right entry is null");
            
            if (leftEntry == rightEntry) return 0;
            
            long leftPrice = leftEntry.limitPrice;
            long rightPrice = rightEntry.limitPrice;
            
            if (leftPrice < rightPrice) return -1;
            else if (leftPrice > rightPrice) return 1;
            else
            {
                long leftSequentialNumber = leftEntry.sequentialNumber;
                long rightSequentialNumber = rightEntry.sequentialNumber;
                
                if (leftSequentialNumber < rightSequentialNumber) return -1;
                else if (leftSequentialNumber > rightSequentialNumber) return 1;
                else return 0;
            }
        }
    }
}
