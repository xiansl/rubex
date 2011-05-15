package com.googlecode.rubex.orderbook.event;

import java.util.EventObject;

import com.googlecode.rubex.orderbook.OrderBook;
import com.googlecode.rubex.orderbook.OrderBookEntrySide;

/**
 * Contains details about quote event.
 * 
 * @see OrderBook
 *
 * @author Mikhail Vladimirov
 */
public class OrderBookQuoteEvent extends EventObject
{
    private final long timestamp;
    private final OrderBookEntrySide side;
    private final long price;
    private final long quantityDelta;

    /**
     * Create new quote event with given source, timestamp, side, price and quantity delta.
     * 
     * @param source event source
     * @param timestamp time when event occurred in milliseconds since epoch.
     * @param side quote side
     * @param price quote price
     * @param quantityDelta quote quantity delta
     * 
     * @see System#currentTimeMillis()
     */
    public OrderBookQuoteEvent (
        Object source, 
        long timestamp, OrderBookEntrySide side, long price, long quantityDelta)
    {
        super (source);
        
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
        
        this.timestamp = timestamp;
        this.side = side;
        this.price = price;
        this.quantityDelta = quantityDelta;
    }

    /**
     * Return event timestamp as milliseconds since epoch.
     * 
     * @see System#currentTimeMillis()
     */
    public long getTimestamp ()
    {
        return timestamp;
    }

    /**
     * Return quote side.
     */
    public OrderBookEntrySide getSide ()
    {
        return side;
    }

    /**
     * Return quote price in price units.
     */
    public long getPrice ()
    {
        return price;
    }

    /**
     * Return quote quantity delta in quantity units.
     */
    public long getQuantityDelta ()
    {
        return quantityDelta;
    }
}
