package com.googlecode.rubex.net;

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
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;
import com.googlecode.rubex.protocol.ProtocolMessage;
import com.googlecode.rubex.protocol.ProtocolMessageType;
import com.googlecode.rubex.protocol.ProtocolUtils;

/**
 * Simple implementation of connection that allows sending and receiving 
 * business-level protocol messages.
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleProtocolConnection extends AbstractConnection <ProtocolMessage>
{
    private final static Logger logger =
        Logger.getLogger (SimpleProtocolConnection.class.getName ());

    private final Connection <DataObject> connection;

    /**
     * Create new simple protocol connection based on given connection.
     * 
     * @param connection connection to base on
     */
    public SimpleProtocolConnection (Connection <DataObject> connection)
    {
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
        
        connection.addMessageListener (new MyMessageListener ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage (ProtocolMessage message)
    {
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        connection.sendMessage (
            new StructureDataObjectBuilder ().
            addStringField ("messageType", message.getType ().name ()).
            addField ("messageBody", ProtocolUtils.marshal (message)).
            getStructureDataObject ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start ()
    {
        connection.start ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown ()
    {
        connection.shutdown ();
    }

    private void onMessage (MessageEvent <? extends DataObject> event)
    {
        DataObject dataObject = event.getMessage ();
        
        try
        {
            MyMessage message = new MyMessage (dataObject);
        
            fireOnMessage (ProtocolUtils.unmarshal (message.getMessageType (), message.getMessageBody ()));
        }
        catch (IllegalArgumentException ex)
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
                ProtocolUtils.marshal (
                    ProtocolUtils.createReject (
                        ex.getMessage (), dataObject)));
        }
    }

    private void onDisconnect (ConnectionEvent <? extends DataObject> event)
    {
        fireOnDisconnect ();
    }
    
    private class MyMessageListener implements MessageListener <DataObject>
    {
        @Override
        public void onMessage (MessageEvent <? extends DataObject> event)
        {
            SimpleProtocolConnection.this.onMessage (event);
        }

        @Override
        public void onDisconnect (ConnectionEvent <? extends DataObject> event)
        {
            SimpleProtocolConnection.this.onDisconnect (event);
        }
    }
    
    @SuppressWarnings ("unused")
    private static class MyMessage
    {
        private ProtocolMessageType messageType;
        private DataObject messageBody;
        
        public MyMessage (DataObject dataObject)
        {
            if (dataObject == null)
                throw new IllegalArgumentException ("Data object is null");
            
            String [] unusedFields = DataObjectUtils.mapFields (dataObject, this);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException ("Unknown fields: " + Arrays.asList (unusedFields));
        }
        
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
            if (messageBody == null)
                throw new IllegalArgumentException ("Message body is null");
            
            this.messageBody = messageBody;
        }

        public ProtocolMessageType getMessageType ()
        {
            return messageType;
        }

        public DataObject getMessageBody ()
        {
            return messageBody;
        }
    }
}
