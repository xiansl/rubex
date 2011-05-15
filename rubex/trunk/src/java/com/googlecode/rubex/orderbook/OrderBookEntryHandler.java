package com.googlecode.rubex.orderbook;

/**
 * Handler associated to particular order book entry.
 * 
 * @see OrderBook
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderBookEntryHandler
{
    /**
     * Return side of the corresponding order book entry.
     */
    public OrderBookEntrySide getEntrySide (); 
    
    /**
     * Return unfilled quantity of the corresponding order book entry in 
     * quantity units.
     */
    public long getUnfilledQuantity ();
    
    /**
     * Return limit price of the corresponding order book entry in price units.
     */
    public long getLimitPrice ();
    
    /**
     * Return closure object of the corresponding order book entry.
     */
    public Object getClosure ();
    
    /**
     * Cancel corresponding order book entry.
     * 
     * @param timestamp time when cancel occurred in milliseconds since epoch
     * @throws OrderBookException if entry could not be canceled
     * 
     * @see System#currentTimeMillis()
     */
    public void cancel (long timestamp) throws OrderBookException;
}
