package com.googlecode.rubex;

import java.net.ServerSocket;
import java.util.logging.LogManager;

import com.googlecode.rubex.data.StructureDataObjectBuilder;
import com.googlecode.rubex.protocol.DataConnection;
import com.googlecode.rubex.protocol.SimpleDataConnection;
import com.googlecode.rubex.protocol.SimpleServerSocketServer;
import com.googlecode.rubex.protocol.event.ConnectionEvent;
import com.googlecode.rubex.protocol.event.ConnectionListener;

public class Rubex
{
    public static void main (String[] args) throws Exception
    {
        LogManager.getLogManager ().readConfiguration (
            Rubex.class.getResourceAsStream ("logging.properties"));
        
        SimpleServerSocketServer server = new SimpleServerSocketServer (new ServerSocket (1234));
        
        server.addConnectionListener (new ConnectionListener()
        {
            @Override
            public void onNewConnection (ConnectionEvent event)
            {
                DataConnection dataConnection =
                    new SimpleDataConnection (event.getConnection ());
                
                dataConnection.sendData (
                    new StructureDataObjectBuilder ().
                        addStringField ("messageType", "ORDER").
                        addStringField ("symbol", "BTC").
                        getStructureDataObject ());
                
                dataConnection.shutdown ();
            }
        });
        
        server.start ();
        Thread.sleep (10000);
        server.shutdown ();
    }
}
