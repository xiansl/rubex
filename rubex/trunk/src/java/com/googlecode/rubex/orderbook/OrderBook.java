package com.googlecode.rubex.orderbook;

import com.googlecode.rubex.orderbook.event.OrderBookListener;

/**
 * Holds list of open bids and offers.
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderBook
{
    /**
     * Add listener to be notified about order book events.
     * 
     * @param listener listener to be added
     */
    public void addOrderBookListener (OrderBookListener listener);
    
    /**
     * Remove order book listener.
     * 
     * @param listener listener to be removed
     */
    public void removeOrderBookListener (OrderBookListener listener);
    
    /**
     * Place an order book entry with given timestamp, entry side, quantity, limit price, callback and closure.
     * 
     * @param timestamp time of the bid
     * @param side side of the order book entry
     * @param quantity bid quantity in quantity units
     * @param limitPrice limit price in price units or zero for market order
     * @param callback callback to be notified about entry events
     * @param closure closure to be passed to the callback
     * @return order book entry handler
     */
    public OrderBookEntryHandler placeEntry (
        long timestamp, OrderBookEntrySide side,
        long quantity, long limitPrice, 
        OrderBookEntryCallback callback, Object closure) 
        throws OrderBookException;
}
