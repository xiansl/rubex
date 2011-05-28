package com.googlecode.rubex.protocol;

import java.util.Arrays;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.data.StructureDataObjectBuilder;
import com.googlecode.rubex.data.StructureField;
import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;
import com.googlecode.rubex.party.PartyOrderState;

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
        
        switch (messageType)
        {
        case REJECT:
            return new MyReject (dataObject);
        case NEW_ORDER:
            return new MyNewOrder (dataObject);
        case REPLACE_ORDER:
            return new MyReplaceOrder (dataObject);
        case CANCEL_ORDER:
            return new MyCancelOrder (dataObject);
        case ORDER_STATUS:
            return new MyOrderStatus (dataObject);
        default:
            throw new IllegalArgumentException (
                "Unknown message type: " + messageType);
        }
    }
    
    /**
     * Create reject message with given reject reason and rejected message.
     * 
     * @param rejectReason reject reason
     * @param rejectedMessage message that was rejected
     * @return reject message
     */
    public static RejectProtocolMessage createReject (
        String rejectReason,
        DataObject rejectedMessage)
    {
        if (rejectReason == null)
            throw new IllegalArgumentException ("Reject reason is null");
        
        if (rejectedMessage == null)
            throw new IllegalArgumentException ("Rejected message is null");
        
        return new MyReject (rejectReason, rejectedMessage);
    }

    /**
     * Create new order message with given order ID, account, symbol, side
     * quantity, order type, time in force, limit price, stop price and
     * visible quantity.
     * 
     * @param orderID order ID
     * @param account account or 0 for default account
     * @param symbol symbol
     * @param side order side
     * @param quantity order quantity in quantity units
     * @param orderType order type
     * @param timeInForce time in force or <code>null</code> if there is no time 
     *        in force
     * @param limitPrice limit price in price units or 0 if there is no limit 
     *        price
     * @param stopPrice stop price in price units or 0 if there is no stop 
     *        price
     * @param visibleQuantity visible quantity in quantity units or 0 if there 
     *        is no visible quantity
     * @return new order message
     */
    public static NewOrderProtocolMessage createNewOrder (
        long orderID, long account, String symbol,
        OrderSide side, long quantity, OrderType orderType,
        OrderTimeInForce timeInForce, long limitPrice, long stopPrice,
        long visibleQuantity)
    {
        if (orderID <= 0)
            throw new IllegalArgumentException ("Order ID <= 0");
        
        if (account < 0)
            throw new IllegalArgumentException ("Account < 0");
        
        if (symbol == null)
            throw new IllegalArgumentException ("Symbol is null");
        
        if (side == null)
            throw new IllegalArgumentException ("Side is null");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (orderType == null)
            throw new IllegalArgumentException ("Order type is null");
                
        if (limitPrice < 0)
            throw new IllegalArgumentException ("Limit price < 0");
        
        if (stopPrice < 0)
            throw new IllegalArgumentException ("Stop price < 0");
        
        if (visibleQuantity < 0)
            throw new IllegalArgumentException ("Visible quantity < 0");
            
        return new MyNewOrder (
            orderID, account, symbol, side, quantity, orderType, timeInForce, 
            limitPrice, stopPrice, visibleQuantity);
    }
    
    /**
     * Create replace order message with given original order ID, order ID, 
     * account, symbol, side quantity, order type, time in force, limit price, 
     * stop price and visible quantity.
     * 
     * @param originalOrderID original order ID
     * @param orderID order ID
     * @param quantity order quantity in quantity units
     * @param orderType order type
     * @param timeInForce time in force or <code>null</code> if there is no time 
     *        in force
     * @param limitPrice limit price in price units or 0 if there is no limit 
     *        price
     * @param stopPrice stop price in price units or 0 if there is no stop 
     *        price
     * @param visibleQuantity visible quantity in quantity units or 0 if there 
     *        is no visible quantity
     * @return replace order message
     */
    public static ReplaceOrderProtocolMessage createReplaceOrder (
        long originalOrderID, long orderID, 
        long quantity, OrderType orderType, OrderTimeInForce timeInForce, 
        long limitPrice, long stopPrice, long visibleQuantity)
    {
        if (originalOrderID <= 0)
            throw new IllegalArgumentException ("Original order ID <= 0");
        
        if (orderID <= 0)
            throw new IllegalArgumentException ("Order ID <= 0");
        
        if (quantity <= 0)
            throw new IllegalArgumentException ("Quantity <= 0");
        
        if (orderType == null)
            throw new IllegalArgumentException ("Order type is null");
                
        if (limitPrice < 0)
            throw new IllegalArgumentException ("Limit price < 0");
        
        if (stopPrice < 0)
            throw new IllegalArgumentException ("Stop price < 0");
        
        if (visibleQuantity < 0)
            throw new IllegalArgumentException ("Visible quantity < 0");
        
        return new MyReplaceOrder (
            originalOrderID, orderID, quantity, orderType, timeInForce, 
            limitPrice, stopPrice, visibleQuantity);
    }
    
    /**
     * Create cancel order message with given orderID.
     * 
     * @param orderID order ID
     * @return cancel order message
     */
    public static CancelOrderProtocolMessage createCancel (long orderID)
    {
        if (orderID <= 0)
            throw new IllegalArgumentException ("Order ID <= 0");
        
        return new MyCancelOrder (orderID);
    }
    
    /**
     * Create order status message with given order ID, order state, account,
     * symbol, side, quantity, filled quantity, filled value, order type,
     * time in force, limit price, stop price and visible quantity.
     * 
     * @param orderID order ID
     * @return cancel order message
     */
    public static OrderStatusProtocolMessage createOrderStatus (
        long orderID, PartyOrderState orderState, 
        long account, String symbol,
        OrderSide side, long quantity, long filledQuantity, long filledValue,
        OrderType orderType, OrderTimeInForce timeInForce, long limitPrice, 
        long stopPrice, long visibleQuantity)
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
        
        return new MyOrderStatus (
            orderID, orderState, account, symbol, side, quantity, 
            filledQuantity, filledValue, orderType, timeInForce, 
            limitPrice, stopPrice, visibleQuantity);
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
        
        @Override
        public DataObject visitOrderStatus (
            OrderStatusProtocolMessage orderStatus)
        {
            if (orderStatus == null)
                throw new IllegalArgumentException ("New order is null");
            
            StructureDataObjectBuilder builder = 
                new StructureDataObjectBuilder ();
            
            builder.addIntegerField (
                OrderStatusProtocolMessage.ORDER_ID, 
                orderStatus.getOrderID ());
            
            builder.addStringField (
                OrderStatusProtocolMessage.ORDER_STATE,
                orderStatus.getOrderState ().name ());
            
            long account = orderStatus.getAccount ();
            if (account != 0)
                builder.addIntegerField (
                    OrderStatusProtocolMessage.ACCOUNT, account);
            
            builder.addStringField (
                OrderStatusProtocolMessage.SYMBOL, orderStatus.getSymbol ());
            builder.addStringField (
                OrderStatusProtocolMessage.SIDE, 
                orderStatus.getSide ().name ());
            builder.addIntegerField (
                OrderStatusProtocolMessage.QUANTITY, 
                orderStatus.getQuantity ());
            builder.addIntegerField (
                OrderStatusProtocolMessage.FILLED_QUANTITY, 
                orderStatus.getFilledQuantity ());
            builder.addIntegerField (
                OrderStatusProtocolMessage.FILLED_VALUE, 
                orderStatus.getFilledValue ());
            builder.addStringField (
                OrderStatusProtocolMessage.ORDER_TYPE, 
                orderStatus.getOrderType ().name ());
            
            OrderTimeInForce timeInforce = orderStatus.getTimeInForce ();
            if (timeInforce != null)
                builder.addStringField (
                    OrderStatusProtocolMessage.TIME_IN_FORCE, 
                    timeInforce.name ());
            
            long limitPrice = orderStatus.getLimitPrice ();
            if (limitPrice != 0)
                builder.addIntegerField (
                    OrderStatusProtocolMessage.lIMIT_PRICE, limitPrice);
            
            long stopPrice = orderStatus.getStopPrice ();
            if (stopPrice != 0)
                builder.addIntegerField (
                    OrderStatusProtocolMessage.STOP_PRICE, stopPrice);
            
            long visibleQuantity = orderStatus.getVisibleQuantity ();
            if (visibleQuantity != 0)
                builder.addIntegerField (
                    OrderStatusProtocolMessage.VISIBLE_QUANTITY, 
                    visibleQuantity);
            
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
        
        @Override
        public ProtocolMessage visitOrderStatus (
            OrderStatusProtocolMessage orderStatus)
        {
            OrderType type = orderStatus.getOrderType ();
            
            switch (type)
            {
            case MARKET:
                if (orderStatus.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for market order");
                
                if (orderStatus.getLimitPrice () != 0)
                    throw new IllegalArgumentException (
                        "Limit price is set for market order");
                
                if (orderStatus.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for market order");
                
                if (orderStatus.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for market order");
                
                break;
            case LIMIT:
                if (orderStatus.getTimeInForce () == null)
                    throw new IllegalArgumentException (
                        "Time in force is not set for limit order");
                
                if (orderStatus.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for limit order");
                
                if (orderStatus.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for limit order");
                
                if (orderStatus.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for limit order");
                
                break;
            case STOP:
                if (orderStatus.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for stop order");
                
                if (orderStatus.getLimitPrice () != 0)
                    throw new IllegalArgumentException (
                        "Limit price is set for stop order");
                
                if (orderStatus.getStopPrice () == 0)
                    throw new IllegalArgumentException (
                        "Stop price is not set for stop order");
                
                if (orderStatus.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for stop order");
                
                break;
            case STOP_LIMIT:
                if (orderStatus.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for stop limit order");
                
                if (orderStatus.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for stop limit order");
                
                if (orderStatus.getStopPrice () == 0)
                    throw new IllegalArgumentException (
                        "Stop price is not set for stop limit order");
                
                if (orderStatus.getVisibleQuantity () != 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is set for stop limit order");
                
                break;
            case ICEBERG:
                if (orderStatus.getTimeInForce () != null)
                    throw new IllegalArgumentException (
                        "Time in force is set for iceberg order");
                
                if (orderStatus.getLimitPrice () == 0)
                    throw new IllegalArgumentException (
                        "Limit price is not set for iceberg order");
                
                if (orderStatus.getStopPrice () != 0)
                    throw new IllegalArgumentException (
                        "Stop price is set for iceberg order");
                
                if (orderStatus.getVisibleQuantity () == 0)
                    throw new IllegalArgumentException (
                        "Visible quantity is not set for iceberg order");
                
                break;
            default:
                throw new IllegalArgumentException (
                    "Unknown order type: " + type);
            }
            
            return orderStatus;
        }
    }
    
    @SuppressWarnings ("unused")
    private static class MyReject extends AbstractRejectProtocolMessage
    {
        private String rejectReason;
        private DataObject rejectedMessage;
        
        public MyReject (DataObject dataObject)
        {
            if (dataObject == null)
                throw new IllegalArgumentException ("Data object is null");
            
            String [] unusedFields = DataObjectUtils.mapFields (dataObject, this);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException ("Unknown fields: " + Arrays.asList (unusedFields));
            
            accept (VALIDATING_VISITOR);
        }
        
        public MyReject (String rejectReason, DataObject rejectedMessage)
        {
            if (rejectReason == null)
                throw new IllegalArgumentException ("Reject reason is null");
            
            if (rejectedMessage == null)
                throw new IllegalArgumentException ("Rejected message is null");
            
            this.rejectReason = rejectReason;
            this.rejectedMessage = rejectedMessage;
            
            accept (VALIDATING_VISITOR);
        }

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

        public MyNewOrder (DataObject dataObject)
        {
            if (dataObject == null)
                throw new IllegalArgumentException ("Data object is null");
            
            String [] unusedFields = DataObjectUtils.mapFields (dataObject, this);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException ("Unknown fields: " + Arrays.asList (unusedFields));
            
            accept (VALIDATING_VISITOR);
        }
        
        public MyNewOrder (long orderID, long account, String symbol,
                OrderSide side, long quantity, OrderType orderType,
                OrderTimeInForce timeInForce, long limitPrice, long stopPrice,
                long visibleQuantity)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            if (account < 0)
                throw new IllegalArgumentException ("Account < 0");
            
            if (symbol == null)
                throw new IllegalArgumentException ("Symbol is null");
            
            if (side == null)
                throw new IllegalArgumentException ("Side is null");
            
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            if (orderType == null)
                throw new IllegalArgumentException ("Order type is null");
                    
            if (limitPrice < 0)
                throw new IllegalArgumentException ("Limit price < 0");
            
            if (stopPrice < 0)
                throw new IllegalArgumentException ("Stop price < 0");
            
            if (visibleQuantity < 0)
                throw new IllegalArgumentException ("Visible quantity < 0");
            
            this.orderID = orderID;
            this.account = account;
            this.symbol = symbol;
            this.side = side;
            this.quantity = quantity;
            this.orderType = orderType;
            this.timeInForce = timeInForce;
            this.limitPrice = limitPrice;
            this.stopPrice = stopPrice;
            this.visibleQuantity = visibleQuantity;
            
            accept (VALIDATING_VISITOR);
        }

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
        public void setQuantity (long quantity)
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

        public MyReplaceOrder (DataObject dataObject)
        {
            if (dataObject == null)
                throw new IllegalArgumentException ("Data object is null");
            
            String [] unusedFields = DataObjectUtils.mapFields (dataObject, this);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException ("Unknown fields: " + Arrays.asList (unusedFields));
            
            accept (VALIDATING_VISITOR);
        }
        
        public MyReplaceOrder (long originalOrderID, long orderID, 
            long quantity, OrderType orderType, OrderTimeInForce timeInForce, 
            long limitPrice, long stopPrice, long visibleQuantity)
        {
            if (originalOrderID <= 0)
                throw new IllegalArgumentException ("Original order ID <= 0");
            
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            if (orderType == null)
                throw new IllegalArgumentException ("Order type is null");
                    
            if (limitPrice < 0)
                throw new IllegalArgumentException ("Limit price < 0");
            
            if (stopPrice < 0)
                throw new IllegalArgumentException ("Stop price < 0");
            
            if (visibleQuantity < 0)
                throw new IllegalArgumentException ("Visible quantity < 0");
            
            this.originalOrderID = originalOrderID;
            this.orderID = orderID;
            this.quantity = quantity;
            this.orderType = orderType;
            this.timeInForce = timeInForce;
            this.limitPrice = limitPrice;
            this.stopPrice = stopPrice;
            this.visibleQuantity = visibleQuantity;
            
            accept (VALIDATING_VISITOR);
        }
        
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
        public void setQuantity (long quantity)
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

        public MyCancelOrder (DataObject dataObject)
        {
            if (dataObject == null)
                throw new IllegalArgumentException ("Data object is null");
            
            String [] unusedFields = DataObjectUtils.mapFields (dataObject, this);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException ("Unknown fields: " + Arrays.asList (unusedFields));
            
            accept (VALIDATING_VISITOR);
        }
        
        public MyCancelOrder (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
            
            accept (VALIDATING_VISITOR);
        }
        
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
    
    @SuppressWarnings ("unused")
    private static class MyOrderStatus 
        extends AbstractOrderStatusProtocolMessage
    {
        private long orderID;
        private PartyOrderState orderState;
        private long account = 0;
        private String symbol;
        private OrderSide side;
        private long quantity;
        private long filledQuantity;
        private long filledValue;
        private OrderType orderType;
        private OrderTimeInForce timeInForce = null;
        private long limitPrice = 0;
        private long stopPrice = 0;
        private long visibleQuantity = 0;

        public MyOrderStatus (DataObject dataObject)
        {
            if (dataObject == null)
                throw new IllegalArgumentException ("Data object is null");
            
            String [] unusedFields = DataObjectUtils.mapFields (dataObject, this);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException ("Unknown fields: " + Arrays.asList (unusedFields));
            
            accept (VALIDATING_VISITOR);
        }
        
        public MyOrderStatus (
            long orderID, PartyOrderState orderState, 
            long account, String symbol,
            OrderSide side, long quantity, long filledQuantity, long filledValue,
            OrderType orderType, OrderTimeInForce timeInForce, long limitPrice, 
            long stopPrice, long visibleQuantity)
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
            
            accept (VALIDATING_VISITOR);
        }

        @StructureField (name = ORDER_ID)
        public void setOrderID (long orderID)
        {
            if (orderID <= 0)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }

        @StructureField (name = ORDER_STATE)
        public void setOrderState (String orderState)
        {
            if (orderState == null)
                throw new IllegalArgumentException ("Order state is null");
            
            try
            {
                this.orderState = PartyOrderState.valueOf (orderState);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown order state: " + side);
            }
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
        public void setQuantity (long quantity)
        {
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            this.quantity = quantity;
        }

        @StructureField (name = FILLED_QUANTITY)
        public void setFilledQuantity (long filledQuantity)
        {
            if (filledQuantity <= 0)
                throw new IllegalArgumentException ("Filled quantity <= 0");
            
            this.filledQuantity = filledQuantity;
        }

        @StructureField (name = FILLED_VALUE)
        public void setFilledValue (long filledValue)
        {
            if (filledValue <= 0)
                throw new IllegalArgumentException ("Fille value <= 0");
            
            this.filledValue = filledValue;
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
    }
}
