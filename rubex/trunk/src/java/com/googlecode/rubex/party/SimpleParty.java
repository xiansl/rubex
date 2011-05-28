package com.googlecode.rubex.party;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.rubex.exchange.Order;
import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;
import com.googlecode.rubex.protocol.CancelOrderProtocolMessage;
import com.googlecode.rubex.protocol.NewOrderProtocolMessage;
import com.googlecode.rubex.protocol.ReplaceOrderProtocolMessage;

public class SimpleParty implements Party
{
    private final Map <Long, MyPartyOrder> orders =
        new HashMap <Long, MyPartyOrder> ();
    
    @Override
    public synchronized void processNewOrderMessage (NewOrderProtocolMessage newOrder)
    {
        if (newOrder == null)
            throw new IllegalArgumentException ("New order is null");
        
        Long orderID = Long.valueOf (newOrder.getOrderID ());
        
        if (orders.containsKey (orderID))
            throw new IllegalArgumentException (
                "Duplicate order ID: " + orderID);
        
        MyPartyOrder partyOrder = new MyPartyOrder (
            newOrder.getOrderID (),
            PartyOrderState.NEW,
            newOrder.getAccount (),
            newOrder.getSymbol (),
            newOrder.getSide (),
            newOrder.getQuantity (),
            0, // Filled quantity
            0, // Filled value
            newOrder.getOrderType (),
            newOrder.getTimeInForce (),
            newOrder.getLimitPrice (),
            newOrder.getStopPrice (),
            newOrder.getVisibleQuantity (),
            null // Order
        );
        
        orders.put (orderID, partyOrder);
    }

    @Override
    public synchronized void processReplaceOrderMessage (
        ReplaceOrderProtocolMessage replaceOrder)
    {
        if (replaceOrder == null)
            throw new IllegalArgumentException ("Replace order is null");
        
        Long originalOrderID = Long.valueOf (replaceOrder.getOriginalOrderID ());
        if (!orders.containsKey (originalOrderID))
            throw new IllegalArgumentException (
                "No order with such ID: " + originalOrderID);
        
        Long orderID = Long.valueOf (replaceOrder.getOrderID ());
        if (orders.containsKey (orderID))
            throw new IllegalArgumentException (
                "Duplicate order ID: " + orderID);
        
        PartyOrder originalOrder = orders.get (originalOrderID);
        
        MyPartyOrder partyOrder = new MyPartyOrder (
            replaceOrder.getOrderID (),
            PartyOrderState.NEW,
            originalOrder.getAccount (),
            originalOrder.getSymbol (),
            originalOrder.getSide (),
            replaceOrder.getQuantity (),
            0, // Filled quantity
            0, // Filled value
            replaceOrder.getOrderType (),
            replaceOrder.getTimeInForce (),
            replaceOrder.getLimitPrice (),
            replaceOrder.getStopPrice (),
            replaceOrder.getVisibleQuantity (),
            null // Order
        );
        
        orders.put (orderID, partyOrder);
    }

    @Override
    public synchronized void processCancelOrderMessage (
        CancelOrderProtocolMessage cancelOrder)
    {
        if (cancelOrder == null)
            throw new IllegalArgumentException ("Cancel order is null");
        
        Long orderID = Long.valueOf (cancelOrder.getOrderID ());
        if (!orders.containsKey (orderID))
            throw new IllegalArgumentException (
                "No order with such ID: " + orderID);
        
        
    }

    private class MyPartyOrder implements PartyOrder
    {
        private final long orderID;
        private final PartyOrderState orderState;
        private final long account;
        private final String symbol;
        private final OrderSide side;
        private final long quantity;
        private final long filledQuantity;
        private final long filledValue;
        private final OrderType orderType;
        private final OrderTimeInForce timeInForce;
        private final long limitPrice;
        private final long stopPrice;
        private final long visibleQuantity;
        private final Order order;
        
        public MyPartyOrder (long orderID, PartyOrderState orderState,
            long account, String symbol, OrderSide side, long quantity,
            long filledQuantity, long filledValue, OrderType orderType,
            OrderTimeInForce timeInForce, long limitPrice, long stopPrice,
            long visibleQuantity, Order order)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            if (orderState == null)
                throw new IllegalArgumentException ("Order state is null");
            
            if (account < 0)
                throw new IllegalArgumentException ("Account < 0");
            
            if (symbol == null)
                throw new IllegalArgumentException ("Symbol is null");
            
            if (side == null)
                throw new IllegalArgumentException ("Side is null");
            
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            if (filledQuantity < 0)
                throw new IllegalArgumentException ("Filled quantity < 0");
            
            if (filledValue < 0)
                throw new IllegalArgumentException ("Filled value < 0");
            
            if (orderType == null)
                throw new IllegalArgumentException ("Order type is null");
            
            if (limitPrice < 0)
                throw new IllegalArgumentException ("Limit price < 0");
            
            if (stopPrice < 0)
                throw new IllegalArgumentException ("Stop price < 0");
            
            if (visibleQuantity < 0)
                throw new IllegalArgumentException ("Visible quantity < 0");
            
            this.orderID = orderID;
            this.orderState = orderState;
            this.account = account;
            this.symbol = symbol;
            this.side = side;
            this.quantity = quantity;
            this.filledQuantity = filledQuantity;
            this.filledValue = filledValue;
            this.orderType = orderType;
            this.timeInForce = timeInForce;
            this.limitPrice = limitPrice;
            this.stopPrice = stopPrice;
            this.visibleQuantity = visibleQuantity;
            this.order = order;
        }

        @Override
        public long getOrderID ()
        {
            return orderID;
        }

        @Override
        public PartyOrderState getOrderState ()
        {
            return orderState;
        }

        @Override
        public long getAccount ()
        {
            return account;
        }

        @Override
        public String getSymbol ()
        {
            return symbol;
        }

        @Override
        public OrderSide getSide ()
        {
            return side;
        }

        @Override
        public long getQuantity ()
        {
            return quantity;
        }

        @Override
        public long getFilledQuantity ()
        {
            return filledQuantity;
        }

        @Override
        public long getFilledValue ()
        {
            return filledValue;
        }

        @Override
        public OrderType getOrderType ()
        {
            return orderType;
        }

        @Override
        public OrderTimeInForce getTimeInForce ()
        {
            return timeInForce;
        }

        @Override
        public long getLimitPrice ()
        {
            return limitPrice;
        }

        @Override
        public long getStopPrice ()
        {
            return stopPrice;
        }

        @Override
        public long getVisibleQuantity ()
        {
            return visibleQuantity;
        }

        public Order getOrder ()
        {
            return order;
        }
    }
}
