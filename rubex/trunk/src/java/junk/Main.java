package junk;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.marketdata.MarketDataTracker;
import com.googlecode.rubex.marketdata.SimpleOrderBookMarketDataTracker;
import com.googlecode.rubex.orderbook.OrderBookEntryCallback;
import com.googlecode.rubex.orderbook.OrderBookEntryHandler;
import com.googlecode.rubex.orderbook.OrderBookEntrySide;
import com.googlecode.rubex.orderbook.SimpleOrderBook;

public class Main
{
    private final static List<OrderBookEntryHandler> handlers = new ArrayList<OrderBookEntryHandler> ();
    
    public static void main (String[] args) throws Exception
    {
        SimpleOrderBook orderBook = new SimpleOrderBook ();
        
        orderBook.addOrderBookListener (new OrderBookWindow ());
        SimpleOrderBookMarketDataTracker tracker = new SimpleOrderBookMarketDataTracker ();
        orderBook.addOrderBookListener (tracker);
        
        OrderBookEntryCallback callback = new MyOrderBookEntryCallback ();
        
        BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
        while (true)
        {
            dumpTracker (tracker);
            
            String line = reader.readLine ();
            if (line == null) break;
            
            String [] parts = line.trim ().split ("\\s+", -1);

            String command = parts [0];
            
            if ("CANCEL".equalsIgnoreCase (command))
            {
                if (parts.length != 2)
                {
                    System.out.println ("Syntax error");
                    continue;
                }
                
                int orderID;
                try
                {
                    orderID = Integer.parseInt (parts [1]);
                }
                catch (NumberFormatException ex)
                {
                    System.out.print ("Invalid numeric format: " + parts [1]);
                    continue;
                }
                
                if (orderID < 0 || orderID >= handlers.size ())
                {
                    System.out.print ("No order with such ID: " + orderID);
                    continue;
                }
                
                try
                {
                    handlers.get (orderID).cancel (System.currentTimeMillis ());
                }
                catch (IllegalStateException ex)
                {
                    ex.printStackTrace (System.out);
                }
            }
            else if ("BUY".equalsIgnoreCase (command) || "SELL".equalsIgnoreCase (command))
            {
                if (parts.length != 2)
                {
                    System.out.println ("Syntax error");
                    continue;
                }
                
                OrderBookEntrySide side;
                if ("BUY".equalsIgnoreCase (parts [0]))
                    side = OrderBookEntrySide.BID;
                else if ("SELL".equalsIgnoreCase (parts [0]))
                    side = OrderBookEntrySide.ASK;
                else
                {
                    System.out.println ("Unknown order side: " + parts [0]);
                    continue;
                }
                
                parts = parts [1].split ("@", 2);
                
                long quantity;
                try
                {
                    quantity = Long.parseLong (parts [0]);
                }
                catch (NumberFormatException ex)
                {
                    System.out.print ("Invalid numeric format: " + parts [0]);
                    continue;
                }
                
                if (quantity <= 0)
                {
                    System.out.print ("Non-positive quantity");
                    continue;
                }
                
                long limitPrice;
                try
                {
                    limitPrice = Long.parseLong (parts [1]);
                }
                catch (NumberFormatException ex)
                {
                    System.out.print ("Invalid numeric format: " + parts [1]);
                    continue;
                }
                
                if (limitPrice < 0)
                {
                    System.out.print ("Negative price");
                    continue;
                }
                
                System.out.println ("Order #" + handlers.size () + " accepted");
                
                OrderBookEntryHandler handler = orderBook.placeEntry (System.currentTimeMillis (), side, quantity, limitPrice, callback, Integer.toString (handlers.size ()));
                handlers.add (handler);
            }
            else
            {
                System.out.println ("Syntax error");
                continue;
            }
        }
    }
    
    private static class MyOrderBookEntryCallback implements OrderBookEntryCallback
    {
        @Override
        public void onFill (long timestamp, OrderBookEntryHandler handler,
            long quantity, long price)
        {
            System.out.println ("[Order #" + handler.getClosure () + "] FILL: " + quantity + "@" + price);
        }

        @Override
        public void onFilled (long timestamp, OrderBookEntryHandler handler)
        {
            System.out.println ("[Order #" + handler.getClosure () + "] FILLED");
        }

        @Override
        public void onCanceled (long timestamp, OrderBookEntryHandler handler)
        {
            System.out.println ("[Order #" + handler.getClosure () + "] CANCELED");
        }
    }
    
    private static void dumpTracker (MarketDataTracker tracker)
    {
        System.out.println ("{");
        System.out.println ("    lastTradePrice: " + tracker.getLastTradePrice ());
        System.out.println ("    lastTradeQuantity: " + tracker.getLastTradeQuantity ());
        System.out.println ("    lastTradeTime: " + tracker.getLastTradeTimestamp ());
        System.out.println ("    totalVolume: " + tracker.getTotalVolume ());
        System.out.println ("    totalValue: " + tracker.getTotalValue ());
        System.out.println ("    bestBidPrice: " + tracker.getBestBidPrice ());
        System.out.println ("    bestBidQuantity: " + tracker.getBestBidQuantity ());
        System.out.println ("    bestAskPrice: " + tracker.getBestAskPrice ());
        System.out.println ("    bestAskQuantity: " + tracker.getBestAskQuantity ());
        System.out.println ("}");
    }
}
