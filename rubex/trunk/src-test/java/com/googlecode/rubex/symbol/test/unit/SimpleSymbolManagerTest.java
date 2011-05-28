package com.googlecode.rubex.symbol.test.unit;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.googlecode.rubex.exchange.Exchange;
import com.googlecode.rubex.symbol.SimpleSymbolManager;

public class SimpleSymbolManagerTest
{
    @Test
    public void testAddSymbol () throws Exception
    {
        assertEquals (0, manager.getAllSymbols ().length);
        
        manager.addSymbol ("FOO");
        
        assertEquals (1, manager.getAllSymbols ().length);
        assertEquals ("FOO", manager.getAllSymbols () [0]);
        
        manager.addSymbol ("BAR");
        assertEquals (2, manager.getAllSymbols ().length);
        assertEquals (
            new HashSet <String> (Arrays.asList ("FOO", "BAR")), 
            new HashSet <String> (Arrays.asList (manager.getAllSymbols ())));
    }
    
    @Test
    public void testRemoveSymbol () throws Exception
    {
        manager.addSymbol ("FOO");
        manager.addSymbol ("BAR");
        
        assertEquals (2, manager.getAllSymbols ().length);
        assertEquals (
            new HashSet <String> (Arrays.asList ("FOO", "BAR")), 
            new HashSet <String> (Arrays.asList (manager.getAllSymbols ())));
        
        manager.removeSymbol ("FOO");
        
        assertEquals (1, manager.getAllSymbols ().length);
        assertEquals ("BAR", manager.getAllSymbols () [0]);
        
        manager.removeSymbol ("BAR");

        assertEquals (0, manager.getAllSymbols ().length);
    }
    
    @Test
    public void testExecuteInExchangeThread () throws Exception
    {
        manager.addSymbol ("FOO");
        manager.addSymbol ("BAR");
        
        assertTrue (events.isEmpty ());
        
        MyRunnable r1 = new MyRunnable ();
        manager.executeInSymbolThread ("FOO", r1);
        
        synchronized (r1)
        {
            while (events.isEmpty ())
                r1.wait ();
        }
        
        assertEquals (2, events.size ());
        assertSame (r1, events.get (0));
        assertNotSame (Thread.currentThread (), (Thread)events.get (1));
        
        MyRunnable r2 = new MyRunnable ();
        manager.executeInSymbolThread ("BAR", r2);
        
        synchronized (r2)
        {
            while (events.size () < 4)
                r2.wait ();
        }
        
        assertEquals (4, events.size ());
        assertSame (r1, events.get (0));
        assertNotSame (Thread.currentThread (), (Thread)events.get (1));
        assertSame (r2, events.get (2));
        assertNotSame (Thread.currentThread (), (Thread)events.get (3));
        assertNotSame ((Thread)events.get (1), (Thread)events.get (3));
    }
    
    @Test
    public void testGetSymbolExchange () throws Exception
    {
        manager.addSymbol ("FOO");
        manager.addSymbol ("BAR");

        Exchange e1 = manager.getSymbolExchange ("FOO");
        assertNotNull (e1);
        
        Exchange e2 = manager.getSymbolExchange ("BAR");
        assertNotNull (e2);
        
        assertNotSame (e1, e2);
    }
    
    @Test
    public void testDestroy () throws Exception
    {
        manager.addSymbol ("FOO");
        manager.addSymbol ("BAR");

        manager.executeInSymbolThread ("FOO", new RecursiveRunnable ("FOO"));
        manager.executeInSymbolThread ("BAR", new RecursiveRunnable ("BAR"));
        
        int n = events.size ();
        
        Thread.sleep (100);
        
        assertTrue (events.size () > n);
        
        manager.destroy ();
        
        n = events.size ();
        
        Thread.sleep (100);
        
        assertEquals (n, events.size ());
    }
    
    private SimpleSymbolManager manager;
    private List <Object> events;
    
    @Before
    public void setUp () throws Exception
    {
        manager = new SimpleSymbolManager ();
        events = Collections.synchronizedList (new ArrayList <Object> ());
    }

    @After
    public void tearDown () throws Exception
    {
        if (!manager.isDestroyed ())
            manager.destroy ();
        manager = null;
        events = null;
    }
    
    private class MyRunnable implements Runnable 
    {
        @Override
        public void run ()
        {
            synchronized (this)
            {
                events.add (this);
                events.add (Thread.currentThread ());
                notifyAll ();
            }
        }
    }
    
    private class RecursiveRunnable implements Runnable
    {
        private final String symbol;
        
        public RecursiveRunnable (String symbol)
        {
            if (symbol == null)
                throw new IllegalArgumentException ("Symbol is null");
            
            this.symbol = symbol;
        }
        
        public void run ()
        {
            events.add (this);
            events.add (Thread.currentThread ());

            try
            {
                Thread.sleep (1);
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread ().interrupt ();
            }
            
            manager.executeInSymbolThread (symbol, this);
        }
    }
}
