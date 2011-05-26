package com.googlecode.rubex.server;

import java.net.ServerSocket;

import com.googlecode.rubex.net.Connection;
import com.googlecode.rubex.net.ProtocolServerSocketServer;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.ConnectionListener;
import com.googlecode.rubex.protocol.ProtocolMessage;

public class RubexServer
{
    public RubexServer (String [] args) throws Exception
    {
        // Do nothing
    }
    
    public void run () throws Exception
    {
        ProtocolServerSocketServer server =
            new ProtocolServerSocketServer (new ServerSocket (1234));
        
        server.addConnectionListener (new MyConnectionListener ());
        server.start ();
    }
    
    private class MyConnectionListener 
        implements ConnectionListener <ProtocolMessage>
    {
        @Override
        public void onNewConnection (
            ConnectionEvent <ProtocolMessage> event)
        {
            Connection <ProtocolMessage> connection = event.getConnection ();
            
            new RubexServerSession (connection);
        }
    }
    
    public static void main (String [] args) throws Exception
    {
        RubexServer rubexServer = new RubexServer (args);
        rubexServer.run ();
    }
}
