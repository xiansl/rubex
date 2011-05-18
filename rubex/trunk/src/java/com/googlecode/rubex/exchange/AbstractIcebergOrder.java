package com.googlecode.rubex.exchange;

public abstract class AbstractIcebergOrder implements IcebergOrder
{
    @Override
    public OrderType getType ()
    {
        return OrderType.ICEBERG;
    }

    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitIcebergOrder (this);
    }
}
