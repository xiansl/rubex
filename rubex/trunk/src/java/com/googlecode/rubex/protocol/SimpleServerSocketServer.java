package com.googlecode.rubex.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.protocol.event.ConnectionEvent;
import com.googlecode.rubex.protocol.event.MessageEvent;
import com.googlecode.rubex.protocol.event.MessageListener;

/**
 * Simple implementation of {@link Server} interface based on 
 * {@link ServerSocket}.
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleServerSocketServer extends AbstractServer
{
    private final static Logger logger = 
        Logger.getLogger (SimpleServerSocketServer.class.getName ());
    
    private final ServerSocket serverSocket;
    private final String name;
    private final Thread acceptorThread;
    
    private final List <Connection> connections = 
        new ArrayList <Connection> ();
    
    private final MessageListener messageListener = new MessageListener()
    {
        @Override
        public void onMessage (MessageEvent event)
        {
            // Do nothing
        }
        
        @Override
        public void onDisconnect (ConnectionEvent event)
        {
            disconnected (event.getConnection ());
        }
    };
    
    private boolean started = false;
    private boolean shutdown = false;
    
    /**
     * Create new simple server socket server with given server socket.
     * 
     * @param serverSocket server socket to base on
     */
    public SimpleServerSocketServer (ServerSocket serverSocket)
    {
        if (serverSocket == null)
            throw new IllegalArgumentException ("Server socket is null");
        
        this.serverSocket = serverSocket;
        name = serverSocket.getInetAddress () + ":" + 
            serverSocket.getLocalPort ();
        acceptorThread = new Thread (
            "Acceptor [" + name + "]")
        {
            @Override
            public void run() 
            {
                runAcceptorThread ();
            };
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void start ()
    {
        if (started)
            throw new IllegalStateException ("Server already started");
        
        if (logger.isLoggable (Level.INFO))
            logger.info ("Starting server acceptor thread: " + name);
        
        acceptorThread.start ();
        
        started = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void shutdown ()
    {
        if (!started)
            throw new IllegalStateException ("Server is not started");
        
        if (shutdown)
            throw new IllegalStateException ("Server is already sutdown");
        
        shutdown = true;
        
        Connection [] connections = this.connections.toArray (
            new Connection [this.connections.size ()]);
        
        for (Connection connection: connections)
        {
            try
            {
                connection.shutdown ();
            }
            catch (IllegalStateException ex)
            {
                // Ignore
            }
        }
        
        if (logger.isLoggable (Level.INFO))
            logger.info ("Closing server socket: " + name);
        
        try
        {
            serverSocket.close ();
        }
        catch (IOException ex)
        {
            if (logger.isLoggable (Level.WARNING))
                logger.log (Level.WARNING, "Cannot close server socket: " + name, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Connection[] getAllConnections ()
    {
        return connections.toArray (new Connection [connections.size ()]);
    }
    
    private synchronized boolean isShutdown ()
    {
        return shutdown;
    }
    
    private synchronized void newConnection (Socket socket)
    {
        if (logger.isLoggable (Level.INFO))
            logger.info (
                "New connection accepted from " + 
                socket.getInetAddress () + ":" + socket.getPort () + ": " + 
                name);
        
        Connection connection = new SimpleSocketConnection (socket);
        connection.addMessageListener (messageListener);
        
        connections.add (connection);
        
        fireOnNewConnection (connection);
        
        connection.start ();
    }
    
    private synchronized void disconnected (Connection connection)
    {
        connections.remove (connection);
    }
    
    private void runAcceptorThread ()
    {
        while (true)
        {
            try
            {
                Socket socket = serverSocket.accept ();
                newConnection (socket);
            }
            catch (IOException ex)
            {
                if (!isShutdown ())
                {
                    if (logger.isLoggable (Level.SEVERE))
                        logger.log (Level.SEVERE, "Cannot accept connection: " + name, ex);
                }
                
                return;
            }
        }
    }
}
