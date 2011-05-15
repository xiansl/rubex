package com.googlecode.rubex.orderbook.event;

import java.util.EventObject;

public class OrderBookTradeEvent extends EventObject
{
    private final long timestamp;
    private final long quantity;
    private final long price;

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

    public long getTimestamp ()
    {
        return timestamp;
    }

    public long getQuantity ()
    {
        return quantity;
    }

    public long getPrice ()
    {
        return price;
    }
}
