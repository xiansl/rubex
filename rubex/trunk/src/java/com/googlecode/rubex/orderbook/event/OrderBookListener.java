package com.googlecode.rubex.orderbook.event;

import java.util.EventListener;

import com.googlecode.rubex.orderbook.OrderBook;

/**
 * Receives order book events.
 * 
 * @see OrderBook
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderBookListener extends EventListener
{
    /**
     * Called when trade event occurred in order book.
     * 
     * @param event trade event
     */
    public void onTrade (OrderBookTradeEvent event);
    
    /**
     * Called when quote event occurred in order book.
     * 
     * @param event quote event.
     */
    public void onQuote (OrderBookQuoteEvent event);
}
