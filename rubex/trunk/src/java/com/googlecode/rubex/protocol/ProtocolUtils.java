package com.googlecode.rubex.protocol;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.data.StructureDataObjectBuilder;
import com.googlecode.rubex.data.StructureField;
import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;

/**
 * Contains various method useful for work with business-level protocol 
 * messages.
 * 
 * @author Mikhail Vladimirov
 */
public class ProtocolUtils
{
    private final static MarshallingVisitor MARSHALLING_VISITOR =
        new MarshallingVisitor ();
    
    private final static ValidatingVisitor VALIDATING_VISITOR =
        new ValidatingVisitor ();
    
    private ProtocolUtils ()
    {
        throw new Error ("Do not instantiate me");
    }
    
    /**
     * Marshal business-level protocol message into data object.
     * 
     * @param message business-level protocol message to marshal
     * @return data object
     */
    public static DataObject marshal (ProtocolMessage message)
    {
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        return message.accept (MARSHALLING_VISITOR);
    }
    
    public static ProtocolMessage unmarshal (
        ProtocolMessageType messageType, DataObject dataObject)
    {
        if (messageType == null)
            throw new IllegalArgumentException ("Message type is null");
        
        if (dataObject == null)
            throw new IllegalArgumentException ("Data object is null");
        
        ProtocolMessage result;
        
        switch (messageType)
        {
        case REJECT:
            result = new MyReject ();
            break;
        case NEW_ORDER:
            result = new MyNewOrder ();
            break;
        case REPLACE_ORDER:
            result = new MyReplaceOrder ();
            break;
        case CANCEL_ORDER:
            result = new MyCancelOrder ();
            break;
        default:
            throw new IllegalArgumentException ("Unknown message type: " + messageType);
        }
        
        DataObjectUtils.mapFields (dataObject, result);
        
        return result.accept (VALIDATING_VISITOR);
    }
    
    private static class MarshallingVisitor 
        implements ProtocolMessageVisitor <DataObject>
    {
        @Override
        public DataObject visitReject (RejectProtocolMessage reject)
        {
            if (reject == null)
                throw new IllegalArgumentException ("Reject is null");
            
            return new StructureDataObjectBuilder ().
                addStringField (
                    RejectProtocolMessage.REJECT_REASON, 
                    reject.getRejectReason ()).
                addField (
                    RejectProtocolMessage.REJECTED_MESSAGE, 
                    reject.getRejectedMessage ()).
                getStructureDataObject ();
        }

        @Override
        public DataObject visitNewOrder (NewOrderProtocolMessage newOrder)
        {
            if (newOrder == null)
                throw new IllegalArgumentException ("New order is null");
            
            StructureDataObjectBuilder builder = 
                new StructureDataObjectBuilder ();
            
            builder.addIntegerField (
                NewOrderProtocolMessage.ORDER_ID, 
                newOrder.getOrderID ());
            
            long account = newOrder.getAccount ();
            if (account != 0)
                builder.addIntegerField (
                    NewOrderProtocolMessage.ACCOUNT, account);
            
            builder.addStringField (
                NewOrderProtocolMessage.SYMBOL, newOrder.getSymbol ());
            builder.addStringField (
                NewOrderProtocolMessage.SIDE, newOrder.getSide ().name ());
            builder.addIntegerField (
                NewOrderProtocolMessage.QUANTITY, newOrder.getQuantity ());
            builder.addStringField (
                NewOrderProtocolMessage.ORDER_TYPE, 
                newOrder.getOrderType ().name ());
            
            OrderTimeInForce timeInforce = newOrder.getTimeInForce ();
            if (timeInforce != null)
                builder.addStringField (
                    NewOrderProtocolMessage.TIME_IN_FORCE, timeInforce.name ());
            
            long limitPrice = newOrder.getLimitPrice ();
            if (limitPrice != 0)
                builder.addIntegerField (
                    NewOrderProtocolMessage.lIMIT_PRICE, limitPrice);
            
            long stopPrice = newOrder.getStopPrice ();
            if (stopPrice != 0)
                builder.addIntegerField (
                    NewOrderProtocolMessage.STOP_PRICE, stopPrice);
            
            long visibleQuantity = newOrder.getVisibleQuantity ();
            if (visibleQuantity != 0)
                builder.addIntegerField (
                    NewOrderProtocolMessage.VISIBLE_QUANTITY, visibleQuantity);
            
            return builder.getStructureDataObject ();
        }

        @Override
        public DataObject visitReplaceOrder (
            ReplaceOrderProtocolMessage replaceOrder)
        {
            if (replaceOrder == null)
                throw new IllegalArgumentException ("Replace order is null");
            
            StructureDataObjectBuilder builder = 
                new StructureDataObjectBuilder ();
            
            builder.addIntegerField (
                ReplaceOrderProtocolMessage.ORIGINAL_ORDER_ID, 
                replaceOrder.getOriginalOrderID ());
            
            builder.addIntegerField (
                ReplaceOrderProtocolMessage.ORDER_ID, 
                replaceOrder.getOrderID ());
            
            builder.addIntegerField (
                ReplaceOrderProtocolMessage.QUANTITY, 
                replaceOrder.getQuantity ());
            builder.addStringField (
                ReplaceOrderProtocolMessage.ORDER_TYPE, 
                replaceOrder.getOrderType ().name ());
            
            OrderTimeInForce timeInforce = replaceOrder.getTimeInForce ();
            if (timeInforce != null)
                builder.addStringField (
                    ReplaceOrderProtocolMessage.TIME_IN_FORCE, 
                    timeInforce.name ());
            
            long limitPrice = replaceOrder.getLimitPrice ();
            if (limitPrice != 0)
                builder.addIntegerField (
                    ReplaceOrderProtocolMessage.lIMIT_PRICE, 
                    limitPrice);
            
            long stopPrice = replaceOrder.getStopPrice ();
            if (stopPrice != 0)
                builder.addIntegerField (
                    ReplaceOrderProtocolMessage.STOP_PRICE, 
                    stopPrice);
            
            long visibleQuantity = replaceOrder.getVisibleQuantity ();
            if (visibleQuantity != 0)
                builder.addIntegerField (
                    ReplaceOrderProtocolMessage.VISIBLE_QUANTITY, 
                    visibleQuantity);
            
            return builder.getStructureDataObject ();
        }

        @Override
        public DataObject visitCancelOrder (
            CancelOrderProtocolMessage cancelOrder)
        {
            if (cancelOrder == null)
                throw new IllegalArgumentException ("Cancel order is null");
            
            StructureDataObjectBuilder builder = 
                new StructureDataObjectBuilder ();
            
            builder.addIntegerField (
                CancelOrderProtocolMessage.ORDER_ID, 
                cancelOrder.getOrderID ());
            
            return builder.getStructureDataObject ();
        }
    }
    
    private static class ValidatingVisitor
        implements ProtocolMessageVisitor <ProtocolMessage>
    {

        @Override
        public ProtocolMessage visitReject (RejectProtocolMessage reject)
        {
            return reject;
        }

        @Override
        public ProtocolMessage visitNewOrder (NewOrderProtocolMessage newOrder)
        {
            OrderType type = newOrder.getOrderType ();
            
            switch (type)
            {
            case MARKET:
                if (newOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for market order");
                
                if (newOrder.getLimitPrice () != 0)
                    throw new IllegalArgumentException (
                        "Limit price is set for market order");
                
                if (newOrder.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for market order");
                
                if (newOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for market order");
                
                break;
            case LIMIT:
                if (newOrder.getTimeInForce () == null)
                    throw new IllegalArgumentException (
                        "Time in force is not set for limit order");
                
                if (newOrder.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for limit order");
                
                if (newOrder.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for limit order");
                
                if (newOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for limit order");
                
                break;
            case STOP:
                if (newOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for stop order");
                
                if (newOrder.getLimitPrice () != 0)
                    throw new IllegalArgumentException (
                        "Limit price is set for stop order");
                
                if (newOrder.getStopPrice () == 0)
                    throw new IllegalArgumentException (
                        "Stop price is not set for stop order");
                
                if (newOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for stop order");
                
                break;
            case STOP_LIMIT:
                if (newOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for stop limit order");
                
                if (newOrder.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for stop limit order");
                
                if (newOrder.getStopPrice () == 0)
                    throw new IllegalArgumentException (
                        "Stop price is not set for stop limit order");
                
                if (newOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for stop limit order");
                
                break;
            case ICEBERG:
                if (newOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for iceberg order");
                
                if (newOrder.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for iceberg order");
                
                if (newOrder.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for iceberg order");
                
                if (newOrder.getVisibleQuantity () == 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is not set for iceberg order");
                
                break;
            default:
                throw new IllegalArgumentException (
                    "Unknown order type: " + type);
            }
            
            return newOrder;
        }

        @Override
        public ProtocolMessage visitReplaceOrder (
            ReplaceOrderProtocolMessage replaceOrder)
        {
            OrderType type = replaceOrder.getOrderType ();
            
            switch (type)
            {
            case MARKET:
                if (replaceOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for market order");
                
                if (replaceOrder.getLimitPrice () != 0)
                    throw new IllegalArgumentException (
                        "Limit price is set for market order");
                
                if (replaceOrder.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for market order");
                
                if (replaceOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for market order");
                
                break;
            case LIMIT:
                if (replaceOrder.getTimeInForce () == null)
                    throw new IllegalArgumentException (
                        "Time in force is not set for limit order");
                
                if (replaceOrder.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for limit order");
                
                if (replaceOrder.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for limit order");
                
                if (replaceOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for limit order");
                
                break;
            case STOP:
                if (replaceOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for stop order");
                
                if (replaceOrder.getLimitPrice () != 0)
                    throw new IllegalArgumentException (
                        "Limit price is set for stop order");
                
                if (replaceOrder.getStopPrice () == 0)
                    throw new IllegalArgumentException (
                        "Stop price is not set for stop order");
                
                if (replaceOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for stop order");
                
                break;
            case STOP_LIMIT:
                if (replaceOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for stop limit order");
                
                if (replaceOrder.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for stop limit order");
                
                if (replaceOrder.getStopPrice () == 0)
                    throw new IllegalArgumentException (
                        "Stop price is not set for stop limit order");
                
                if (replaceOrder.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for stop limit order");
                
                break;
            case ICEBERG:
                if (replaceOrder.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for iceberg order");
                
                if (replaceOrder.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for iceberg order");
                
                if (replaceOrder.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for iceberg order");
                
                if (replaceOrder.getVisibleQuantity () == 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is not set for iceberg order");
                
                break;
            default:
                throw new IllegalArgumentException (
                    "Unknown order type: " + type);
            }
            
            return replaceOrder;
        }

        @Override
        public ProtocolMessage visitCancelOrder (CancelOrderProtocolMessage cancelOrder)
        {
            return cancelOrder;
        }
    }
    
    @SuppressWarnings ("unused")
    private static class MyReject extends AbstractRejectProtocolMessage
    {
        private String rejectReason;
        private DataObject rejectedMessage;
        
        @StructureField (name = REJECT_REASON)
        public void setRejectReason (String rejectReason)
        {
            if (rejectReason == null)
                throw new IllegalArgumentException ("Reject reason is null");
            
            this.rejectReason = rejectReason;
        }

        @StructureField (name = REJECTED_MESSAGE)
        public void setRejectedMessage (DataObject rejectedMessage)
        {
            if (rejectedMessage == null)
                throw new IllegalArgumentException ("Rejected message is null");
            
            this.rejectedMessage = rejectedMessage;
        }
        
        @Override
        public String getRejectReason ()
        {
            return rejectReason;
        }

        @Override
        public DataObject getRejectedMessage ()
        {
            return rejectedMessage;
        }
    }
    
    @SuppressWarnings ("unused")
    private static class MyNewOrder 
        extends AbstractNewOrderProtocolMessage
    {
        private long orderID;
        private long account;
        private String symbol;
        private OrderSide side;
        private long quantity;
        private OrderType orderType;
        private OrderTimeInForce timeInForce;
        private long limitPrice;
        private long stopPrice;
        private long visibleQuantity;

        @StructureField (name = ORDER_ID)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = ACCOUNT)
        public void setOrderID (long account)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = SYMBOL)
        public void setOrderID (String symbol)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = SIDE)
        public void setOrderID (String side)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = QUANTITY)
        public void setOrderID (long quantity)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = ORDER_TYPE)
        public void setOrderID (String orderType)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = TIME_IN_FORCE)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = lIMIT_PRICE)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = STOP_PRICE)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = VISIBLE_QUANTITY)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }
        
        @Override
        public long getOrderID ()
        {
            return orderID;
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
    }
}
