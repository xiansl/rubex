package com.googlecode.rubex.exchange.text.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.googlecode.rubex.exchange.AbstractOrder;
import com.googlecode.rubex.exchange.Order;
import com.googlecode.rubex.exchange.OrderCallback;
import com.googlecode.rubex.exchange.OrderException;
import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderType;
import com.googlecode.rubex.exchange.OrderVisitor;

public class AbstractOrderTest
{
    @Test
    public void testGetters () throws Exception
    {
        assertEquals (OrderSide.SELL, order.getSide ());
        assertEquals (1234L, order.getOrderedQuantity ());
        assertEquals ("foo", order.getClosure ());
    }
    
    @Test
    public void testFill () throws Exception
    {
        assertEquals (0L, order.getFilledQuantity ());
        assertEquals (0L, order.getFilledValue ());
        
        order.fill (123L, 234L);
        
        assertEquals (123L, order.getFilledQuantity ());
        assertEquals (123L * 234L, order.getFilledValue ());
        
        order.fill (345L, 456L);
        
        assertEquals (123L + 345L, order.getFilledQuantity ());
        assertEquals (123L * 234L + 345L * 456L, order.getFilledValue ());
    }
    
    public void testFireOnFill () throws Exception
    {
        assertTrue (events.isEmpty ());
        
        order.fireOnFill (1234L, 123L, 456L);
        
        assertEquals (2, events.size ());
        assertEquals ("FILL:1234:123:456", events.get (0));
        assertSame (order, events.get (1));
    }
    
    public void testFireOnFilled () throws Exception
    {
        assertTrue (events.isEmpty ());
        
        order.fireOnFilled (1234L);
        
        assertEquals (2, events.size ());
        assertEquals ("FILLED:1234", events.get (0));
        assertSame (order, events.get (1));
    }
    
    public void testFireOnReplaced () throws Exception
    {
        assertTrue (events.isEmpty ());
        
        MyOrder newOrder = new MyOrder (
            OrderSide.BUY, 9876L, new MyOrderCallback (), "bar");
        order.fireOnReplaced (1234L, newOrder);
        
        assertEquals (2, events.size ());
        assertEquals ("REPLACED:1234", events.get (0));
        assertSame (order, events.get (1));
        assertSame (newOrder, events.get (2));
    }
    
    private MyOrder order;
    private List <Object> events;

    @Before
    public void setUp () throws Exception
    {
        order = new MyOrder (
            OrderSide.SELL, 1234L, new MyOrderCallback (), "foo");
        events = new ArrayList <Object> ();
    }

    @After
    public void tearDown () throws Exception
    {
        order = null;
        events = null;
    }
    
    private class MyOrderCallback implements OrderCallback
    {
        @Override
        public void onFill (long timestamp, Order order, long quantity,
            long price)
        {
            events.add ("FILL:" + timestamp + ":" + quantity + ":" + price);
            events.add (order);
        }

        @Override
        public void onFilled (long timestamp, Order order)
        {
            events.add ("FILLED:" + timestamp);
            events.add (order);
        }

        @Override
        public void onCanceled (long timestamp, Order order)
        {
            events.add ("CANCELED:" + timestamp);
            events.add (order);
        }

        @Override
        public void onReplaced (long timestamp, Order order, Order newOrder)
        {
            events.add ("REPLACED:" + timestamp);
            events.add (order);
            events.add (newOrder);
        }
    }

    private static class MyOrder extends AbstractOrder
    {
        public MyOrder (OrderSide side, long orderedQuantity,
                OrderCallback callback, Object closure)
        {
            super (side, orderedQuantity, callback, closure);
        }

        @Override
        public OrderType getType ()
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public <T> T accept (OrderVisitor <T> visitor)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void cancel (long timestamp) throws OrderException
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void fill (long quantity, long price)
        {
            super.fill (quantity, price);
        }

        @Override
        public void fireOnFill (long timestamp, long quantity, long price)
        {
            super.fireOnFill (timestamp, quantity, price);
        }

        @Override
        public void fireOnFilled (long timestamp)
        {
            super.fireOnFilled (timestamp);
        }

        @Override
        public void fireOnCanceled (long timestamp)
        {
            super.fireOnCanceled (timestamp);
        }

        @Override
        public void fireOnReplaced (long timestamp, Order newOrder)
        {
            super.fireOnReplaced (timestamp, newOrder);
        }
    }
}
