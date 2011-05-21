package com.googlecode.rubex.exchange;

/**
 * Time in force value of the order.
 * 
 * @author Mikhail Vladimirov
 */
public enum OrderTimeInForce
{
    /**
     * Day order.
     */
    DAY, 
    
    /**
     * Immediate or cancel order.
     */
    IOC, 
    
    /**
     * Fill or kill order.
     */
    FOK
}
