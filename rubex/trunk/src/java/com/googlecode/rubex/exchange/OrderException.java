package com.googlecode.rubex.exchange;

/**
 * Signals an order-related error on exchange, e.g. invalid order.
 * 
 * @author Mikhail Vladimirov
 */
public class OrderException extends Exception
{
    /**
     * @see Exception#Exception()
     */
    public OrderException ()
    {
        super ();
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public OrderException (String message, Throwable cause)
    {
        super (message, cause);
    }

    /**
     * @see Exception#Exception(String)
     */
    public OrderException (String message)
    {
        super (message);
    }

    /**
     * @see Exception#Exception(Throwable)
     */
    public OrderException (Throwable cause)
    {
        super (cause);
    }
}
