package com.googlecode.rubex.protocol;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.protocol.event.ConnectionListener;

/**
 * Network server that can accept connections.
 * 
 * @author Mikhail Vladimirov
 */
public interface Server
{
    /**
     * Add new listener to be notified about new connections.
     * 
     * @param listener listener to be added
     */
    public void addConnectionListener (ConnectionListener <Message> listener);
    
    /**
     * Remove connection listener.
     * 
     * @param listener listener to be removed
     */
    public void removeConnectionListener (
        ConnectionListener <Message> listener);

    /**
     * Start the server.
     */
    public void start ();
    
    /**
     * Shutdown the server.
     */
    public void shutdown ();
    
    /**
     * Get all open connections.
     * 
     * @return an array of {@link Connection} objects
     */
    public Connection <Message> [] getAllConnections ();
}
