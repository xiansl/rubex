package com.googlecode.rubex.exchange;

/**
 * Iceberg order.
 * 
 * @author Mikhail Vladimirov
 */
public interface IcebergOrder extends Order
{
    /**
     * Return limit price for the order in price units.
     */
    public long getLimitPrice ();
    
    /**
     * Return visible quantity for the order in quantity units.
     */
    public long getVisibleQuantity ();
    
    /**
     * Replace this order with new market order.
     * 
     * @param timestamp time when replace occurred in milliseconds since epoch
     * @param newQuantity ordered quantity for the new market order in quantity 
     *        units
     * @param callback callback for the new market order
     * @param closure closure object for the new market order or 
     *        <code>null</code> if there is no closure
     * @return new market order
     * @throws OrderException if order cannot be replaced with new market order
     */
    public MarketOrder replaceWithMarketOrder (
        long timestamp, long newQuantity, 
        OrderCallback callback, Object closure) throws OrderException;
    
    /**
     * Replace this order with new limit order.
     * 
     * @param timestamp time when replace occurred in milliseconds since epoch
     * @param newQuantity ordered quantity for the new limit order in quantity 
     *        units
     * @param newLimitPrice limit price for the new limit order in price units
     * @param newTimeInForce time in force for the new limit order
     * @param callback callback for the new limit order
     * @param closure closure object for the new limit order or 
     *        <code>null</code> if there is no closure
     * @return new limit order
     * @throws OrderException if order cannot be replaced with new limit order
     */
    public LimitOrder replaceWithLimitOrder (
        long timestamp, long newQuantity, long newLimitPrice, 
        OrderTimeInForce newTimeInForce, 
        OrderCallback callback, Object closure) throws OrderException;
    
    /**
     * Replace this order with new iceberg order.
     * 
     * @param timestamp time when replace occurred in milliseconds since epoch.
     * @param newQuantity ordered quantity for the new iceberg order in quantity 
     *        units
     * @param newLimitPrice limit price for the new iceberg order in price units
     * @param newVisibleQuantity visible quantity for the new iceberg order in 
     *        quantity units
     * @param callback callback for the new iceberg order.
     * @param closure closure object for the new iceberg order or 
     *        <code>null</code> if there is no closure.
     * @return new iceberg order
     * @throws OrderException if order cannot be replaced with new iceberg order
     */
    public IcebergOrder replaceWithIcebergOrder (
        long timestamp, long newQuantity, long newLimitPrice, 
        long newVisibleQuantity, OrderCallback callback, Object closure) 
        throws OrderException; 
}
