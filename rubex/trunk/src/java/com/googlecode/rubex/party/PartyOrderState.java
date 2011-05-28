package com.googlecode.rubex.party;

/**
 * State of party order.
 * 
 * @author Mikhail Vladimirov
 */
public enum PartyOrderState
{
    /**
     * Order is received.
     */
    NEW,
    
    /**
     * Order is received and placed on exchange.
     */
    OPEN,
    
    /**
     * Order is partially filled.
     */
    PARTIALLY_FILLED,
    
    /**
     * Order is completely filled.
     */
    FILLED,
    
    /**
     * Order was replaced.
     */
    REPLACED,
    
    /**
     * Order was canceled.
     */
    CANCELED,
    
    /**
     * Order was rejected.
     */
    REJECTED
}
