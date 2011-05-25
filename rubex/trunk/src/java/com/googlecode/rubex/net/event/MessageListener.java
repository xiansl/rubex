package com.googlecode.rubex.net.event;

/**
 * Receives notifications from connection about incoming messages and 
 * disconnect.
 * 
 * @param <MessageType> type of the incoming messages
 * 
 * @author Mikhail Vladimirov
 */
public interface MessageListener <MessageType>
{
    /**
     * Called when new message was received on connection.
     * 
     * @param event new message event details
     */
    public void onMessage (MessageEvent <? extends MessageType> event);
    
    /**
     * Called when connection was broken.
     * 
     * @param event broken connection event details
     */
    public void onDisconnect (ConnectionEvent <? extends MessageType> event);
}
