package com.googlecode.rubex.exchange;

public interface Order
{
    public OrderType getType ();
    
    public <T> T accept (OrderVisitor <T> visitor);

    public OrderSide getSide ();
    
    public long getOrderedQuantity ();
    
    public long getFilledQuantity ();
    
    public long getFilledValue ();
    
    public Object getClosure ();
}
