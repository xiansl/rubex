package com.googlecode.rubex.orderbook.event;

import java.util.EventObject;

import com.googlecode.rubex.orderbook.OrderBook;
import com.googlecode.rubex.orderbook.OrderBookEntryHandler;
import com.googlecode.rubex.orderbook.OrderBookEntrySide;

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
    private final OrderBookEntryHandler bidEntryHandler;
    private final OrderBookEntryHandler askEntryHandler;
    private final long quantity;
    private final long price;

    /**
     * Create new trade event with given source, timestamp, quantity and price.
     * 
     * @param source event source
     * @param timestamp time when event occurred in milliseconds since epoch
     * @param bidEntryHandler handler of bid entry participated in trade
     * @param askEntryHandler handler of ask entry participated in trade
     * @param quantity trade quantity in quantity units
     * @param price trade price in price units
     * 
     * @see System#currentTimeMillis()
     */
    public OrderBookTradeEvent (
        Object source, long timestamp, 
        OrderBookEntryHandler bidEntryHandler, 
        OrderBookEntryHandler askEntryHandler, 
        long quantity, long price)
    {
        super (source);
        
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
        
        this.timestamp = timestamp;
        this.bidEntryHandler = bidEntryHandler;
        this.askEntryHandler = askEntryHandler;
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
     * Return handler of bid entry participated in trade.
     */
    public OrderBookEntryHandler getBidEntryHandler ()
    {
        return bidEntryHandler;
    }

    /**
     * Return handler of ask entry participated in trade.
     */
    public OrderBookEntryHandler getAskEntryHandler ()
    {
        return askEntryHandler;
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
