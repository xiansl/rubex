package com.googlecode.rubex.exchange;

public abstract class AbstractStopOrder implements StopOrder
{
    @Override
    public OrderType getType ()
    {
        return OrderType.STOP;
    }

    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitStopOrder (this);
    }
}
