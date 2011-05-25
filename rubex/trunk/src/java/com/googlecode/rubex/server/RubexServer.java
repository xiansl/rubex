package com.googlecode.rubex.server;

import java.net.ServerSocket;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.net.Connection;
import com.googlecode.rubex.net.DataServerSocketServer;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.ConnectionListener;

public class RubexServer
{
    public RubexServer (String [] args) throws Exception
    {
        // Do nothing
    }
    
    public void run () throws Exception
    {
        DataServerSocketServer server =
            new DataServerSocketServer (new ServerSocket (1234));
        
        server.addConnectionListener (new MyConnectionListener ());
        server.start ();
    }
    
    private class MyConnectionListener 
        implements ConnectionListener <DataObject>
    {
        @Override
        public void onNewConnection (
            ConnectionEvent <DataObject> event)
        {
            Connection <DataObject> connection = event.getConnection ();
            
            new RubexServerSession (connection);
        }
    }
    
    public static void main (String [] args) throws Exception
    {
        RubexServer rubexServer = new RubexServer (args);
        rubexServer.run ();
    }
}
