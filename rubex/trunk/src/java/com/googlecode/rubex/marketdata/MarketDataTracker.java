package com.googlecode.rubex.marketdata;

/**
 * Keeps track of market data.
 * 
 * @author Mikhail Vladimirov
 */
public interface MarketDataTracker
{
    /**
     * Return price of the last trade in price units or 0 if there were no 
     * trades yet.
     */
    public long getLastTradePrice ();
    
    /**
     * Return time of the last trade in milliseconds since epoch or 0 if there 
     * were no trades yet.
     * 
     * @see System#currentTimeMillis()
     */
    public long getLastTradeTimestamp ();
    
    /**
     * Return quantity of the last trade in quantity units or 0 if there were 
     * no trades yet.
     */
    public long getLastTradeQuantity ();
    
    /**
     * Return total volume (sum of quantities) of all trades in quantity units.
     */
    public long getTotalVolume ();
    
    /**
     * Return total value (sum of quantity * price) of all trades in price 
     * units.
     */
    public long getTotalValue ();
    
    /**
     * Return best bid price in price units or 0 if there is no bids.
     */
    public long getBestBidPrice ();
    
    /**
     * Return best bid quantity in quantity units or 0 if there is no bids.
     */
    public long getBestBidQuantity ();
    
    /**
     * Return best ask price in price units or 0 if there is no asks.
     */
    public long getBestAskPrice ();
    
    /**
     * Return best ask quantity in quantity units or 0 if there is no asks.
     */
    public long getBestAskQuantity ();
    
    public Quote [] getBidQuotes (int maximumCount);
    
    public Quote [] getAskQuotes (int maximumCount);
    
    public long getBidQuantityAbove (int price);
    
    public long getAskQuantityBelow (int price);
    
    public static interface Quote
    {
        public long getQuantity ();
        
        public long getPrice ();
    }
}
