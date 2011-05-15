package com.googlecode.rubex.orderbook;

/**
 * Signals an error in order book, i.e. invalid entry.
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
