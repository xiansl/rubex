package com.googlecode.rubex.server;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectType;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.data.StringDataObject;
import com.googlecode.rubex.data.StructureDataObject;
import com.googlecode.rubex.data.StructureDataObjectBuilder;
import com.googlecode.rubex.data.StructureField;
import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;
import com.googlecode.rubex.net.Connection;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;
import com.googlecode.rubex.protocol.ProtocolMessageType;

public class RubexServerSession
{
    private final static Logger logger =
        Logger.getLogger (RubexServerSession.class.getName ());
    
    private final Connection <DataObject> connection;
    
    public RubexServerSession (Connection <DataObject> connection)
    {
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
        
        connection.addMessageListener (new MyMessageListener ());
    }
    
    private synchronized void processMessage (DataObject dataObject)
    {
        if (dataObject == null)
            throw new IllegalArgumentException ("Data object is null");

        try
        {
            IncomingMessage incomingMessage = new IncomingMessage ();
            
            String [] unusedFields = DataObjectUtils.mapFields (
                dataObject, incomingMessage);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException (
                    "Unexpected fields in incoming message: " + 
                        Arrays.asList (unusedFields));
            
            ProtocolMessageType messageType = incomingMessage.messageType; 
            
            switch (messageType)
            {
            case NEW_ORDER:
                processNewOrder (incomingMessage.messageBody);
                break;
            case REJECT:
                processReject (incomingMessage.messageBody);
                break;
            default:
                throw new UnsupportedOperationException (
                    "Unsupported message type: " + messageType);
            }
        }
        catch (Exception ex)
        {
            if (logger.isLoggable (Level.SEVERE))
                logger.log (
                    Level.SEVERE, 
                    "Invalid incoming message: " + dataObject, ex);
            
            if (DataObjectType.STRUCTURE.equals (dataObject.getType ()))
            {
                StructureDataObject structureDataObject =
                    (StructureDataObject)dataObject;
                
                if (structureDataObject.hasField ("messageType"))
                {
                    DataObject messageType = 
                        structureDataObject.getField ("messageType");
                    
                    if (DataObjectType.STRING.equals (messageType.getType ()))
                    {
                        StringDataObject stringMessageType = 
                            (StringDataObject)messageType;
                        
                        if ("REJECT".equals (stringMessageType.getString ()))
                            return; // Never reject reject message
                    }
                }
            }
            
            connection.sendMessage (
                new StructureDataObjectBuilder ().
                addStringField ("messageType", ProtocolMessageType.REJECT.name ()).
                addField ("messageBody",
                    new StructureDataObjectBuilder ().
                    addStringField ("rejectReason", ex.getMessage ()).
                    addField ("rejectedMessage", dataObject).
                    getStructureDataObject ()
                ).getStructureDataObject ());
        }
    }
    
    private void processNewOrder (DataObject dataObject)
    {
        NewOrderRequest newOrder = new NewOrderRequest ();
        String [] unusedFields = DataObjectUtils.mapFields (dataObject, newOrder);
        
        if (unusedFields.length > 0)
            throw new IllegalArgumentException (
                "Unexpected fields in reject message: " + 
                    Arrays.asList (unusedFields));
    }
    
    private void processReject (DataObject dataObject)
    {
        Reject reject = new Reject ();
        String [] unusedFields = DataObjectUtils.mapFields (dataObject, reject);
        
        if (unusedFields.length > 0)
            throw new IllegalArgumentException (
                "Unexpected fields in reject message: " + 
                    Arrays.asList (unusedFields));
        
        if (logger.isLoggable (Level.SEVERE))
            logger.severe (
                "Message was rejected by client: " + 
                reject.rejectReason + ": " + reject.rejectedMessage);
    }
    
    private class MyMessageListener implements MessageListener <DataObject>
    {
        @Override
        public void onMessage (MessageEvent <? extends DataObject> event)
        {
            if (event == null)
                throw new IllegalArgumentException ("Event is null");
            
            processMessage (event.getMessage ());
        }

        @Override
        public void onDisconnect (ConnectionEvent <? extends DataObject> event)
        {
            // Do nothing
        }
    }
    
    @SuppressWarnings ("unused")
    private static class IncomingMessage
    {
        public ProtocolMessageType messageType;
        public DataObject messageBody;
        
        @StructureField (name = "messageType")
        public void setMessageType (String messageType)
        {
            if (messageType == null)
                throw new IllegalArgumentException ("Message type is null");
            
            try
            {
                this.messageType = ProtocolMessageType.valueOf (messageType);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException ("Unknown message type: " + messageType);
            }
        }
        
        @StructureField (name = "messageBody")
        public void setMessageBody (DataObject messageBody)
        {
            this.messageBody = messageBody;
        }
    }
    
    @SuppressWarnings ("unused")
    private static class NewOrderRequest
    {
        public long orderID;
        public long account = 0;
        public String symbol;
        public OrderSide side;
        public long quantity;
        public OrderType orderType;
        public OrderTimeInForce timeInForce = null;
        public long limitPrice = 0;
        private long stopPrice = 0;
        private long visibleQuantity = 0;
        
        @StructureField (name = "orderID")
        public void setOrderID (long orderID)
        {
            if (orderID <= 0L)
                throw new IllegalArgumentException ("Order ID <= 0");
            
            this.orderID = orderID;
        }
        
        @StructureField (name = "account", optional = true)
        public void setAccount (long account)
        {
            if (account <= 0L)
                throw new IllegalArgumentException ("Account <= 0");
            
            this.account = account;
        }
        
        @StructureField (name = "symbol")
        public void setSymbol (String symbol)
        {
            if (symbol == null)
                throw new IllegalArgumentException ("Symbol is null");
            
            this.symbol = symbol;
        }
        
        @StructureField (name = "side")
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
                throw new IllegalArgumentException ("Unknown order side: " + side);
            }
        }
        
        @StructureField (name = "quantity")
        public void setQuantity (long quantity)
        {
            if (quantity <= 0)
                throw new IllegalArgumentException ("Quantity <= 0");
            
            this.quantity = quantity; 
        }
        
        @StructureField (name = "orderType")
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
                throw new IllegalArgumentException (
                    "Unknown order type: " + orderType);
            }
        }
        
        @StructureField (name = "timeInForce", optional = true)
        public void setTimeInForce (String timeInForce)
        {
            if (timeInForce == null)
                throw new IllegalArgumentException ("Time in force is null");
            
            try
            {
                this.timeInForce = OrderTimeInForce.valueOf (timeInForce);
            }
            catch (IllegalArgumentException ex)
            {
                throw new IllegalArgumentException (
                    "Unknown time in force: " + timeInForce);
            }
        }
        
        @StructureField (name = "limitPrice", optional = true)
        public void setLimitPrice (long limitPrice)
        {
            if (limitPrice <= 0)
                throw new IllegalArgumentException ("Limit price <= 0");
            
            this.limitPrice = limitPrice; 
        }
        
        @StructureField (name = "stopPrice", optional = true)
        public void setStopPrice (long stopPrice)
        {
            if (stopPrice <= 0)
                throw new IllegalArgumentException ("Stop price <= 0");
            
            this.stopPrice = stopPrice; 
        }
        
        @StructureField (name = "visibleQuantity", optional = true)
        public void setVisibleQuantity (long visibleQuantity)
        {
            if (visibleQuantity <= 0)
                throw new IllegalArgumentException ("isible Quantity <= 0");
            
            this.visibleQuantity = visibleQuantity; 
        }
    }
    
    @SuppressWarnings ("unused")
    private static class Reject
    {
        public String rejectReason;
        public DataObject rejectedMessage;
        
        @StructureField (name = "rejectReason")
        public void setRejectReason (String rejectReason)
        {
            if (rejectReason == null)
                throw new IllegalArgumentException ("Reject reason is null");
            
            this.rejectReason = rejectReason;
        }
        
        @StructureField (name = "rejectedMessage")
        public void setRejectedMessage (DataObject rejectedMessage)
        {
            if (rejectedMessage == null)
                throw new IllegalArgumentException ("Rejected message is null");
            
            this.rejectedMessage = rejectedMessage;
        }
    }
}
