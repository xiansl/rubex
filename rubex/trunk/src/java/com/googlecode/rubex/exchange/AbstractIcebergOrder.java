package com.googlecode.rubex.exchange;

/**
 * Abstract base class for implementations of {@link IcebergOrder} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractIcebergOrder extends AbstractOrder implements IcebergOrder
{
    private final long limitPrice;
    private final long visibleQuantity;
    
    /**
     * Create new abstract iceberg order with given side, ordered quantity, 
     * callback and closure.
     *  
     * @param side order side
     * @param orderedQuantity ordered quantity
     * @param limitPrice limit price in price units
     * @param visibleQuantity visible quantity in quantity units
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     */
    public AbstractIcebergOrder (OrderSide side, long orderedQuantity,
        long limitPrice, long visibleQuantity,
        OrderCallback callback, Object closure)
    {
        super (side, orderedQuantity, callback, closure);
        
        if (limitPrice <= 0)
            throw new IllegalArgumentException ("Limit price <= 0");
        
        if (visibleQuantity <= 0)
            throw new IllegalArgumentException ("Visible quantity <= 0");
        
        this.limitPrice = limitPrice;
        this.visibleQuantity = visibleQuantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderType getType ()
    {
        return OrderType.ICEBERG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitIcebergOrder (this);
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
    public long getVisibleQuantity ()
    {
        return visibleQuantity;
    }
}
