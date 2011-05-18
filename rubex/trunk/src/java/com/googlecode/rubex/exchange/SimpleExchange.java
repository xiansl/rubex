package com.googlecode.rubex.exchange;

import com.googlecode.rubex.marketdata.MarketDataTracker;
import com.googlecode.rubex.marketdata.SimpleOrderBookMarketDataTracker;
import com.googlecode.rubex.orderbook.OrderBook;
import com.googlecode.rubex.orderbook.OrderBookEntryCallback;
import com.googlecode.rubex.orderbook.OrderBookEntryHandler;
import com.googlecode.rubex.orderbook.OrderBookEntrySide;
import com.googlecode.rubex.orderbook.OrderBookException;
import com.googlecode.rubex.orderbook.SimpleOrderBook;

public class SimpleExchange implements Exchange
{
    private final OrderBook orderBook = new SimpleOrderBook ();
    private final MarketDataTracker marketDataTracker;
    
    public SimpleExchange ()
    {
        SimpleOrderBookMarketDataTracker marketDataTracker = new SimpleOrderBookMarketDataTracker ();
        orderBook.addOrderBookListener (marketDataTracker);
        this.marketDataTracker = marketDataTracker;
    }
    
    @Override
    public MarketOrder createMarketOrder (long timestamp, OrderSide side,
        long quantity, OrderCallback callback, Object closure)
        throws OrderException
    {
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (callback == null)
            throw new IllegalArgumentException ("Callback is null");
        
        MyMarketOrder order = new MyMarketOrder (side, quantity, callback, closure);
        order.place (timestamp);
        
        return order;
    }

    @Override
    public LimitOrder createLimitOrder (long timestamp, OrderSide side,
        long quantity, long limitPrice, OrderTimeInForce timeInForce,
        OrderCallback callback, Object closure) throws OrderException
    {
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (callback == null)
            throw new IllegalArgumentException ("Callback is null");

        if (limitPrice <= 0)
            throw new IllegalArgumentException ("Limit price is null");
        
        MyLimitOrder order = new MyLimitOrder (side, quantity, limitPrice, timeInForce, callback, closure);
        order.place (timestamp);
        
        return order;
    }

    @Override
    public StopOrder createStopOrder (long timestamp, OrderSide side,
        long quantity, long stopPrice, OrderCallback callback, Object closure)
        throws OrderException
    {
        throw new OrderException ("Stop orders are not supported yet");
    }

    @Override
    public StopLimitOrder createStopLimitOrder (long timestamp, OrderSide side,
        long quantity, long stopPrice, long limitPrice, OrderCallback callback,
        Object closure) throws OrderException
    {
        throw new OrderException ("Stop limit order are not supported yet");
    }

    @Override
    public IcebergOrder createIcebergOrder (long timestamp, OrderSide side,
        long quantity, long limitPrice, long visibleQuantity,
        OrderCallback callback, Object closure) throws OrderException
    {
        throw new OrderException ("Iceberg orders are not supported yet");
    }
    
    private class MyMarketOrder extends AbstractMarketOrder implements OrderBookEntryCallback
    {
        private OrderBookEntryHandler entryHandler = null;
        private boolean done = false;
        
        public MyMarketOrder (OrderSide side, long orderedQuantity, OrderCallback callback, Object closure)
        {
            super (side, orderedQuantity, callback, closure);
        }

        @Override
        public void onFill (long timestamp, OrderBookEntryHandler handler,
            long quantity, long price)
        {
            fill (quantity, price);
            fireOnFill (timestamp, quantity, price);
        }

        @Override
        public void onFilled (long timestamp, OrderBookEntryHandler handler)
        {
            done = true;
            fireOnFilled (timestamp);
        }

        @Override
        public void onCanceled (long timestamp, OrderBookEntryHandler handler)
        {
            done = true;
            fireOnCanceled (timestamp);
        }
        
        @Override
        public void cancel (long timestamp) throws OrderException
        {
            if (!done)
            {
                try
                {
                    entryHandler.cancel (timestamp);
                }
                catch (OrderBookException ex)
                {
                    throw new OrderException ("Cannot cancel order book entry", ex);
                }
            }
        }
        
        public void place (long timestamp) throws OrderException
        {
            try
            {
                entryHandler = orderBook.placeEntry (timestamp, convertSide (getSide ()), getOrderedQuantity (), 0, this, this);
            }
            catch (OrderBookException ex)
            {
                throw new OrderException ("Cannot place order book entry", ex);
            }
        }
    }
    
    private class MyLimitOrder extends AbstractLimitOrder implements OrderBookEntryCallback
    {
        private final long limitPrice;
        private final OrderTimeInForce timeInForce;
        
        private OrderBookEntryHandler entryHandler = null;
        private boolean done = false;

        public MyLimitOrder (OrderSide side, long orderedQuantity, long limitPrice, OrderTimeInForce timeInForce,
                OrderCallback callback, Object closure)
        {
            super (side, orderedQuantity, callback, closure);
            
            if (limitPrice <= 0)
                throw new IllegalArgumentException ("Limit price <= 0");

            if (timeInForce == null)
                throw new IllegalArgumentException ("Time in force is null");
            
            this.limitPrice = limitPrice;
            this.timeInForce = timeInForce;
        }

        @Override
        public long getLimitPrice ()
        {
            return limitPrice;
        }

        @Override
        public OrderTimeInForce getTimeInForce ()
        {
            return timeInForce;
        }

        @Override
        public MarketOrder replaceWithMarketOrder (long timestamp,
            long newQuantity, OrderCallback callback, Object closure)
            throws OrderException
        {
            throw new OrderException ("Order replace is not supported yet");
        }

        @Override
        public MarketOrder replaceWithLimitOrder (long timestamp,
            long newQuantity, long newLimitPrice,
            OrderTimeInForce newTimeInForce, OrderCallback callback,
            Object closure) throws OrderException
        {
            throw new OrderException ("Order replace is not supported yet");
        }

        @Override
        public IcebergOrder replaceWithIcebergOrder (long timestamp,
            long newQuantity, long newLimitPrice, long newVisibleQuantity,
            OrderCallback callback, Object closure) throws OrderException
        {
            throw new OrderException ("Order replace is not supported yet");
        }

        @Override
        public void onFill (long timestamp, OrderBookEntryHandler handler,
            long quantity, long price)
        {
            fill (quantity, price);
            fireOnFill (timestamp, quantity, price);
        }

        @Override
        public void onFilled (long timestamp, OrderBookEntryHandler handler)
        {
            done = true;
            fireOnFilled (timestamp);
        }

        @Override
        public void onCanceled (long timestamp, OrderBookEntryHandler handler)
        {
            done = true;
            fireOnCanceled (timestamp);
        }
        
        @Override
        public void cancel (long timestamp) throws OrderException
        {
            if (!done)
            {
                try
                {
                    entryHandler.cancel (timestamp);
                }
                catch (OrderBookException ex)
                {
                    throw new OrderException ("Cannot cancel order book entry", ex);
                }
            }
        }
        
        public void place (long timestamp) throws OrderException
        {
            OrderTimeInForce timeInForce = getTimeInForce (); 
            
            switch (timeInForce)
            {
            case DAY:
                placeDay (timestamp);
                break;
            case FOK:
                placeFillOrKill (timestamp);
                break;
            case IOC:
                placeImmediateOrCancel (timestamp);
                break;
            default:
                throw new OrderException ("Unsupported time in force: " + timeInForce);
            }
        }
        
        private void placeDay (long timestamp) throws OrderException
        {
            try
            {
                entryHandler = orderBook.placeEntry (timestamp, convertSide (getSide ()), getOrderedQuantity (), 0, this, this);
            }
            catch (OrderBookException ex)
            {
                throw new OrderException ("Cannot place order book entry", ex);
            }
        }
        
        private void placeFillOrKill (long timestamp) throws OrderException
        {
            OrderSide side = getSide ();
            long orderedQuantity = getOrderedQuantity ();
            long limitPrice = getLimitPrice ();
            
            boolean doPlace;
            switch (side)
            {
            case BUY:
                doPlace = orderedQuantity <= marketDataTracker.getAskQuantityBelow (limitPrice);
                break;
            case SELL:
                doPlace = orderedQuantity <= marketDataTracker.getBidQuantityAbove (limitPrice);
                break;
            default:
                throw new Error ("Unknown order side: " + side);
            }
            
            if (doPlace)
            {
                try
                {
                    entryHandler = orderBook.placeEntry (timestamp, convertSide (side), orderedQuantity, limitPrice, this, this);
                }
                catch (OrderBookException ex)
                {
                    throw new OrderException ("Cannot place order book entry", ex);
                }
                
                if (!done)
                    throw new Error ("Order expected to be done");
            }
            else cancel (timestamp);
        }
        
        private void placeImmediateOrCancel (long timestamp) throws OrderException
        {
            try
            {
                entryHandler = orderBook.placeEntry (timestamp, convertSide (getSide ()), getOrderedQuantity (), limitPrice, this, this);
            }
            catch (OrderBookException ex)
            {
                throw new OrderException ("Cannot place order book entry", ex);
            }
            
            if (!done)
                cancel (timestamp);
        }
    }
    
    private static OrderBookEntrySide convertSide (OrderSide side)
    {
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        switch (side)
        {
        case BUY:
            return OrderBookEntrySide.BID;
        case SELL:
            return OrderBookEntrySide.ASK;
        default:
            throw new Error ("Unknown order side: " + side);
        }
    }
}
