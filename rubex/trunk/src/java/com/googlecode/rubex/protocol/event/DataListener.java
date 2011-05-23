package com.googlecode.rubex.protocol.event;

/**
 * Receives notifications from data connection about incoming data objects and
 * disconnect.
 * 
 * @author Mikhail Vladimirov
 */
public interface DataListener
{
    /**
     * Called when new data object was received on data connection.
     * 
     * @param event incoming data object event details
     */
    public void onIncomingData (DataEvent event);

    /**
     * Called when data connection was broken.
     * 
     * @param event broken data connection event details
     */
    public void onDisconnect (DataConnectionEvent event);
}
