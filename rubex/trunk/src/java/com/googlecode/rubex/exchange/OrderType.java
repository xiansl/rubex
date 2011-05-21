package com.googlecode.rubex.exchange;

/**
 * Type of the order.
 * 
 * @author Mikhail Vladimirov
 */
public enum OrderType
{
    /**
     * Market order.
     */
    MARKET,
    
    /**
     * Limit order.
     */
    LIMIT,
    
    /**
     * Stop order.
     */
    STOP,
    
    /**
     * Stop limit order.
     */
    STOP_LIMIT,
    
    /**
     * Iceberg order.
     */
    ICEBERG
}
