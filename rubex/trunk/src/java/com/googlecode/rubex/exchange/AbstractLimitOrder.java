package com.googlecode.rubex.exchange;

public abstract class AbstractLimitOrder extends AbstractOrder implements LimitOrder
{
    public AbstractLimitOrder (OrderSide side, long orderedQuantity,
            OrderCallback callback, Object closure)
    {
        super (side, orderedQuantity, callback, closure);
    }

    @Override
    public OrderType getType ()
    {
        return OrderType.LIMIT;
    }

    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitLimitOrder (this);
    }
}
