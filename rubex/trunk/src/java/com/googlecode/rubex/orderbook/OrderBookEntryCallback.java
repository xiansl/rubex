package com.googlecode.rubex.orderbook;

public interface OrderBookEntryCallback
{
    public void onFill (long timestamp, OrderBookEntryHandler handler, long quantity, long price);
    
    public void onFilled (long timestamp, OrderBookEntryHandler handler);
    
    public void onCanceled (long timestamp, OrderBookEntryHandler handler);
}
