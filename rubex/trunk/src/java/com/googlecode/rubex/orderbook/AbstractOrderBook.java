package com.googlecode.rubex.orderbook;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.orderbook.event.OrderBookListener;
import com.googlecode.rubex.orderbook.event.OrderBookQuoteEvent;
import com.googlecode.rubex.orderbook.event.OrderBookTradeEvent;

/**
 * Abstract base class for implementations of {@link OrderBook} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractOrderBook implements OrderBook
{
    private final List <OrderBookListener> listeners = 
        new ArrayList <OrderBookListener> ();

    /**
     * Create new abstract order book.
     */
    public AbstractOrderBook ()
    {
        // Do nothing
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addOrderBookListener (OrderBookListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        listeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOrderBookListener (OrderBookListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        if (!listeners.remove (listener))
            throw new IllegalStateException ("No such listener");
    }

    /**
     * Return all order book listeners for this order book.
     * 
     * @return an array of {@link OrderBookListener} objects
     */
    public OrderBookListener [] getAllOrderBookListeners ()
    {
        return listeners.toArray (new OrderBookListener[listeners.size ()]);
    }
    
    /**
     * Notify all order book listeners about trade.
     * 
     * @param timestamp time when event occurred in milliseconds since epoch.
     * @param bidEntryHandler handler of bid entry participated in trade
     * @param askEntryHandler handler of ask entry participated in trade
     * @param quantity trade quantity in quantity units
     * @param price trade price in price units
     * 
     * @see System#currentTimeMillis()
     * @see OrderBookTradeEvent
     */
    protected void fireOnTrade (
        long timestamp, 
        OrderBookEntryHandler bidEntryHandler, 
        OrderBookEntryHandler askEntryHandler, 
        long quantity, long price)
    {
        if (bidEntryHandler == null)
            throw new IllegalArgumentException ("Bid entry handler is null");
        
        if (askEntryHandler == null)
            throw new IllegalArgumentException ("Ask entry handler is null");
        
        if (!OrderBookEntrySide.BID.equals (bidEntryHandler.getEntrySide ()))
            throw new IllegalArgumentException ("Bid entry handler is not bid");
        
        if (!OrderBookEntrySide.ASK.equals (askEntryHandler.getEntrySide ()))
            throw new IllegalArgumentException ("Ask entry handler is not ask");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
        
        OrderBookTradeEvent event = null;
        
        for (OrderBookListener listener: listeners)
        {
            if (event == null)
                event = new OrderBookTradeEvent (
                    this, timestamp, 
                    bidEntryHandler, askEntryHandler, 
                    quantity, price);
            
            listener.onTrade (event);
        }
    }
    
    /**
     * Notify all order book listeners about quote event.
     * 
     * @param timestamp time when event occurred in milliseconds since epoch.
     * @param side quote side
     * @param price quote price in price units
     * @param quantityDelta delta of the quantity in quantity units
     * 
     * @see System#currentTimeMillis()
     * @see OrderBookQuoteEvent
     */
    protected void fireOnQuote (
        long timestamp, OrderBookEntrySide side, long price, long quantityDelta)
    {
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
        
        OrderBookQuoteEvent event = null;
        
        for (OrderBookListener listener: listeners)
        {
            if (event == null)
                event = new OrderBookQuoteEvent (
                    this, timestamp, side, price, quantityDelta);
            
            listener.onQuote (event);
        }
    }
}
