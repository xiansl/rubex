package com.googlecode.rubex.net.event;

import java.util.EventObject;

/**
 * Contains details about message-related event.
 * 
 * @param <MessageType> type of the messages
 * 
 * @author Mikhail Vladimirov
 */
public class MessageEvent <MessageType> extends EventObject
{
    private final MessageType message;
    
    /**
     * Create new message event with given event source and message.
     * 
     * @param source event source
     * @param message message
     */
    public MessageEvent (Object source, MessageType message)
    {
        super (source);
        
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        this.message = message;
    }

    /**
     * Return message of this event.
     */
    public MessageType getMessage ()
    {
        return message;
    }
}
