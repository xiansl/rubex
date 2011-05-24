package com.googlecode.rubex.protocol;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.protocol.event.ConnectionEvent;
import com.googlecode.rubex.protocol.event.ConnectionListener;

/**
 * Abstract base class for implementations of {@link Server} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractServer implements Server
{
    private final List <ConnectionListener <Message>> connectionListeners = 
        new ArrayList <ConnectionListener <Message>> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionListener (ConnectionListener <Message> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        connectionListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionListener (ConnectionListener <Message> listener)
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
    protected void fireOnNewConnection (Connection <Message> connection)
    {
        ConnectionEvent <Message> event = null;
        
        for (ConnectionListener <Message> listener: connectionListeners)
        {
            if (event == null)
                event = new ConnectionEvent <Message> (this, connection);
            
            listener.onNewConnection (event);
        }
    }
}
