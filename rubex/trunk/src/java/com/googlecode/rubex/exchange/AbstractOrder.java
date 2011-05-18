package com.googlecode.rubex.exchange;

import com.googlecode.rubex.utils.LongUtils;

public abstract class AbstractOrder implements Order
{
    private final OrderSide side;
    private final long orderedQuantity;
    private final OrderCallback callback;
    private final Object closure;
    
    private long filledQuantity = 0L;
    private long filledValue = 0L;
    
    public AbstractOrder (OrderSide side, long orderedQuantity, OrderCallback callback, Object closure)
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

    @Override
    public OrderSide getSide ()
    {
        return side;
    }

    @Override
    public long getOrderedQuantity ()
    {
        return orderedQuantity;
    }

    @Override
    public long getFilledQuantity ()
    {
        return filledQuantity;
    }

    @Override
    public long getFilledValue ()
    {
        return filledValue;
    }

    @Override
    public Object getClosure ()
    {
        return closure;
    }

    protected void fill (long quantity, long price)
    {
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (price <= 0)
            throw new IllegalArgumentException ("Price <= 0");
        
        if (quantity > orderedQuantity - filledQuantity)
            throw new IllegalStateException ("Fill quantity > unfilled quantity"); 
        
        filledQuantity = LongUtils.safeAdd (filledQuantity, quantity);
        filledValue = LongUtils.safeAdd (filledValue, LongUtils.safeMultiply (quantity, price));
    }
    
    protected void fireOnFill (long timestamp, long quantity, long price)
    {
        callback.onFill (timestamp, this, quantity, price);
    }

    protected void fireOnFilled (long timestamp)
    {
        callback.onFilled (timestamp, this);
    }

    protected void fireOnCanceled (long timestamp)
    {
        callback.onCanceled (timestamp, this);
    }

    protected void fireOnReplaced (long timestamp, Order newOrder)
    {
        callback.onReplaced (timestamp, this, newOrder);
    }
}
