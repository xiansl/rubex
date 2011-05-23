package com.googlecode.rubex.protocol.event;

import java.util.EventObject;

import com.googlecode.rubex.protocol.Connection;

/**
 * Contains details about connection-related event.
 * 
 * @author Mikhail Vladimirov
 */
public class ConnectionEvent extends EventObject
{
    private final Connection connection;
    
    /**
     * Create new connection event with given event source and connection.
     * 
     * @param source event source
     * @param connection connection
     */
    public ConnectionEvent (Object source, Connection connection)
    {
        super (source);
        
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
    }
    
    /**
     * Return connection of this event.
     */
    public Connection getConnection ()
    {
        return connection;
    }
}
