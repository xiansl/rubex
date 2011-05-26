package com.googlecode.rubex.server;

import java.util.logging.Logger;

import com.googlecode.rubex.net.Connection;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;
import com.googlecode.rubex.protocol.ProtocolMessage;

public class RubexServerSession
{
    private final static Logger logger =
        Logger.getLogger (RubexServerSession.class.getName ());
    
    private final Connection <ProtocolMessage> connection;
    
    public RubexServerSession (Connection <ProtocolMessage> connection)
    {
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
        
        connection.addMessageListener (new MyMessageListener ());
    }
    
    private synchronized void processMessage (ProtocolMessage message)
    {
        if (message == null)
            throw new IllegalArgumentException ("Message is null");

        System.out.println (message);
    }
    
    private class MyMessageListener implements MessageListener <ProtocolMessage>
    {
        @Override
        public void onMessage (
            MessageEvent <? extends ProtocolMessage> event)
        {
            if (event == null)
                throw new IllegalArgumentException ("Event is null");
            
            processMessage (event.getMessage ());
        }

        @Override
        public void onDisconnect (
            ConnectionEvent <? extends ProtocolMessage> event)
        {
            // Do nothing
        }
    }
}
