package com.googlecode.rubex.protocol;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.protocol.event.DataListener;

/**
 * Network connection that allows to send and receive data objects.
 * 
 * @author Mikhail Vladimirov
 */
public interface DataConnection
{
    /**
     * Add listener to be notified about incoming data and disconnect.
     * 
     * @param listener listener to be added
     */
    public void addDataListener (DataListener listener);
    
    /**
     * Remove data message listener
     * 
     * @param listener listener to be removed
     */
    public void removeDataListener (DataListener listener);
    
    /**
     * Send data object via connection
     * 
     * @param message message to be sent
     */
    public void sendData (DataObject data);

    /**
     * Start the connection.
     */
    public void start ();
    
    /**
     * Shutdown the connection.
     */
    public void shutdown ();
}
