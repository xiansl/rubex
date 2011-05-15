package com.googlecode.rubex.marketdata;

public interface MarketDataTracker
{
    public long getLastTradePrice ();
    
    public long getLastTradeTimestamp ();
    
    public long getLastTradeQuantity ();
    
    public long getTotalVolume ();
    
    public long getTotalValue ();
    
    public long getBestBidPrice ();
    
    public long getBestBidQuantity ();
    
    public long getBestAskPrice ();
    
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
