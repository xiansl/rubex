package com.googlecode.rubex.protocol;

public enum ProtocolMessageType
{
    /**
     * Notification about rejected message.
     */
    REJECT,
    
    /**
     * New order request.
     */
    NEW_ORDER,
    
    /**
     * Replace order request.
     */
    REPLACE_ORDER,
    
    /**
     * Cancel order request.
     */
    CANCEL_ORDER,
    
    /**
     * Order status message.
     */
    ORDER_STATUS
}
