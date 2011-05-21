package com.googlecode.rubex.exchange;

import com.googlecode.rubex.utils.LongUtils;

/**
 * Abstract base class for implementations of {@link Order} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractOrder implements Order
{
    private final OrderSide side;
    private final long orderedQuantity;
    private final OrderCallback callback;
    private final Object closure;
    
    private long filledQuantity = 0L;
    private long filledValue = 0L;
    
    /**
     * Create new abstract order with given side, ordered quantity, callback 
     * and closure.
     * 
     * @param side order side
     * @param orderedQuantity ordered quantity
     * @param callback callback to be notified about order events
     * @param closure closure object to be assigned to the order or 
     *        <code>null</code> if there is no closure
     */
    public AbstractOrder (
        OrderSide side, long orderedQuantity, 
        OrderCallback callback, Object closure)
    {
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (orderedQuantity <= 0)
            throw new IllegalArgumentException ("Ordered quantity <= 0");
        
        if (callback == null)
            throw new IllegalArgumentException ("Callback is null");
        
        this.side = side;
        this.orderedQuantity = orderedQuantity;
        this.callback = callback;
        this.closure = closure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderSide getSide ()
    {
        return side;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getOrderedQuantity ()
    {
        return orderedQuantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFilledQuantity ()
    {
        return filledQuantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFilledValue ()
    {
        return filledValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getClosure ()
    {
        return closure;
    }

    /**
     * Adjust filled quantity and filled value according to given fill.
     * 
     * @param quantity fill quantity in quantity units
     * @param price fill price in price units
     */
    protected void fill (long quantity, long price)
    {
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
        
        if (quantity > orderedQuantity - filledQuantity)
            throw new IllegalStateException (
                "Fill quantity > unfilled quantity"); 
        
        filledQuantity = LongUtils.safeAdd (filledQuantity, quantity);
        filledValue = LongUtils.safeAdd (
            filledValue, LongUtils.safeMultiply (quantity, price));
    }

    /**
     * Notify callback about fill.
     * 
     * @param timestamp time when fill occurred in milliseconds since epoch
     * @param quantity fill quantity in quantity units
     * @param price fill price in price units
     */
    protected void fireOnFill (long timestamp, long quantity, long price)
    {
        callback.onFill (timestamp, this, quantity, price);
    }

    /**
     * Notify callback that order was filled completely.
     * 
     * @param timestamp time when order was filled in milliseconds since epoch
     */
    protected void fireOnFilled (long timestamp)
    {
        callback.onFilled (timestamp, this);
    }

    /**
     * Notify callback that order was canceled.
     * 
     * @param timestamp time when order was canceled in milliseconds since epoch
     */
    protected void fireOnCanceled (long timestamp)
    {
        callback.onCanceled (timestamp, this);
    }

    /**
     * Notify callback that order was replaced with new order.
     * 
     * @param timestamp time when order was replaced with new order in 
     * milliseconds since epoch
     * @param newOrder new order
     */
    protected void fireOnReplaced (long timestamp, Order newOrder)
    {
        callback.onReplaced (timestamp, this, newOrder);
    }
}
