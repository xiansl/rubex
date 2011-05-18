package com.googlecode.rubex.exchange;

public interface StopOrder extends Order
{
    public long getStopPrice ();
    
    
    public MarketOrder replaceWithMarketOrder (long timestamp, long newQuantity, OrderCallback callback, Object closure) throws OrderException;
    
    public MarketOrder replaceWithLimitOrder (long timestamp, long newQuantity, long newLimitPrice, OrderTimeInForce newTimeInForce, OrderCallback callback, Object closure) throws OrderException;

    public StopOrder replaceWithStopOrder (long timestamp, long newQuantity, long newStopPrice, OrderCallback callback, Object closure) throws OrderException;
    
    public StopLimitOrder replaceWithStopLimitOrder (long timestamp, long newQuantity, long newStopPrice, long newLimitPrice, OrderCallback callback, Object closure) throws OrderException;
    
    public IcebergOrder replaceWithIcebergOrder (long timestamp, long newQuantity, long newLimitPrice, long newVisibleQuantity, OrderCallback callback, Object closure) throws OrderException; 
}
