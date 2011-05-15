package com.googlecode.rubex.orderbook.event;

import java.util.EventObject;

import com.googlecode.rubex.orderbook.OrderBookEntrySide;

public class OrderBookQuoteEvent extends EventObject
{
    private final long timestamp;
    private final OrderBookEntrySide side;
    private final long price;
    private final long quantityDelta;

    public OrderBookQuoteEvent (Object source, long timestamp, OrderBookEntrySide side, long price, long quantityDelta)
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

    public long getTimestamp ()
    {
        return timestamp;
    }

    public OrderBookEntrySide getSide ()
    {
        return side;
    }

    public long getPrice ()
    {
        return price;
    }

    public long getQuantityDelta ()
    {
        return quantityDelta;
    }
}
