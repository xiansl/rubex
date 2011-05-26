package com.googlecode.rubex.net;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;

/**
 * Simple implementation of {@link Connection <DataObject>} interface based on 
 * {@link Connection} object.
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleDataConnection 
    extends AbstractConnection <DataObject>
{
    private final static Logger logger = 
        Logger.getLogger (SimpleDataConnection.class.getName ());
    
    private final Connection <Message> connection;
    
    /**
     * Create new simple data connection based on given {@link Connection} 
     * object.
     * 
     * @param connection {@link Connection} object to base on
     */
    public SimpleDataConnection (Connection <Message> connection)
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
    public void sendMessage (DataObject data)
    {
        if (data == null)
            throw new IllegalArgumentException ("Data is null");
        
        try
        {
            connection.sendMessage (
                new MyMessage (data.toString ().getBytes ("UTF-8")));
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new Error (ex);
        }
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
    
    private void onMessage (MessageEvent <? extends Message> event)
    {
        if (event == null)
            throw new IllegalArgumentException ("Event is null");
        
        String messageString;
        try
        {
            messageString = 
                new String (event.getMessage ().getBytes (), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new Error (ex);
        }
        
        DataObject dataObject;
        try
        {
            dataObject = DataObjectUtils.parseFromString (messageString);
        }
        catch (IllegalArgumentException ex)
        {
            if (logger.isLoggable (Level.WARNING))
                logger.log (
                    Level.WARNING, 
                    "Cannot parse incoming message: " + messageString, ex);
            
            shutdown ();
            return;
        }
        
        if (logger.isLoggable (Level.FINE))
            logger.fine ("Data object received: " + dataObject);
        
        fireOnMessage (dataObject);
    }

    private void onDisconnect (ConnectionEvent <? extends Message> event)
    {
        fireOnDisconnect ();
    }
    
    private class MyMessageListener 
        implements MessageListener <Message>
    {
        @Override
        public void onMessage (MessageEvent <? extends Message> event)
        {
            SimpleDataConnection.this.onMessage (event);
        }

        @Override
        public void onDisconnect (ConnectionEvent <? extends Message> event)
        {
            SimpleDataConnection.this.onDisconnect (event);
        }
    }
    
    private static class MyMessage implements Message
    {
        private final byte [] data;
        
        public MyMessage (byte [] data)
        {
            if (data == null)
                throw new IllegalArgumentException ("Data is null");
            
            this.data = data.clone ();
        }

        @Override
        public int getSize ()
        {
            return data.length;
        }

        @Override
        public void copyData (int sourceOffset, byte[] destination,
            int destinationOffset, int length)
        {
            if (sourceOffset < 0)
                throw new IllegalArgumentException ("Source offset < 0");
            
            if (destination == null)
                throw new IllegalArgumentException ("Destination is null");
            
            if (destinationOffset < 0)
                throw new IllegalArgumentException ("Destination offset < 0");
            
            if (length < 0)
                throw new IllegalArgumentException ("Length < 0");
            
            int size = data.length;
            
            if (sourceOffset + length > size)
                throw new IllegalArgumentException (
                    "Source offset + length > size");
            
            int destinationSize = destination.length;
            
            if (destinationOffset + length > destinationSize)
                throw new IllegalArgumentException (
                    "Destination offset + length > destination size");
            
            System.arraycopy (
                data, sourceOffset, destination, destinationOffset, length);
        }
        
        @Override
        public byte[] getBytes ()
        {
            return data.clone ();
        }
    }
}
