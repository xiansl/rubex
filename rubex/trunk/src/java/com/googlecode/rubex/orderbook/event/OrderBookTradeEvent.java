package com.googlecode.rubex.orderbook.event;

import java.util.EventObject;

import com.googlecode.rubex.orderbook.OrderBook;

/**
 * Contains details about trade event.
 * 
 * @see OrderBook
 *
 * @author Mikhail Vladimirov
 */
public class OrderBookTradeEvent extends EventObject
{
    private final long timestamp;
    private final long quantity;
    private final long price;

    /**
     * Create new trade event with given source, timestamp, quantity and price.
     * 
     * @param source event source
     * @param timestamp time when event occurred in milliseconds since epoch.
     * @param quantity trade quantity in quantity units
     * @param price trade price in price units
     * 
     * @see System#currentTimeMillis()
     */
    public OrderBookTradeEvent (Object source, long timestamp, long quantity, long price)
    {
        super (source);
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
        
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.price = price;
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
     * Return trade quantity in quantity units.
     */
    public long getQuantity ()
    {
        return quantity;
    }

    /**
     * Return trade price in price units.
     */
    public long getPrice ()
    {
        return price;
    }
}
