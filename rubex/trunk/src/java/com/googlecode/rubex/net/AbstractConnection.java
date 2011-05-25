package com.googlecode.rubex.net;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;

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
    private final static Logger logger =
        Logger.getLogger (AbstractConnection.class.getName ());
    
    private final List <MessageListener <? super MessageType>> 
        messageListeners = 
            new ArrayList <MessageListener <? super MessageType>> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener (
        MessageListener <? super MessageType> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        messageListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener (
        MessageListener <? super MessageType> listener)
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
        
        for (MessageListener <? super MessageType> listener: messageListeners)
        {
            if (event == null)
                event = new MessageEvent <MessageType> (this, message);
            
            try
            {
                listener.onMessage (event);
            }
            catch (Exception ex)
            {
                if (logger.isLoggable (Level.SEVERE))
                    logger.log (
                        Level.SEVERE, "Exception in message listener", ex);
            }
        }
    }
    
    /**
     * Notify all message listeners about disconnect.
     */
    protected void fireOnDisconnect ()
    {
        ConnectionEvent <MessageType> event = null;
        
        for (MessageListener <? super MessageType> listener: messageListeners)
        {
            if (event == null)
                event = new ConnectionEvent <MessageType> (this, this);
            
            try
            {
                listener.onDisconnect (event);
            }
            catch (Exception ex)
            {
                if (logger.isLoggable (Level.SEVERE))
                    logger.log (
                        Level.SEVERE, "Exception in message listener", ex);
            }
        }
    }
}
