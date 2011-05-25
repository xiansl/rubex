package com.googlecode.rubex.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.message.Message;
import com.googlecode.rubex.message.MessageReader;
import com.googlecode.rubex.message.MessageWriter;

/**
 * Simple implementation of {@link Connection} interface based on {@link Socket}.
 * 
 * @author Mikhail Vladimirov
 */
public class SimpleSocketConnection 
    extends AbstractConnection <Message>
{
    private final static Message KISS_OF_DEATH = new KissOfDeath ();
    
    private final static Logger logger = 
        Logger.getLogger (SimpleSocketConnection.class.getName ());
    
    private final Socket socket;
    private final String name;
    private final Thread receiverThread;
    private final Thread senderThread;
    private final BlockingQueue <Message> sendQueue = 
        new LinkedBlockingQueue <Message> ();
    
    private boolean started = false;
    private boolean shutdown = false;
    
    /**
     * Create new simple socket connection with given socket.
     * 
     * @param socket socket to base on
     */
    public SimpleSocketConnection (Socket socket)
    {
        if (socket == null)
            throw new IllegalArgumentException ("Socket is null");
        
        this.socket = socket;
        name = socket.getInetAddress () + ":" + socket.getPort ();
        receiverThread = new Thread ("Receiver [" + name + "]")
        {
            @Override
            public void run() 
            {
                runReceiverThread ();
            };
        };
        senderThread = new Thread ("Sender [" + name + "]")
        {
            @Override
            public void run() 
            {
                runSenderThread ();
            };
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void sendMessage (Message message)
    {
        if (shutdown)
            throw new IllegalStateException ("Connection is already shutdown");
        
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        if (logger.isLoggable (Level.INFO))
            logger.info ("Sending message: " + name + ": " + messageToString (message));
        
        try
        {
            sendQueue.put (message);
        }
        catch (InterruptedException ex)
        {
            if (logger.isLoggable (Level.SEVERE))
                logger.log (
                    Level.SEVERE, "Cannot put message to send queue: " + name, 
                    ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void start ()
    {
        if (started)
            throw new IllegalStateException ("Connection already started");
        
        if (logger.isLoggable (Level.FINE))
            logger.fine ("Starting connection: " + name);
        
        receiverThread.start ();
        senderThread.start ();
        
        started = true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void shutdown ()
    {
        if (shutdown)
            throw new IllegalStateException ("Connection is already shutdown");
        
        if (logger.isLoggable (Level.INFO))
            logger.info ("Closing socket: " + name);
        
        shutdown = true;
        
        try
        {
            socket.shutdownInput ();
        }
        catch (IOException ex)
        {
            if (logger.isLoggable (Level.WARNING))
                logger.log (Level.WARNING, "Cannot close socket: " + name, ex);
        }
        
        try
        {
            sendQueue.put (KISS_OF_DEATH);
        }
        catch (InterruptedException ex)
        {
            if (logger.isLoggable (Level.SEVERE))
                logger.log (
                    Level.SEVERE, 
                    "Cannot put kiss of death message to send queue: " + name, 
                    ex);
        }
        
        fireOnDisconnect ();
    }
    
    private synchronized boolean isShutdown ()
    {
        return shutdown;
    }
    
    private synchronized void connectionLost ()
    {
        if (!shutdown)
            shutdown ();
    }
    
    private void runReceiverThread ()
    {
        try
        {
            MessageReader messageReader = 
                new MessageReader (socket.getInputStream ());
            
            while (true)
            {
                Message message = messageReader.readMessage ();
                
                if (logger.isLoggable (Level.INFO))
                    logger.info (
                        "Received message: " + name + ": " + 
                        messageToString (message));
                
                fireOnMessage (message);
            }
        }
        catch (IOException ex)
        {
            if (!isShutdown ())
            {
                if (logger.isLoggable (Level.SEVERE))
                    logger.log (
                        Level.SEVERE, "Error while receiving messages: " + name,
                        ex);
            }
        }
        
        connectionLost ();
    }

    private void runSenderThread ()
    {
        try
        {
            MessageWriter writer = new MessageWriter (socket.getOutputStream ());
            
            while (true)
            {
                Message message;
                
                try
                {
                    message = sendQueue.take ();
                }
                catch (InterruptedException ex)
                {
                    if (logger.isLoggable (Level.SEVERE))
                        logger.log (
                            Level.SEVERE, 
                            "Interrupted while taking message from queue: " + 
                            name, ex);
                    
                    break;
                }
                
                if (message == KISS_OF_DEATH)
                {
                    break;
                }
                
                writer.writeMessage (message);
            }
        }
        catch (IOException ex)
        {
            if (!isShutdown ())
            {
                if (logger.isLoggable (Level.SEVERE))
                    logger.log (
                        Level.SEVERE, "Error while sending messages: " + name,
                        ex);
            }
        }
        finally
        {
            try
            {
                socket.close ();
            }
            catch (IOException ex)
            {
                if (logger.isLoggable (Level.WARNING))
                    logger.log (
                        Level.WARNING, "Cannot close socket: " + name, ex);
            }
        }
        
        connectionLost ();
    }
    
    private static String messageToString (Message message)
    {
        int size = message.getSize ();
        byte [] data = new byte [size];
        message.copyData (0, data, 0, size);
        
        try
        {
            return new String (data, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new Error (ex);
        }
    }
    
    private static class KissOfDeath implements Message
    {
        @Override
        public int getSize ()
        {
            throw new UnsupportedOperationException ();
        }

        @Override
        public void copyData (int sourceOffset, byte[] destination,
            int destinationOffset, int length)
        {
            throw new UnsupportedOperationException ();
        }
        
        @Override
        public byte[] getBytes ()
        {
            throw new UnsupportedOperationException ();
        }
    }
}
