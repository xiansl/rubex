package com.googlecode.rubex.server;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.server.event.MessageListener;

/**
 * Network connection that allows to send and receive messages.
 * 
 * @author Mikhail Vladimirov
 */
public interface Connection
{
    /**
     * Add listener to be notified about incoming messages and disconnect.
     * 
     * @param listener listener to be added
     */
    public void addMessageListener (MessageListener listener);
    
    /**
     * Remove message listener
     * 
     * @param listener listener to be removed
     */
    public void removeMessageListener (MessageListener listener);
    
    /**
     * Send message via connection
     * 
     * @param message message to be sent
     */
    public void sendMessage (Message message);

    /**
     * Start the connection.
     */
    public void start ();
    
    /**
     * Shutdown the connection.
     */
    public void shutdown ();
}
