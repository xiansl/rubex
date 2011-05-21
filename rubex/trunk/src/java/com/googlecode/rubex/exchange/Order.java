package com.googlecode.rubex.exchange;

/**
 * Order placed on exchange.
 * 
 * @author Mikhail Vladimirov
 */
public interface Order
{
    /**
     * Return type of the order.
     */
    public OrderType getType ();
    
    /**
     * Accept visitor by calling appropriate visitXXX method on it.
     * 
     * @param visitor visitor to accept
     * @return visit result
     */
    public <T> T accept (OrderVisitor <T> visitor);

    /**
     * Return side of the order.
     */
    public OrderSide getSide ();
    
    /**
     * Return ordered quantity of the order in quantity units.
     */
    public long getOrderedQuantity ();
    
    /**
     * Return filled quantity of the order in quantity units.
     */
    public long getFilledQuantity ();
    
    /**
     * Return filled value of the order in price units.
     */
    public long getFilledValue ();
    
    /**
     * Return closure object associated to the order or <code>null</code> if 
     * there is no closure.
     */
    public Object getClosure ();
    
    /**
     * Cancel order.
     * 
     * @param timestamp time when cancel occurred in milliseconds since epoch
     * @throws OrderException if order cannot be canceled
     * 
     * @see System#currentTimeMillis()
     */
    public void cancel (long timestamp) throws OrderException;
}
