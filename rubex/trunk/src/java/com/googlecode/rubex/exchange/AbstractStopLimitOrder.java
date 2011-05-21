package com.googlecode.rubex.exchange;

/**
 * Abstract base class for implementations of {@link StopLimitOrder} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractStopLimitOrder 
    extends AbstractOrder implements StopLimitOrder
{
    private final long stopPrice;
    private final long limitPrice;

    /**
     * Create new abstract stop limit order with given side, ordered quantity, 
     * stop price, limit price, callback and closure.
     * 
     * @param side order side
     * @param orderedQuantity ordered quantity
     * @param stopPrice stop price in price units
     * @param limitPrice limit price in price units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     */
    public AbstractStopLimitOrder (OrderSide side, long orderedQuantity,
        long stopPrice, long limitPrice,
        OrderCallback callback, Object closure)
    {
        super (side, orderedQuantity, callback, closure);
        
        if (stopPrice <= 0)
            throw new IllegalArgumentException ("Stop price <= 0");
        
        if (limitPrice <= 0)
            throw new IllegalArgumentException ("Limit price <= 0");
        
        this.stopPrice = stopPrice;
        this.limitPrice = limitPrice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderType getType ()
    {
        return OrderType.STOP_LIMIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitStopLimitOrder (this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getStopPrice ()
    {
        return stopPrice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLimitPrice ()
    {
        return limitPrice;
    }
}
