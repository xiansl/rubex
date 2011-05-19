package com.googlecode.rubex.server.event;

import java.util.EventObject;

import com.googlecode.rubex.server.Connection;

public class ConnectionEvent extends EventObject
{
    private final Connection connection;
    
    public ConnectionEvent (Object source, Connection connection)
    {
        super (source);
        
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
    }
    
    public Connection getConnection ()
    {
        return connection;
    }
}
