package com.googlecode.rubex.orderbook;

/**
 * Side of the order book entry.
 * 
 * @see OrderBook
 * 
 * @author Mikhail Valadimirov
 */
public enum OrderBookEntrySide
{
    /**
     * Bid (a.k.a. buy) entry.
     */
    BID, 
    
    /**
     * Ask (a.k.a sell or offer) entry.
     */
    ASK
}
