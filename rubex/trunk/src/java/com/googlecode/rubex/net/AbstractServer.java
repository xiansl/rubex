package com.googlecode.rubex.net;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.ConnectionListener;

/**
 * Abstract base class for implementations of {@link Server} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractServer <MessageType> 
    implements Server <MessageType>
{
    private final static Logger logger =
        Logger.getLogger (AbstractServer.class.getName ());
    
    private final List <ConnectionListener <MessageType>> 
        connectionListeners = 
            new ArrayList <ConnectionListener <MessageType>> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionListener (
        ConnectionListener <MessageType> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        connectionListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionListener (
        ConnectionListener <MessageType> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        connectionListeners.remove (listener);
    }

    /**
     * Get all connection listeners.
     * 
     * @return an array of {@link ConnectionListener} objects
     */
    @SuppressWarnings ("unchecked")
    public ConnectionListener <Message> [] getAllConnectionListeners ()
    {
        return (ConnectionListener <Message> [])connectionListeners.toArray (
            new ConnectionListener <?> [connectionListeners.size ()]);
    }
    
    /**
     * Notify all connection listeners about newly accepted connection.
     * 
     * @param connection newly accepted connection 
     */
    protected void fireOnNewConnection (Connection <MessageType> connection)
    {
        ConnectionEvent <MessageType> event = null;
        
        for (ConnectionListener <MessageType> listener: 
            connectionListeners)
        {
            if (event == null)
                event = new ConnectionEvent <MessageType> (this, connection);

            try
            {
                listener.onNewConnection (event);
            }
            catch (Exception ex)
            {
                if (logger.isLoggable (Level.SEVERE))
                    logger.log (
                        Level.SEVERE, "Exception in connection listener", ex);
            }
        }
    }
}
