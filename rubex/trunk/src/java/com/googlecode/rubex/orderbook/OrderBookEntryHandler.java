package com.googlecode.rubex.orderbook;

public interface OrderBookEntryHandler
{
    public OrderBookEntrySide getEntrySide (); 
    
    public long getUnfilledQuantity ();
    
    public long getLimitPrice ();
    
    public Object getClosure ();
    
    public void cancel (long timestamp) throws OrderBookException;
}
