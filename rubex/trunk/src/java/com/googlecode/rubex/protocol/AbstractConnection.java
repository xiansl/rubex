package com.googlecode.rubex.protocol;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.protocol.event.ConnectionEvent;
import com.googlecode.rubex.protocol.event.MessageEvent;
import com.googlecode.rubex.protocol.event.MessageListener;

/**
 * Abstract base class for implementations of {@link Connection} interface.
 * 
 * @param <MessageType> type of the messages to be send and received
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractConnection <MessageType> 
    implements Connection <MessageType>
{
    private final List <MessageListener <MessageType>> messageListeners = 
        new ArrayList <MessageListener <MessageType>> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener (MessageListener <MessageType> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        messageListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener (MessageListener <MessageType> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        messageListeners.remove (listener);
    }

    /**
     * Get all message listeners.
     * 
     * @return an array of {@link MessageListener} objects
     */
    @SuppressWarnings ("unchecked")
    public MessageListener <MessageType> [] getAllMessageListeners ()
    {
        return (MessageListener <MessageType> [])messageListeners.toArray (
            new MessageListener <?> [messageListeners.size ()]);
    }
    
    /**
     * Notify all message listeners about incoming message.
     * 
     * @param message incoming message
     */
    protected void fireOnMessage (MessageType message)
    {
        MessageEvent <MessageType> event = null;
        
        for (MessageListener <MessageType> listener: messageListeners)
        {
            if (event == null)
                event = new MessageEvent <MessageType> (this, message);
            
            listener.onMessage (event);
        }
    }
    
    /**
     * Notify all message listeners about disconnect.
     */
    protected void fireOnDisconnect ()
    {
        ConnectionEvent <MessageType> event = null;
        
        for (MessageListener <MessageType> listener: messageListeners)
        {
            if (event == null)
                event = new ConnectionEvent <MessageType> (this, this);
            
            listener.onDisconnect (event);
        }
    }
}
