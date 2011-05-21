package com.googlecode.rubex.exchange;

/**
 * Exchange where orders can be placed.
 * 
 * @author Mikhail Vladimirov
 */
public interface Exchange
{
    /**
     * Create new market order with given side, quantity, callback and closure.
     * 
     * @param timestamp time in milliseconds since epoch when order is created.
     * @param side order side
     * @param quantity order quantity in quantity units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     * @return new market order
     * @throws OrderException if market order cannot be created
     */
    public MarketOrder createMarketOrder (
        long timestamp, 
        OrderSide side, long quantity, 
        OrderCallback callback, Object closure) throws OrderException;
    
    /**
     * Create new limit order with given side, quantity, limit price, time in 
     * force, callback and closure.
     * 
     * @param timestamp time in milliseconds since epoch when order is created.
     * @param side order side
     * @param quantity order quantity in quantity units
     * @param limitPrice limit price in price units
     * @param timeInForce time in force
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     * @return new limit order
     * @throws OrderException if limit order cannot be created
     */
    public LimitOrder createLimitOrder (
        long timestamp, 
        OrderSide side, long quantity, long limitPrice, 
        OrderTimeInForce timeInForce, 
        OrderCallback callback, Object closure) throws OrderException;
    
    /**
     * Create new stop order with given side, quantity, stop price, callback 
     * and closure.
     * 
     * @param timestamp time in milliseconds since epoch when order is created.
     * @param side order side
     * @param quantity order quantity in quantity units
     * @param stopPrice stop price in price units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     * @return new stop order
     * @throws OrderException if stop order cannot be created
     */
    public StopOrder createStopOrder (
        long timestamp, 
        OrderSide side, long quantity, long stopPrice, 
        OrderCallback callback, Object closure) throws OrderException;
    
    /**
     * Create new stop limit order with given side, quantity, stop price, 
     * limit price, callback and closure.
     * 
     * @param timestamp time in milliseconds since epoch when order is created.
     * @param side order side
     * @param quantity order quantity in quantity units
     * @param stopPrice stop price in price units
     * @param limitPrice limit price in price units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     * @return new stop limit order
     * @throws OrderException if stop limit order cannot be created
     */
    public StopLimitOrder createStopLimitOrder (
        long timestamp, 
        OrderSide side, long quantity, 
        long stopPrice, long limitPrice, 
        OrderCallback callback, Object closure) throws OrderException;
    
    /**
     * Create new iceberg order with given side, quantity, limit price, 
     * visible quantity, callback and closure.
     * 
     * @param timestamp time in milliseconds since epoch when order is created.
     * @param side order side
     * @param quantity order quantity in quantity units
     * @param limitPrice limit price in price units
     * @param visibleQuantity visible quantity in quantity units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     * @return new iceberg order
     * @throws OrderException if iceberg order cannot be created
     */
    public IcebergOrder createIcebergOrder (
        long timestamp, 
        OrderSide side, 
        long quantity, long limitPrice, long visibleQuantity, 
        OrderCallback callback, Object closure) throws OrderException;
}
