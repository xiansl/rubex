package com.googlecode.rubex.marketdata;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.googlecode.rubex.orderbook.OrderBook;
import com.googlecode.rubex.orderbook.OrderBookEntrySide;
import com.googlecode.rubex.orderbook.event.OrderBookListener;
import com.googlecode.rubex.orderbook.event.OrderBookQuoteEvent;
import com.googlecode.rubex.orderbook.event.OrderBookTradeEvent;
import com.googlecode.rubex.utils.LongUtils;

/**
 * Simple implementation of {@link MarketDataTracker} interface that listens to 
 * order book events.
 * 
 * @see OrderBook
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleOrderBookMarketDataTracker 
    implements MarketDataTracker, OrderBookListener
{
    private long lastTradePrice = 0;
    private long lastTradeQuantity = 0;
    private long lastTradeTimestamp = 0;
    private long totalVolume = 0;
    private long totalValue = 0;
    
    private NavigableMap<Long, Long> bids = new TreeMap<Long, Long> ();
    private NavigableMap<Long, Long> asks = new TreeMap<Long, Long> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTrade (OrderBookTradeEvent event)
    {
        lastTradePrice = event.getPrice ();
        lastTradeQuantity = event.getQuantity ();
        lastTradeTimestamp = event.getTimestamp ();
        
        totalVolume = LongUtils.safeAdd (totalVolume, event.getQuantity ());
        totalValue = LongUtils.safeAdd (
            totalValue, LongUtils.safeMultiply (event.getQuantity (), event.getPrice ()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onQuote (OrderBookQuoteEvent event)
    {
        OrderBookEntrySide side = event.getSide ();
        long price = event.getPrice ();
        Long priceObject = Long.valueOf (price);
        
        long quantity;
        
        switch (side)
        {
        case BID:
            quantity = LongUtils.safeAdd (
                bids.containsKey (priceObject) ? 
                    bids.get (priceObject).longValue () : 0, 
                event.getQuantityDelta ()); 
            if (quantity > 0)
                bids.put (priceObject, Long.valueOf (quantity));
            else bids.remove (priceObject);
            break;
        case ASK:
            quantity = LongUtils.safeAdd (
                asks.containsKey (priceObject) ? 
                    asks.get (priceObject).longValue () : 0, 
                event.getQuantityDelta ());
            if (quantity > 0)
                asks.put (priceObject, Long.valueOf (quantity));
            else asks.remove (priceObject);
            break;
        default:
            throw new Error ("Unknown order book entry side: " + side);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastTradePrice ()
    {
        return lastTradePrice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastTradeTimestamp ()
    {
        return lastTradeTimestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastTradeQuantity ()
    {
        return lastTradeQuantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTotalVolume ()
    {
        return totalVolume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTotalValue ()
    {
        return totalValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBestBidPrice ()
    {
        return bids.isEmpty () ? 0 : bids.lastKey ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBestBidQuantity ()
    {
        return bids.isEmpty () ? 0 : bids.lastEntry ().getValue ().longValue ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBestAskPrice ()
    {
        return asks.isEmpty () ? 0 : asks.firstKey ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBestAskQuantity ()
    {
        return asks.isEmpty () ? 
            0 : asks.firstEntry ().getValue ().longValue ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quote[] getBidQuotes (int maximumCount)
    {
        int count = Math.min (bids.size (), maximumCount);
        
        Quote [] result = new Quote [count];
        
        Iterator<Map.Entry<Long, Long>> iterator = 
            bids.descendingMap ().entrySet ().iterator ();
        for (int i = 0; i < count; i++)
        {
            Map.Entry<Long, Long> entry = iterator.next ();
            
            result [i] = new MyQuote (
                entry.getValue ().longValue (), entry.getKey ().longValue ());
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quote[] getAskQuotes (int maximumCount)
    {
        int count = Math.min (asks.size (), maximumCount);
        
        Quote [] result = new Quote [count];
        
        Iterator<Map.Entry<Long, Long>> iterator = asks.entrySet ().iterator ();
        for (int i = 0; i < count; i++)
        {
            Map.Entry<Long, Long> entry = iterator.next ();
            
            result [i] = new MyQuote (
                entry.getValue ().longValue (), entry.getKey ().longValue ());
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBidQuantityAbove (long price)
    {
        long result = 0;
        
        for (Map.Entry <Long, Long> entry: bids.descendingMap ().entrySet ())
        {
            if (entry.getKey ().longValue () >= price)
                result = LongUtils.safeAdd (result, entry.getValue ().longValue ());
            else break;
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAskQuantityBelow (long price)
    {
        long result = 0;
        
        for (Map.Entry <Long, Long> entry: asks.entrySet ())
        {
            if (entry.getKey ().longValue () <= price)
                result = LongUtils.safeAdd (result, entry.getValue ().longValue ());
            else break;
        }
        
        return result;
    }
    
    private static class MyQuote implements Quote
    {
        private final long quantity;
        private final long price;

        public MyQuote (long quantity, long price)
        {
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            if (price <= 0)
                throw new IllegalArgumentException ("Price <= 0");
            
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public long getQuantity ()
        {
            return quantity;
        }

        @Override
        public long getPrice ()
        {
            return price;
        }
    }
}
