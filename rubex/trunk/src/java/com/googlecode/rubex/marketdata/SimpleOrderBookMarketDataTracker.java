package com.googlecode.rubex.marketdata;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.googlecode.rubex.orderbook.OrderBookEntrySide;
import com.googlecode.rubex.orderbook.event.OrderBookListener;
import com.googlecode.rubex.orderbook.event.OrderBookQuoteEvent;
import com.googlecode.rubex.orderbook.event.OrderBookTradeEvent;

public class SimpleOrderBookMarketDataTracker implements MarketDataTracker, OrderBookListener
{
    private long lastTradePrice = 0;
    private long lastTradeQuantity = 0;
    private long lastTradeTimestamp = 0;
    
    private NavigableMap<Long, Long> bids = new TreeMap<Long, Long> ();
    private NavigableMap<Long, Long> asks = new TreeMap<Long, Long> ();

    @Override
    public void onTrade (OrderBookTradeEvent event)
    {
        lastTradePrice = event.getPrice ();
        lastTradeQuantity = event.getQuantity ();
        lastTradeTimestamp = event.getTimestamp ();
    }

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
            quantity = safeAdd (bids.containsKey (priceObject) ? bids.get (priceObject).longValue () : 0, event.getQuantityDelta ()); 
            if (quantity > 0)
                bids.put (priceObject, Long.valueOf (quantity));
            else bids.remove (priceObject);
            break;
        case ASK:
            quantity = safeAdd (asks.containsKey (priceObject) ? asks.get (priceObject).longValue () : 0, event.getQuantityDelta ());
            if (quantity > 0)
                asks.put (priceObject, Long.valueOf (quantity));
            else asks.remove (priceObject);
            break;
        default:
            throw new Error ("Unknown order book entry side: " + side);
        }
    }

    @Override
    public long getLastTradePrice ()
    {
        return lastTradePrice;
    }

    @Override
    public long getLastTradeTimestamp ()
    {
        return lastTradeTimestamp;
    }

    @Override
    public long getLastTradeQuantity ()
    {
        return lastTradeQuantity;
    }

    @Override
    public long getBestBidPrice ()
    {
        return bids.isEmpty () ? 0 : bids.lastKey ();
    }

    @Override
    public long getBestBidQuantity ()
    {
        return bids.isEmpty () ? 0 : bids.lastEntry ().getValue ().longValue ();
    }

    @Override
    public long getBestAskPrice ()
    {
        return asks.isEmpty () ? 0 : asks.firstKey ();
    }

    @Override
    public long getBestAskQuantity ()
    {
        return asks.isEmpty () ? 0 : asks.firstEntry ().getValue ().longValue ();
    }

    @Override
    public Quote[] getBidQuotes (int maximumCount)
    {
        int count = Math.min (bids.size (), maximumCount);
        
        Quote [] result = new Quote [count];
        
        Iterator<Map.Entry<Long, Long>> iterator = bids.descendingMap ().entrySet ().iterator ();
        for (int i = 0; i < count; i++)
        {
            Map.Entry<Long, Long> entry = iterator.next ();
            
            result [i] = new MyQuote (entry.getValue ().longValue (), entry.getKey ().longValue ());
        }
        
        return result;
    }

    @Override
    public Quote[] getAskQuotes (int maximumCount)
    {
        int count = Math.min (asks.size (), maximumCount);
        
        Quote [] result = new Quote [count];
        
        Iterator<Map.Entry<Long, Long>> iterator = asks.entrySet ().iterator ();
        for (int i = 0; i < count; i++)
        {
            Map.Entry<Long, Long> entry = iterator.next ();
            
            result [i] = new MyQuote (entry.getValue ().longValue (), entry.getKey ().longValue ());
        }
        
        return result;
    }

    @Override
    public long getBidQuantityAbove (int price)
    {
        long result = 0;
        
        for (Map.Entry <Long, Long> entry: bids.descendingMap ().entrySet ())
        {
            if (entry.getKey ().longValue () >= price)
                result = safeAdd (result, entry.getValue ().longValue ());
            else break;
        }
        
        return result;
    }

    @Override
    public long getAskQuantityBelow (int price)
    {
        long result = 0;
        
        for (Map.Entry <Long, Long> entry: asks.entrySet ())
        {
            if (entry.getKey ().longValue () <= price)
                result = safeAdd (result, entry.getValue ().longValue ());
            else break;
        }
        
        return result;
    }
    
    private static long safeAdd (long x, long y)
    {
        if (x > 0 && y > 0 && (x > Long.MAX_VALUE - y))
            throw new RuntimeException ("Long number overflow");
        
        if (x < 0 && y < 0 && (x < Long.MIN_VALUE - y))
            throw new RuntimeException ("Long number overflow");
        
        return x + y;
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
