package com.googlecode.rubex.server;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.server.event.ConnectionEvent;
import com.googlecode.rubex.server.event.MessageEvent;
import com.googlecode.rubex.server.event.MessageListener;

/**
 * Abstract base class for implementations of {@link Connection} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractConnection implements Connection
{
    private final List <MessageListener> messageListeners = 
        new ArrayList <MessageListener> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener (MessageListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        messageListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener (MessageListener listener)
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
    public MessageListener [] getAllMessageListeners ()
    {
        return messageListeners.toArray (
            new MessageListener [messageListeners.size ()]);
    }
    
    /**
     * Notify all message listeners about incoming message.
     * 
     * @param message incoming message
     */
    protected void fireOnMessage (Message message)
    {
        MessageEvent event = null;
        
        for (MessageListener listener: messageListeners)
        {
            if (event == null)
                event = new MessageEvent (this, message);
            
            listener.onMessage (event);
        }
    }
    
    /**
     * Notify all message listeners about disconnect.
     */
    protected void fireOnDisconnect ()
    {
        ConnectionEvent event = null;
        
        for (MessageListener listener: messageListeners)
        {
            if (event == null)
                event = new ConnectionEvent (this, this);
            
            listener.onDisconnect (event);
        }
    }
}
