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
    
    /**
     * Unmarshal business-level protocol message from data object.
     * 
     * @param dataObject data object to unmarshal business-level protocol 
     *        message from
     * @return unmarshalled business-level protocol object
     */
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
        private long account = 0;
        private String symbol;
        private OrderSide side;
        private long quantity;
        private OrderType orderType;
        private OrderTimeInForce timeInForce = null;
        private long limitPrice = 0;
        private long stopPrice = 0;
        private long visibleQuantity = 0;

        @StructureField (name = ORDER_ID)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = ACCOUNT, optional = true)
        public void setAccount (long account)
        {
            if (account <= 0)
                throw new IllegalArgumentException ("Account <= 0");
            
            this.account = account;
        }

        @StructureField (name = SYMBOL)
        public void setSymbol (String symbol)
        {
            if (symbol == null)
                throw new IllegalArgumentException ("Symbol is null");
            
            this.symbol = symbol;
        }

        @StructureField (name = SIDE)
        public void setSide (String side)
        {
            if (side == null)
                throw new IllegalArgumentException ("Side is null");
            
            try
            {
                this.side = OrderSide.valueOf (side);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown side: " + side);
            }
        }

        @StructureField (name = QUANTITY)
        public void setQualtity (long quantity)
        {
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            this.quantity = quantity;
        }

        @StructureField (name = ORDER_TYPE)
        public void setOrderType (String orderType)
        {
            if (orderType == null)
                throw new IllegalArgumentException ("Order type is null");
            
            try
            {
                this.orderType = OrderType.valueOf (orderType);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown order type: " + orderType);
            }
        }

        @StructureField (name = TIME_IN_FORCE, optional = true)
        public void setOrderID (String timeInForce)
        {
            if (timeInForce == null)
                throw new IllegalArgumentException ("Time in force is null");
            
            try
            {
                this.timeInForce = OrderTimeInForce.valueOf (timeInForce);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown time in force: " + timeInForce);
            }
        }

        @StructureField (name = lIMIT_PRICE, optional = true)
        public void setLimitPrice (long limitPrice)
        {
            if (limitPrice <= 0)
                throw new IllegalArgumentException ("Limit price <= 0");
            
            this.limitPrice = limitPrice;
        }

        @StructureField (name = STOP_PRICE, optional = true)
        public void setStopPrice (long stopPrice)
        {
            if (stopPrice <= 0)
                throw new IllegalArgumentException ("Stop price <= 0");
            
            this.stopPrice = stopPrice;
        }

        @StructureField (name = VISIBLE_QUANTITY, optional = true)
        public void setVisibleQuantity (long visibleQuantity)
        {
            if (visibleQuantity <= 0)
                throw new IllegalArgumentException ("Visible quantity <= 0");
            
            this.visibleQuantity = visibleQuantity;
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
    
    @SuppressWarnings ("unused")
    private static class MyReplaceOrder 
        extends AbstractReplaceOrderProtocolMessage
    {
        private long originalOrderID;
        private long orderID;
        private long quantity;
        private OrderType orderType;
        private OrderTimeInForce timeInForce = null;
        private long limitPrice = 0;
        private long stopPrice = 0;
        private long visibleQuantity = 0;

        @StructureField (name = ORIGINAL_ORDER_ID)
        public void setOriginalOrderID (long originalOrderID)
        {
            if (originalOrderID <= 0)
                throw new IllegalArgumentException ("Original order ID <= 0");
            
            this.originalOrderID = originalOrderID;
        }

        @StructureField (name = ORDER_ID)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = QUANTITY)
        public void setQualtity (long quantity)
        {
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            this.quantity = quantity;
        }

        @StructureField (name = ORDER_TYPE)
        public void setOrderType (String orderType)
        {
            if (orderType == null)
                throw new IllegalArgumentException ("Order type is null");
            
            try
            {
                this.orderType = OrderType.valueOf (orderType);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown order type: " + orderType);
            }
        }

        @StructureField (name = TIME_IN_FORCE, optional = true)
        public void setOrderID (String timeInForce)
        {
            if (timeInForce == null)
                throw new IllegalArgumentException ("Time in force is null");
            
            try
            {
                this.timeInForce = OrderTimeInForce.valueOf (timeInForce);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown time in force: " + timeInForce);
            }
        }

        @StructureField (name = lIMIT_PRICE, optional = true)
        public void setLimitPrice (long limitPrice)
        {
            if (limitPrice <= 0)
                throw new IllegalArgumentException ("Limit price <= 0");
            
            this.limitPrice = limitPrice;
        }

        @StructureField (name = STOP_PRICE, optional = true)
        public void setStopPrice (long stopPrice)
        {
            if (stopPrice <= 0)
                throw new IllegalArgumentException ("Stop price <= 0");
            
            this.stopPrice = stopPrice;
        }

        @StructureField (name = VISIBLE_QUANTITY, optional = true)
        public void setVisibleQuantity (long visibleQuantity)
        {
            if (visibleQuantity <= 0)
                throw new IllegalArgumentException ("Visible quantity <= 0");
            
            this.visibleQuantity = visibleQuantity;
        }
        
        @Override
        public long getOriginalOrderID ()
        {
            return originalOrderID;
        }
        
        @Override
        public long getOrderID ()
        {
            return orderID;
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
    
    @SuppressWarnings ("unused")
    private static class MyCancelOrder 
        extends AbstractCancelOrderProtocolMessage
    {
        private long orderID;

        @StructureField (name = ORDER_ID)
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
    }
}
