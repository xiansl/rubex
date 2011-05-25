package com.googlecode.rubex.server;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectType;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.data.StructureDataObject;
import com.googlecode.rubex.data.StructureDataObjectBuilder;
import com.googlecode.rubex.data.StructureField;
import com.googlecode.rubex.net.Connection;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;
import com.googlecode.rubex.protocol.MessageType;

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
            if (!DataObjectType.STRUCTURE.equals (dataObject.getType ()))
                throw new IllegalArgumentException (
                    "Structure data object expected: " + dataObject);
            
            IncomingMessage incomingMessage = new IncomingMessage ();
            
            String [] unusedFields = DataObjectUtils.mapFields (
                (StructureDataObject)dataObject, incomingMessage);
            
            if (unusedFields.length > 0)
                throw new IllegalArgumentException (
                    "Unexpected fields in incoming message: " + 
                        Arrays.asList (unusedFields));
        }
        catch (Exception ex)
        {
            if (logger.isLoggable (Level.SEVERE))
                logger.log (
                    Level.SEVERE, 
                    "Invalid incoming message: " + dataObject, ex);
            
            connection.sendMessage (
                new StructureDataObjectBuilder ().
                addStringField ("messageType", MessageType.REJECT.name ()).
                addField ("messageBody",
                    new StructureDataObjectBuilder ().
                    addStringField ("rejectReason", ex.getMessage ()).
                    addField ("rejectedMessage", dataObject).
                    getStructureDataObject ()
                ).getStructureDataObject ());
        }
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
        public MessageType messageType;
        public DataObject messageBody;
        
        @StructureField (name = "messageType")
        public void setMessageType (String messageType)
        {
            if (messageType == null)
                throw new IllegalArgumentException ("Message type is null");
            
            try
            {
                this.messageType = MessageType.valueOf (messageType);
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
}
