package com.googlecode.rubex.server.event;

import java.util.EventObject;

import com.googlecode.rubex.message.Message;

public class MessageEvent extends EventObject
{
    private final Message message;
    
    public MessageEvent (Object source, Message message)
    {
        super (source);
        
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        this.message = message;
    }
    
    public Message getMessage ()
    {
        return message;
    }
}
