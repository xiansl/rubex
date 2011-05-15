package com.googlecode.rubex.orderbook.event;

import java.util.EventListener;

public interface OrderBookListener extends EventListener
{
    public void onTrade (OrderBookTradeEvent event);
    public void onQuote (OrderBookQuoteEvent event);
}
