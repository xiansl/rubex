package com.googlecode.rubex.net.event;

import java.util.EventObject;

import com.googlecode.rubex.net.Connection;

/**
 * Contains details about connection-related event.
 * 
 * @param <MessageType> type of connection messages
 * 
 * @author Mikhail Vladimirov
 */
public class ConnectionEvent <MessageType> extends EventObject
{
    private final Connection <MessageType> connection;
    
    /**
     * Create new connection event with given event source and connection.
     * 
     * @param source event source
     * @param connection connection
     */
    public ConnectionEvent (Object source, Connection <MessageType> connection)
    {
        super (source);
        
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
    }
    
    /**
     * Return connection of this event.
     */
    public Connection <MessageType> getConnection ()
    {
        return connection;
    }
}
