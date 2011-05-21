package com.googlecode.rubex.exchange;

/**
 * Receives notifications about order events.
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderCallback
{
    /**
     * Called when order was filled.
     * 
     * @param timestamp time when fill occurred in milliseconds since epoch.
     * @param order order that was filled
     * @param quantity fill quantity in quantity units
     * @param price fill price in price units
     */
    public void onFill (long timestamp, Order order, long quantity, long price);
    
    /**
     * Called when order was filled completely.
     * 
     * @param timestamp time when fill occurred in milliseconds since epoch.
     * @param order order that was filled
     */
    public void onFilled (long timestamp, Order order);
    
    /**
     * Called when order was canceled.
     * 
     * @param timestamp time when order was canceled in milliseconds since epoch.
     * @param order order that was canceled
     */
    public void onCanceled (long timestamp, Order order);
    
    /**
     * Called when order was replaced.
     * 
     * @param timestamp time when order was replaced with new order in milliseconds since epoch.
     * @param order order that was replaced with new order
     * @param newOrder new order
     */
    public void onReplaced (long timestamp, Order order, Order newOrder);
}
