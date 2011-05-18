package com.googlecode.rubex.exchange;

public abstract class AbstractMarketOrder extends AbstractOrder implements MarketOrder
{
    public AbstractMarketOrder (OrderSide side, long orderedQuantity, OrderCallback callback,
            Object closure)
    {
        super (side, orderedQuantity, callback, closure);
    }

    @Override
    public OrderType getType ()
    {
        return OrderType.MARKET;
    }

    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitMarketOrder (this);
    }
}
