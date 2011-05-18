package com.googlecode.rubex.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RubexServer
{
    private final static Logger logger = 
        Logger.getLogger (RubexServer.class.getName ());
    
    private final int port;
    
    private ServerSocket serverSocket = null;
    private Thread acceptorThread = null;
    private volatile boolean stopping = false;
    
    public RubexServer (int port)
    {
        this.port = port;
    }
    
    public void start () throws IOException
    {
        if (acceptorThread != null)
            throw new IllegalStateException ("Server already started");
        
        if (logger.isLoggable (Level.FINE))
            logger.fine ("Creating server socket at port " + port);
        
        serverSocket = new ServerSocket (port);
        
        if (logger.isLoggable (Level.FINE))
            logger.fine ("Starting server acceptor thread");
        
        acceptorThread = new Thread (
            new AcceptorRunnable (), "Server Acceptor Thread");
        acceptorThread.start ();
    }
    
    public void stop () throws InterruptedException
    {
        if (acceptorThread == null)
            throw new IllegalStateException ("Server is not started");

        if (logger.isLoggable (Level.FINE))
            logger.fine ("Closing server socket");
        
        stopping = true;
        
        try
        {
            serverSocket.close ();
        }
        catch (IOException ex)
        {
            ex.printStackTrace ();
        }
        
        acceptorThread.join ();
        
        serverSocket = null;
        acceptorThread = null;
    }

    private void run () throws Exception
    {
        while (true)
        {
            try
            {
                Socket socket = serverSocket.accept ();
                
                if (logger.isLoggable (Level.INFO))
                    logger.info ("Accepted connection from " + socket.getInetAddress () + ":" + socket.getPort ());
                
                socket.getOutputStream ().write ("Hello\n".getBytes ());
                socket.close ();
            }
            catch (IOException ex)
            {
                if (!stopping)
                {
                    if (logger.isLoggable (Level.SEVERE))
                        logger.log (Level.SEVERE, "Error while accepting connection", ex);
                }
                    
                break;
            }
        }
    }

    private class AcceptorRunnable implements Runnable
    {
        public void run ()
        {
            try
            {
                RubexServer.this.run ();
            }
            catch (Exception ex)
            {
                ex.printStackTrace ();
            }
        }
    }
}
