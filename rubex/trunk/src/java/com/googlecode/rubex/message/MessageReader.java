package com.googlecode.rubex.message;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;

public class MessageReader
{
    private final DataInputStream dataInput;
    
    public MessageReader (InputStream input)
    {
        if (input == null)
            throw new IllegalArgumentException ("Input stream is null");
        
        dataInput = new DataInputStream (new BufferedInputStream (input));
    }
    
    public Message readMessage () throws IOException
    {
        long magic = dataInput.readLong ();
        if (magic != MessageWriter.MAGIC)
            throw new StreamCorruptedException ("Invalid magic number");
        
        int length = dataInput.readInt ();
        if (length < 0)
            throw new StreamCorruptedException ("Message length is negative");
        
        byte [] result = new byte [length];
        dataInput.read (result);
        return new MyMessage (result);
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
