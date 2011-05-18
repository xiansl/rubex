package com.googlecode.rubex.exchange;

public abstract class AbstractStopLimitOrder implements StopLimitOrder
{
    @Override
    public OrderType getType ()
    {
        return OrderType.STOP_LIMIT;
    }

    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitStopLimitOrder (this);
    }
}
