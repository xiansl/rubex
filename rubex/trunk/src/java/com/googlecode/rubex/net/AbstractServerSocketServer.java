package com.googlecode.rubex.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;

/**
 * Abstract implementation of {@link Server} interface based on 
 * {@link ServerSocket}.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractServerSocketServer <MessageType>
    extends AbstractServer <MessageType>
{
    private final static Logger logger = 
        Logger.getLogger (AbstractServerSocketServer.class.getName ());
    
    private final ServerSocket serverSocket;
    private final String name;
    private final Thread acceptorThread;
    
    private final List <Connection <?>> connections = 
        new ArrayList <Connection <?>> ();
    
    private final MessageListener <Object> messageListener = 
        new MessageListener <Object> ()
    {
        @Override
        public void onMessage (MessageEvent <? extends Object> event)
        {
            // Do nothing
        }
        
        @Override
        public void onDisconnect (ConnectionEvent <? extends Object> event)
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
    public AbstractServerSocketServer (ServerSocket serverSocket)
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
    @SuppressWarnings ("unchecked")
    @Override
    public synchronized void shutdown ()
    {
        if (!started)
            throw new IllegalStateException ("Server is not started");
        
        if (shutdown)
            throw new IllegalStateException ("Server is already sutdown");
        
        shutdown = true;
        
        Connection <Message> [] connections = 
            (Connection <Message> [])this.connections.toArray (
                new Connection <?> [this.connections.size ()]);
        
        for (Connection <Message> connection: connections)
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
    @SuppressWarnings ("unchecked")
    @Override
    public synchronized Connection <MessageType> [] getAllConnections ()
    {
        return (Connection <MessageType> [])connections.
            toArray (new Connection <?> [connections.size ()]);
    }
    
    /**
     * Create connection for socket.
     * 
     * @param socket socket to create connection for
     * @return {@link Connection <MessageType>} object
     */
    protected abstract Connection <MessageType> createConnection (Socket socket);
    
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
        
        Connection <MessageType> connection = createConnection (socket);
        connection.addMessageListener (messageListener);
        
        connections.add (connection);
        
        fireOnNewConnection (connection);
        
        connection.start ();
    }
    
    private synchronized void disconnected (Connection <?> connection)
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
