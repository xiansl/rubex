package com.googlecode.rubex.exchange;

/**
 * Abstract base class for implementation of {@link MarketOrder} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractMarketOrder 
    extends AbstractOrder implements MarketOrder
{
    /**
     * Create new abstract market order with given side, ordered quantity, 
     * callback and closure.
     * 
     * @param side order side
     * @param orderedQuantity ordered quantity
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     */
    public AbstractMarketOrder (OrderSide side, long orderedQuantity, OrderCallback callback,
            Object closure)
    {
        super (side, orderedQuantity, callback, closure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderType getType ()
    {
        return OrderType.MARKET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (OrderVisitor<T> visitor)
    {
        if (visitor == null)
            throw new IllegalArgumentException ("Visitor is null");
        
        return visitor.visitMarketOrder (this);
    }
}
