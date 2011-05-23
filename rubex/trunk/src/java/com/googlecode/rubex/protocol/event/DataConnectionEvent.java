package com.googlecode.rubex.protocol.event;

import java.util.EventObject;

import com.googlecode.rubex.protocol.DataConnection;

/**
 * Contains details about data connection-related event.
 * 
 * @author Mikhail Vladimirov
 */
public class DataConnectionEvent extends EventObject
{
    private final DataConnection dataConnection;
    
    /**
     * Create new data connection event with given event source and data 
     * connection.
     * 
     * @param source event source
     * @param dataConnection data connection
     */
    public DataConnectionEvent (Object source, DataConnection dataConnection)
    {
        super (source);
        
        if (dataConnection == null)
            throw new IllegalArgumentException ("Data connection is null");
        
        this.dataConnection = dataConnection;
    }
    
    /**
     * Return data connection of this event.
     */
    public DataConnection getDataConnection ()
    {
        return dataConnection;
    }
}
