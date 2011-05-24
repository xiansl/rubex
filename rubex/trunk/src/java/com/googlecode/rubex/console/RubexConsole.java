package com.googlecode.rubex.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.protocol.SimpleDataConnection;
import com.googlecode.rubex.protocol.SimpleSocketConnection;
import com.googlecode.rubex.protocol.event.ConnectionEvent;
import com.googlecode.rubex.protocol.event.MessageEvent;
import com.googlecode.rubex.protocol.event.MessageListener;

public class RubexConsole
{
    public static void main (String [] args) throws Exception
    {
        if (args.length != 2)
        {
            usage ();
            return;
        }
        
        String host = args [0];
        int port = Integer.parseInt (args [1]);
        
        SimpleDataConnection connection = 
            new SimpleDataConnection (
                new SimpleSocketConnection (
                    new Socket (host, port)));
        
        connection.addMessageListener (
            new MessageListener <DataObject> ()
            {
                @Override
                public void onMessage (MessageEvent <? extends DataObject> event)
                {
                    System.out.println (event.getMessage ());
                }

                @Override
                public void onDisconnect (
                    ConnectionEvent <? extends DataObject> event)
                {
                    System.out.println ("DISCONNECTED");
                    System.exit (0);
                }
            });
        
        connection.start ();
        
        BufferedReader reader = new BufferedReader (
            new InputStreamReader (
                System.in));
        String line;
        while ((line = reader.readLine ()) != null)
        {
            DataObject message;
            
            try
            {
                message = DataObjectUtils.parseFromString (line); 
            }
            catch (IllegalArgumentException ex)
            {
                ex.printStackTrace ();
                continue;
            }
            
            connection.sendMessage (message);
        }
    }
    
    private static void usage ()
    {
        System.out.println ("Usage:");
        System.out.println ("\t" + RubexConsole.class.getSimpleName () + " <host> <port>");
    }
}
