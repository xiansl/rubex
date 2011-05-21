package com.googlecode.rubex.exchange;

/**
 * Abstract base class for implementations of {@link LimitOrder} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractLimitOrder 
    extends AbstractOrder implements LimitOrder
{
    private final long limitPrice;
    private final OrderTimeInForce timeInForce;
    
    public AbstractLimitOrder (OrderSide side, long orderedQuantity,
        long limitPrice, OrderTimeInForce timeInForce,
        OrderCallback callback, Object closure)
    {
        super (side, orderedQuantity, callback, closure);
        
        if (limitPrice <= 0)
            throw new IllegalArgumentException ("Limit price <= 0");
        
        if (timeInForce == null)
            throw new IllegalArgumentException ("Time in force is null");
        
        this.limitPrice = limitPrice;
        this.timeInForce = timeInForce;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderType getType ()
    {
        return OrderType.LIMIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitLimitOrder (this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLimitPrice ()
    {
        return limitPrice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderTimeInForce getTimeInForce ()
    {
        return timeInForce;
    }
}
