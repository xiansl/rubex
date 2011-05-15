package com.googlecode.rubex.exchange;

public class OrderException extends Exception
{
    public OrderException ()
    {
        super ();
    }

    public OrderException (String message, Throwable cause)
    {
        super (message, cause);
    }

    public OrderException (String message)
    {
        super (message);
    }

    public OrderException (Throwable cause)
    {
        super (cause);
    }
}
