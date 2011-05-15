package com.googlecode.rubex.exchange;

public interface StopOrder extends Order
{
    public long getStopPrice ();
    
    public StopOrder replaceWithStopOrder (long timestamp, long newQuantity, long newStopPrice, OrderCallback callback, Object closure) throws OrderException;
    
    public StopLimitOrder replaceWithStopLimitOrder (long timestamp, long newQuantity, long newStopPrice, long newLimitPrice, OrderCallback callback, Object closure) throws OrderException;
}
