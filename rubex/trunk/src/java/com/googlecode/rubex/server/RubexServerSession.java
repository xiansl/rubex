package com.googlecode.rubex.server;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.protocol.Connection;
import com.googlecode.rubex.protocol.event.ConnectionEvent;
import com.googlecode.rubex.protocol.event.MessageEvent;
import com.googlecode.rubex.protocol.event.MessageListener;

public class RubexServerSession
{
    private final Connection <DataObject> connection;
    
    public RubexServerSession (Connection <DataObject> connection)
    {
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.connection = connection;
        
        connection.addMessageListener (new MyMessageListener ());
    }
    
    private synchronized void processMessage (DataObject dataObject)
    {
        if (dataObject == null)
            throw new IllegalArgumentException ("Data object is null");

        System.out.println (dataObject);
    }
    
    private class MyMessageListener implements MessageListener <DataObject>
    {
        @Override
        public void onMessage (MessageEvent <? extends DataObject> event)
        {
            if (event == null)
                throw new IllegalArgumentException ("Event is null");
            
            processMessage (event.getMessage ());
        }

        @Override
        public void onDisconnect (ConnectionEvent <? extends DataObject> event)
        {
            // Do nothing
        }
    }
}
