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
    
    /**
     * Get best bid quotes.
     * 
     * @param maximumCount maximum number of quotes to return
     * @return an array of {@link Quote} objects
     */
    public Quote [] getBidQuotes (int maximumCount);
    
    /**
     * Get best ask quotes.
     * 
     * @param maximumCount maximum number of quotes to return
     * @return an array of {@link Quote} objects
     */
    public Quote [] getAskQuotes (int maximumCount);
    
    /**
     * Get total bid quantity in quantity units with price >= given threshold 
     * price.
     * 
     * @param price threshold price
     * @return total bid quantity above given threshold price
     */
    public long getBidQuantityAbove (long price);
    
    /**
     * Get total ask quantity in quantity units with price <= given threshold 
     * price.
     * 
     * @param price threshold price
     * @return total ask quantity below given threshold price
     */
    public long getAskQuantityBelow (long price);
    
    /**
     * Represents single quote.
     */
    public static interface Quote
    {
        /**
         * Return quote quantity in quantity units.
         */
        public long getQuantity ();
        
        /**
         * Return quote price in price units.
         */
        public long getPrice ();
    }
}
