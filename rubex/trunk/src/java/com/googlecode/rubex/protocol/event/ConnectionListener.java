package com.googlecode.rubex.protocol.event;

import java.util.EventListener;

/**
 * Receives notifications about connection-related events.
 * 
 * @param <MessageType> type of connection messages
 * 
 * @author Mikhail Vladimirov
 */
public interface ConnectionListener <MessageType> 
    extends EventListener
{
    /**
     * Called when new connection was accepted.
     * 
     * @param event new connection event details
     */
    public void onNewConnection (
        ConnectionEvent <MessageType> event);
}
