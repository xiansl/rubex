package com.googlecode.rubex.server;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.server.event.ConnectionEvent;
import com.googlecode.rubex.server.event.ConnectionListener;

/**
 * Abstract base class for implementations of {@link Server} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractServer implements Server
{
    private final List <ConnectionListener> connectionListeners = 
        new ArrayList <ConnectionListener> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionListener (ConnectionListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        connectionListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionListener (ConnectionListener listener)
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
    public ConnectionListener [] getAllConnectionListeners ()
    {
        return connectionListeners.toArray (
            new ConnectionListener[connectionListeners.size ()]);
    }
    
    /**
     * Notify all connection listeners about newly accepted connection.
     * 
     * @param connection newly accepted connection 
     */
    protected void fireOnNewConnection (Connection connection)
    {
        ConnectionEvent event = null;
        
        for (ConnectionListener listener: connectionListeners)
        {
            if (event == null)
                event = new ConnectionEvent (this, connection);
            
            listener.onNewConnection (event);
        }
    }
}
