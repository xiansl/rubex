package com.googlecode.rubex.exchange;

public interface Exchange
{
    public MarketOrder createMarketOrder (
        long timestamp, 
        OrderSide side, long quantity, 
        OrderCallback callback, Object closure) throws OrderException;
    
    public LimitOrder createLimitOrder (
        long timestamp, 
        OrderSide side, long quantity, long limitPrice, 
        OrderTimeInForce timeInForce, 
        OrderCallback callback, Object closure) throws OrderException;
    
    public StopOrder createStopOrder (
        long timestamp, 
        OrderSide side, long quantity, long stopPrice, 
        OrderCallback callback, Object closure) throws OrderException;
    
    public StopLimitOrder createStopLimitOrder (
        long timestamp, 
        OrderSide side, long quantity, 
        long stopPrice, long limitPrice, 
        OrderCallback callback, Object closure) throws OrderException;
    
    public IcebergOrder createIcebergOrder (
        long timestamp, 
        OrderSide side, 
        long quantity, long limitPrice, long visibleQuantity, 
        OrderCallback callback, Object closure) throws OrderException;
}
