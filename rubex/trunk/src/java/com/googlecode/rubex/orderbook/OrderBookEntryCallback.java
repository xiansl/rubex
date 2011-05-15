package com.googlecode.rubex.orderbook;

/**
 * Receives notifications about events related to particular order book entry.
 * 
 * @see OrderBook
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderBookEntryCallback
{
    /**
     * Called when entry was filled.
     * 
     * @param timestamp time when entry was filled in milliseconds since epoch.
     * @param handler handler of the corresponding order book entry
     * @param quantity fill quantity in quantity units
     * @param price fill price in price units
     * 
     * @see System#currentTimeMillis()
     */
    public void onFill (
        long timestamp, OrderBookEntryHandler handler, 
        long quantity, long price);
    
    /**
     * Called after entry was completely filled.
     * 
     * @param timestamp time when entry was completely filled in milliseconds 
     *                  since epoch.
     * @param handler handler of the corresponding order book entry
     * 
     * @see System#currentTimeMillis()
     */
    public void onFilled (long timestamp, OrderBookEntryHandler handler);
    
    /**
     * Called after entry was canceled.
     * 
     * @param timestamp time when entry was canceled in milliseconds since 
     *        epoch.
     * @param handler handler of the corresponding order book entry
     * 
     * @see System#currentTimeMillis()
     */
    public void onCanceled (long timestamp, OrderBookEntryHandler handler);
}
