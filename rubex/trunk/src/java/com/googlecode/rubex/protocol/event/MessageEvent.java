package com.googlecode.rubex.protocol.event;

import java.util.EventObject;

import com.googlecode.rubex.message.Message;

/**
 * Contains details about message-related event.
 * 
 * @author Mikhail Vladimirov
 */
public class MessageEvent extends EventObject
{
    private final Message message;
    
    /**
     * Create new message event with given event source and message.
     * 
     * @param source event source
     * @param message message
     */
    public MessageEvent (Object source, Message message)
    {
        super (source);
        
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        this.message = message;
    }

    /**
     * Return message of this event.
     */
    public Message getMessage ()
    {
        return message;
    }
}
