package com.googlecode.rubex.protocol;

/**
 * Cancel order request.
 * 
 * @author Mikhail Vladimirov
 */
public interface CancelOrderProtocolMessage extends ProtocolMessage
{
    /**
     * Name of the order ID field.
     */
    public final static String ORDER_ID = "orderID";
    
    /**
     * ID of the order to be canceled.
     */
    public long getOrderID ();
}
