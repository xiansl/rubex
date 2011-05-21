package com.googlecode.rubex.exchange.text.unit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import com.googlecode.rubex.exchange.AbstractIcebergOrder;
import com.googlecode.rubex.exchange.IcebergOrder;
import com.googlecode.rubex.exchange.LimitOrder;
import com.googlecode.rubex.exchange.MarketOrder;
import com.googlecode.rubex.exchange.Order;
import com.googlecode.rubex.exchange.OrderCallback;
import com.googlecode.rubex.exchange.OrderException;
import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;
import com.googlecode.rubex.exchange.OrderVisitor;
import com.googlecode.rubex.exchange.StopLimitOrder;
import com.googlecode.rubex.exchange.StopOrder;

public class AbstractIcebergOrderTest
{
    @Test
    public void testGetType () throws Exception
    {
        assertEquals (OrderType.ICEBERG, order.getType ());
    }
    
    @Test
    public void testGetters () throws Exception
    {
        assertEquals (234L, order.getLimitPrice ());
        assertEquals (345L, order.getVisibleQuantity ());
    }
    
    @Test
    public void testAccept () throws Exception
    {
        assertEquals ("foo:" + order, order.accept (new MyOrderVisitor ()));
    }
    
    private AbstractIcebergOrder order;

    @Before
    public void setUp () throws Exception
    {
        order = new MyIcebergOrder (
            OrderSide.SELL, 1234L, 234L, 345L, new MyOrderCallback (), "foo");
    }

    @After
    public void tearDown () throws Exception
    {
        order = null;
    }
    
    private class MyOrderCallback implements OrderCallback
    {
        @Override
        public void onFill (long timestamp, Order order, long quantity,
            long price)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void onFilled (long timestamp, Order order)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void onCanceled (long timestamp, Order order)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void onReplaced (long timestamp, Order order, Order newOrder)
        {
            throw new UnsupportedOperationException ();
        }
    }

    private class MyOrderVisitor implements OrderVisitor <String>
    {
        @Override
        public String visitMarketOrder (MarketOrder marketOrder)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public String visitLimitOrder (LimitOrder limitOrder)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public String visitStopOrder (StopOrder stopOrder)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public String visitStopLimitOrder (StopLimitOrder stopLimitOrder)
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public String visitIcebergOrder (IcebergOrder icebergOrder)
        {
            return "foo:" + icebergOrder;
        }
    }
    
    private static class MyIcebergOrder extends AbstractIcebergOrder
    {
        public MyIcebergOrder (OrderSide side, long orderedQuantity,
            long limitPrice, long visibleQuantity,
            OrderCallback callback, Object closure)
        {
            super (
                side, orderedQuantity, limitPrice, 
                visibleQuantity, callback, closure);
        }

        @Override
        public MarketOrder replaceWithMarketOrder (long timestamp,
            long newQuantity, OrderCallback callback, Object closure)
            throws OrderException
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public LimitOrder replaceWithLimitOrder (long timestamp,
            long newQuantity, long newLimitPrice,
            OrderTimeInForce newTimeInForce, OrderCallback callback,
            Object closure) throws OrderException
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public IcebergOrder replaceWithIcebergOrder (long timestamp,
            long newQuantity, long newLimitPrice, long newVisibleQuantity,
            OrderCallback callback, Object closure) throws OrderException
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void cancel (long timestamp) throws OrderException
        {
            throw new UnsupportedOperationException ();
        }
    }
}
