package com.googlecode.rubex.exchange;

public interface OrderCallback
{
    public void onFill (long timestamp, Order order, long quantity, long price);
    
    public void onFilled (long timestamp, Order order);
    
    public void onCanceled (long timestamp, Order order);
    
    public void onReplaced (long timestamp, Order order, Order newOrder);
}
