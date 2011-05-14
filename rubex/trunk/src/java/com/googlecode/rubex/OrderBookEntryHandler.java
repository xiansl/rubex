package com.googlecode.rubex;

public interface OrderBookEntryHandler
{
    public OrderBookEntryType getEntryType (); 
    
    public long getUnfilledQuantity ();
    
    public long getLimitPrice ();
    
    public Object getClosure ();
    
    public void cancel (long timestamp);
}
