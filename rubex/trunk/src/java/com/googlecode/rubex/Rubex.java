package com.googlecode.rubex;

import java.net.ServerSocket;
import java.util.logging.LogManager;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.server.SimpleServerSocketServer;
import com.googlecode.rubex.server.event.ConnectionEvent;
import com.googlecode.rubex.server.event.ConnectionListener;

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
                event.getConnection ().sendMessage (new MyMessage ("Hello, World".getBytes ()));
            }
        });
        
        server.start ();
        Thread.sleep (10000);
        server.shutdown ();
    }
    
    private static class MyMessage implements Message
    {
        private final byte [] data;
        
        public MyMessage (byte [] data)
        {
            if (data == null)
                throw new IllegalArgumentException ("Data is null");
            
            this.data = data.clone ();
        }

        @Override
        public int getSize ()
        {
            return data.length;
        }

        @Override
        public void copyData (int sourceOffset, byte[] destination,
            int destinationOffset, int length)
        {
            if (sourceOffset < 0)
                throw new IllegalArgumentException ("Source offset < 0");
            
            if (destination == null)
                throw new IllegalArgumentException ("Destination is null");
            
            if (destinationOffset < 0)
                throw new IllegalArgumentException ("Destination offset < 0");
            
            if (length < 0)
                throw new IllegalArgumentException ("Length < 0");
            
            int size = data.length;
            
            if (sourceOffset + length > size)
                throw new IllegalArgumentException ("Source offset + length > size");
            
            int destinationSize = destination.length;
            
            if (destinationOffset + length > destinationSize)
                throw new IllegalArgumentException ("Destination offset + length > destination size");
            
            System.arraycopy (data, sourceOffset, destination, destinationOffset, length);
        }
    }
}
