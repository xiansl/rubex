package com.googlecode.rubex.orderbook.test.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.rubex.orderbook.OrderBookEntryCallback;
import com.googlecode.rubex.orderbook.OrderBookEntryHandler;
import com.googlecode.rubex.orderbook.OrderBookEntrySide;
import com.googlecode.rubex.orderbook.SimpleOrderBook;

public class SimpleOrderBookTest
{
    private SimpleOrderBook orderBook;
    private OrderBookEntryCallback callback;
    private List<Object> events;
    
    @Before
    public void setUp () throws Exception
    {
        orderBook = new SimpleOrderBook ();
        callback = new MyOrderBookEntryCallback ();
        events = new ArrayList<Object> ();
    }

    @After
    public void tearDown () throws Exception
    {
        orderBook = null;
        callback = null;
        events = null;
    }

    @Test
    public void testPlaceBidMarketEmpty () throws Exception
    {
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.BID, 1000, 0, callback, "FOO");
        assertEquals (1, events.size ());
        assertContains (events, "CANCELED:123456789:FOO:0:1000");
    }

    @Test
    public void testPlaceAskMarketEmpty () throws Exception
    {
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.ASK, 1000, 0, callback, "FOO");
        assertEquals (1, events.size ());
        assertContains (events, "CANCELED:123456789:FOO:0:1000");
    }

    @Test
    public void testPlaceBidMarketOnlyBids () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 1000, 100, callback, "X");
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 1000, 200, callback, "Y");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.BID, 1000, 0, callback, "FOO");
        assertEquals (1, events.size ());
        assertContains (events, "CANCELED:123456789:FOO:0:1000");
    }

    @Test
    public void testPlaceBidMarketOnlyAsks () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 1000, 100, callback, "X");
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 1000, 200, callback, "Y");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.ASK, 1000, 0, callback, "FOO");
        assertEquals (1, events.size ());
        assertContains (events, "CANCELED:123456789:FOO:0:1000");
    }

    @Test
    public void testPlaceBidMarketPartialFillOne () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 500, 100, callback, "X");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.BID, 1000, 0, callback, "FOO");
        assertEquals (4, events.size ());
        assertContains (events, "FILL:123456789:X:100:0:500:100", "FILLED:123456789:X:100:0");
        assertContains (events, "FILL:123456789:FOO:0:500:500:100", "CANCELED:123456789:FOO:0:500");
    }

    @Test
    public void testPlaceAskMarketPartialFillOne () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 500, 100, callback, "X");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.ASK, 1000, 0, callback, "FOO");
        assertEquals (4, events.size ());
        assertContains (events, "FILL:123456789:X:100:0:500:100", "FILLED:123456789:X:100:0");
        assertContains (events, "FILL:123456789:FOO:0:500:500:100", "CANCELED:123456789:FOO:0:500");
    }

    @Test
    public void testPlaceBidMarketFullFillOne () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 1000, 100, callback, "X");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.BID, 1000, 0, callback, "FOO");
        assertEquals (4, events.size ());
        assertContains (events, "FILL:123456789:X:100:0:1000:100", "FILLED:123456789:X:100:0");
        assertContains (events, "FILL:123456789:FOO:0:0:1000:100", "FILLED:123456789:FOO:0:0");
    }

    @Test
    public void testPlaceAskMarketFullFillOne () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 1000, 100, callback, "X");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.ASK, 1000, 0, callback, "FOO");
        assertEquals (4, events.size ());
        assertContains (events, "FILL:123456789:X:100:0:1000:100", "FILLED:123456789:X:100:0");
        assertContains (events, "FILL:123456789:FOO:0:0:1000:100", "FILLED:123456789:FOO:0:0");
    }

    @Test
    public void testPlaceAskMarketPartialFillMany () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 100, 100, callback, "X");
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 200, 150, callback, "Y");
        orderBook.placeEntry (123456789L, OrderBookEntrySide.BID, 300, 100, callback, "Z");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.ASK, 1000, 0, callback, "FOO");
        assertEquals (10, events.size ());
        assertContains (events, "FILL:123456789:X:100:0:100:100", "FILLED:123456789:X:100:0");
        assertContains (events, "FILL:123456789:Y:150:0:200:150", "FILLED:123456789:Y:150:0");
        assertContains (events, "FILL:123456789:Z:100:0:300:100", "FILLED:123456789:Z:100:0");
        
        assertContains (events, "FILL:123456789:Y:150:0:200:150", "FILL:123456789:X:100:0:100:100", "FILL:123456789:Z:100:0:300:100");
        
        assertContains (events, "FILL:123456789:FOO:0:800:200:150", "FILL:123456789:FOO:0:700:100:100", "FILL:123456789:FOO:0:400:300:100", "CANCELED:123456789:FOO:0:400");
    }

    @Test
    public void testPlaceBidMarketPartialFillMany () throws Exception
    {
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 100, 100, callback, "X");
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 200, 50, callback, "Y");
        orderBook.placeEntry (123456789L, OrderBookEntrySide.ASK, 300, 100, callback, "Z");
        assertTrue (events.isEmpty ());
        orderBook.placeEntry (
            123456789L, OrderBookEntrySide.BID, 1000, 0, callback, "FOO");
        assertEquals (10, events.size ());
        assertContains (events, "FILL:123456789:X:100:0:100:100", "FILLED:123456789:X:100:0");
        assertContains (events, "FILL:123456789:Y:50:0:200:50", "FILLED:123456789:Y:50:0");
        assertContains (events, "FILL:123456789:Z:100:0:300:100", "FILLED:123456789:Z:100:0");
        
        assertContains (events, "FILL:123456789:Y:50:0:200:50", "FILL:123456789:X:100:0:100:100", "FILL:123456789:Z:100:0:300:100");
        
        assertContains (events, "FILL:123456789:FOO:0:800:200:50", "FILL:123456789:FOO:0:700:100:100", "FILL:123456789:FOO:0:400:300:100", "CANCELED:123456789:FOO:0:400");
    }
    
    private static void assertContains (List<Object> list, Object ... elements)
    {
        int i = 0;
        
        for (Object item: list)
        {
            if (i >= elements.length) return;
            
            if (elements [i].equals (item)) i++;
        }
        
        if (i < elements.length)
            fail ();
    }
    
    private class MyOrderBookEntryCallback implements OrderBookEntryCallback
    {
        @Override
        public void onFill (long timestamp, OrderBookEntryHandler handler,
            long quantity, long price)
        {
            events.add (
                "FILL:" + timestamp + ":" + handler.getClosure () + ":" + 
                handler.getLimitPrice () + ":" + handler.getUnfilledQuantity () + ":" + 
                quantity + ":" + price);
        }

        @Override
        public void onFilled (long timestamp, OrderBookEntryHandler handler)
        {
            events.add (
                "FILLED:" + timestamp + ":" + handler.getClosure () + ":" + 
                handler.getLimitPrice () + ":" + handler.getUnfilledQuantity ());
        }

        @Override
        public void onCanceled (long timestamp, OrderBookEntryHandler handler)
        {
            events.add (
                "CANCELED:" + timestamp + ":" + handler.getClosure () + ":" + 
                handler.getLimitPrice () + ":" + handler.getUnfilledQuantity ());
        }
    }
}
