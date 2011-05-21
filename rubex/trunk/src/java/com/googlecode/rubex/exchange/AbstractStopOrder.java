package com.googlecode.rubex.exchange;

/**
 * Abstract base class for implementations of {@link StopOrder} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractStopOrder 
    extends AbstractOrder implements StopOrder
{
    private final long stopPrice;
    
    /**
     * Create new abstract stop order with given side, ordered quantity, stop 
     * price, callback and closure.
     * 
     * @param side order side
     * @param orderedQuantity ordered quantity
     * @param stopPrice stop price in price units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     */
    public AbstractStopOrder (OrderSide side, long orderedQuantity,
        long stopPrice, OrderCallback callback, Object closure)
    {
        super (side, orderedQuantity, callback, closure);
        
        if (stopPrice <= 0)
            throw new IllegalArgumentException ("Stop price <= 0");
        
        this.stopPrice = stopPrice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderType getType ()
    {
        return OrderType.STOP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitStopOrder (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getStopPrice ()
    {
        return stopPrice;
    }
}
