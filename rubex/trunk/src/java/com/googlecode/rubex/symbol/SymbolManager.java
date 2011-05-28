package com.googlecode.rubex.symbol;

import com.googlecode.rubex.exchange.Exchange;

/**
 * Manages symbols allowed for trading.
 * 
 * @author Mikhail Vladimirov
 */
public interface SymbolManager
{
    /**
     * Get all available symbols.
     * 
     * @return an array of strings
     */
    public String [] getAllSymbols ();
    
    /**
     * Get exchange for given symbol.
     * 
     * @param symbol symbol to get exchange to
     * @return exchange for given symbol
     * @throws IllegalArgumentException if given symbol does not exists
     */
    public Exchange getSymbolExchange (String symbol)
        throws IllegalArgumentException;
    
    /**
     * Execute given runnable in a thread that belongs to given symbol.
     * 
     * @param symbol symbol to use to choose proper thread
     * @param runnable runnable to be executed
     * @throws IllegalArgumentException if given symbol does not exists
     */
    public void executeInSymbolThread (
        String symbol, Runnable runnable)
        throws IllegalArgumentException;
}
