package com.googlecode.rubex.symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.exchange.Exchange;
import com.googlecode.rubex.exchange.SimpleExchange;

/**
 * Simple implementation of {@link SymbolManager} interface that assigns
 * separate exchange thread to each symbol.
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleSymbolManager implements SymbolManager
{
    private final static Logger logger =
        Logger.getLogger (SimpleSymbolManager.class.getName ());
    
    private final Map <String, SymbolInfo> symbols =
        new HashMap <String, SymbolInfo> ();
    
    private boolean destroyed = false;
    
    /**
     * Add given symbol to the symbol manager.
     * 
     * @param symbol symbol to be added
     */
    public synchronized void addSymbol (String symbol)
    {
        if (destroyed)
            throw new IllegalStateException (
                "Symbol manager already destroyed");
        
        if (symbol == null)
            throw new IllegalArgumentException ("Symbol is null");
        
        if (symbols.containsKey (symbol))
            throw new IllegalStateException (
                "Symbol already exists: " + symbol);
        
        symbols.put (symbol, new SymbolInfo (symbol));
    }
    
    /**
     * Remove given symbol and stop corresponding eschange thread.
     * 
     * @param symbol symbol to be removed
     */
    public synchronized void removeSymbol (String symbol)
    {
        if (destroyed)
            throw new IllegalStateException (
                "Symbol manager already destroyed");
        
        if (symbol == null)
            throw new IllegalArgumentException ("Symbol is null");
        
        SymbolInfo info = symbols.remove (symbol);
        if (info == null)
            throw new IllegalArgumentException ("No such symbol: " + symbol);
        
        info.stop ();
    }
    
    /**
     * Tells whether this symbol manager is destroyed.
     * 
     * @return <code>true</code> if this symbol manager is destroyed, 
     *         <code>false</code> otherwise.
     */
    public synchronized boolean isDestroyed ()
    {
        return destroyed;
    }
    
    /**
     * Destroy symbol manager and stop all exchange threads.
     */
    public void destroy ()
    {
        SymbolInfo [] infos;
        
        synchronized (this)
        {
            if (destroyed)
                throw new IllegalStateException (
                    "Symbol manager already destroyed");
            
            destroyed = true;
            
            infos = symbols.values ().toArray (new SymbolInfo [symbols.size ()]);
        }
            
        for (SymbolInfo info: infos)
        {
            info.stop ();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String[] getAllSymbols ()
    {
        return symbols.keySet ().toArray (new String [symbols.size ()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Exchange getSymbolExchange (String symbol)
        throws IllegalArgumentException
    {
        if (symbol == null)
            throw new IllegalArgumentException ("Symbol is null");
        
        SymbolInfo info = symbols.get (symbol);
        if (info == null)
            throw new IllegalArgumentException ("No such symbol");
        
        return info.getExchange ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void executeInSymbolThread (
        String symbol, Runnable runnable)
        throws IllegalArgumentException
    {
        if (symbol == null)
            throw new IllegalArgumentException ("Symbol is null");
        
        SymbolInfo info = symbols.get (symbol);
        if (info == null)
            throw new IllegalArgumentException ("No such symbol");

        if (runnable == null)
            throw new IllegalArgumentException ("Runnable is null");
        
        info.executeInSymbolThread (runnable);
    }

    private static class SymbolInfo
    {
        private final Exchange exchange = new SimpleExchange ();
        private final BlockingQueue <Runnable> queue =
            new LinkedBlockingQueue <Runnable> ();
        private final Thread exchangeThread;
        
        public SymbolInfo (String symbol)
        {
            exchangeThread = new ExchangeThread ("Exchange: " + symbol, queue);
            exchangeThread.start ();
        }
        
        public Exchange getExchange ()
        {
            return exchange;
        }
        
        public void executeInSymbolThread (Runnable runnable)
        {
            if (runnable == null)
                throw new IllegalArgumentException ("Runnable is null");
            
            try
            {
                queue.put (runnable);
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread ().interrupt ();
                
                if (logger.isLoggable (Level.SEVERE))
                    logger.log (
                        Level.SEVERE, 
                        "Interrupted while adding runnable to the queue", ex);
            }
        }
        
        public void stop ()
        {
            exchangeThread.interrupt ();
            
            try
            {
                exchangeThread.join ();
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread ().interrupt ();
            }
        }
    }
    
    private static class ExchangeThread extends Thread
    {
        private final BlockingQueue <Runnable> queue;
        
        public ExchangeThread (String name, BlockingQueue <Runnable> queue)
        {
            super (name);
            
            if (queue == null)
                throw new IllegalArgumentException ("Queue is null");
            
            this.queue = queue;
        }
        
        @Override
        public void run ()
        {
            while (true)
            {
                Runnable runnable;
                
                try
                {
                    runnable = queue.take ();
                }
                catch (InterruptedException ex)
                {
                    if (logger.isLoggable (Level.INFO))
                        logger.info (
                            "Exchange thread interrupted: " + getName ());
                    
                    break;
                }
                
                try
                {
                    runnable.run ();
                }
                catch (Throwable ex)
                {
                    if (logger.isLoggable (Level.SEVERE))
                        logger.log (
                            Level.SEVERE, 
                            "Exception while executing runnable", ex);
                }
            }
        }        
    }
}
