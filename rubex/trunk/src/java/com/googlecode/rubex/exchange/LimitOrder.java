package com.googlecode.rubex.exchange;

public interface LimitOrder extends Order
{
    public long getLimitPrice ();
    
    public OrderTimeInForce getTimeInForce ();
    
    public MarketOrder replaceWithMarketOrder (long timestamp, long newQuantity, OrderCallback callback, Object closure) throws OrderException;
    
    public MarketOrder replaceWithLimitOrder (long timestamp, long newQuantity, long newLimitPrice, OrderTimeInForce newTimeInForce, OrderCallback callback, Object closure) throws OrderException;
    
    public IcebergOrder replaceWithIcebergOrder (long timestamp, long newQuantity, long newLimitPrice, long newVisibleQuantity, OrderCallback callback, Object closure) throws OrderException; 
}
