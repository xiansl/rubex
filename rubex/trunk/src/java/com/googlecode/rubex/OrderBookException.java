package com.googlecode.rubex;

/**
 * Signals an error in order book, i.e. invalid order.
 * 
 * @author Mikhail Vladimirov
 */
public class OrderBookException extends Exception
{
    /**
     * @see Exception#Exception()
     */
    public OrderBookException ()
    {
        super ();
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public OrderBookException (String message, Throwable cause)
    {
        super (message, cause);
    }

    /**
     * @see Exception#Exception(String)
     */
    public OrderBookException (String message)
    {
        super (message);
    }

    /**
     * @see Exception#Exception(Throwable)
     */
    public OrderBookException (Throwable cause)
    {
        super (cause);
    }
}
