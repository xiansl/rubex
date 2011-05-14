package com.googlecode.rubex;

/**
 * Holds list of open bids and offers.
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderBook
{
    /**
     * Place an order book entry with given timestamp, entry type, quantity, limit price, callback and closure.
     * 
     * @param timestamp time of the bid
     * @param type type of the order book entry
     * @param quantity bid quantity in quantity units
     * @param limitPrice limit price in price units or zero for market order
     * @param callback callback to be notified about entry events
     * @param closure closure to be passed to the callback
     * @return order book entry handler
     */
    public OrderBookEntryHandler placeEntry (
        long timestamp, OrderBookEntryType type,
        long quantity, long limitPrice, 
        OrderBookEntryCallback callback, Object closure) 
        throws OrderBookException;
}
