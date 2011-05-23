package com.googlecode.rubex.protocol.event;

import java.util.EventListener;

/**
 * Receives notifications about connection-related events.
 * 
 * @author Mikhail Vladimirov
 */
public interface ConnectionListener extends EventListener
{
    /**
     * Called when new connection was accepted.
     * 
     * @param event new connection event details
     */
    public void onNewConnection (ConnectionEvent event);
}
