package com.googlecode.rubex.net;

import com.googlecode.rubex.net.event.MessageListener;

/**
 * Network connection that allows to send and receive messages.
 * 
 * @param <MessageType> type of the messages to be send and received
 * 
 * @author Mikhail Vladimirov
 */
public interface Connection <MessageType>
{
    /**
     * Add listener to be notified about incoming messages and disconnect.
     * 
     * @param listener listener to be added
     */
    public void addMessageListener (
        MessageListener <? super MessageType> listener);
    
    /**
     * Remove message listener
     * 
     * @param listener listener to be removed
     */
    public void removeMessageListener (
        MessageListener <? super MessageType> listener);
    
    /**
     * Send message via connection
     * 
     * @param message message to be sent
     */
    public void sendMessage (MessageType message);

    /**
     * Start the connection.
     */
    public void start ();
    
    /**
     * Shutdown the connection.
     */
    public void shutdown ();
}
