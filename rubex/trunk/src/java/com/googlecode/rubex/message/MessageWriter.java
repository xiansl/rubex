package com.googlecode.rubex.message;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageWriter
{
    public final static long MAGIC = 0x4D4553534147453EL;
    public final static long EOM_SIGN = 0x3C4547415353454DL;
    
    private final DataOutputStream dataOutput;
    
    public MessageWriter (OutputStream output)
    {
        if (output == null)
            throw new IllegalArgumentException ("Output is null");
        
        dataOutput = new DataOutputStream (
            new BufferedOutputStream (output));
    }
    
    public void writeMessage (Message message) throws IOException
    {
        if (message == null)
            throw new IllegalArgumentException ("Message is null");
        
        int size = message.getSize ();
        byte [] buffer = new byte [size];
        message.copyData (0, buffer, 0, size);
        
        dataOutput.writeLong (MAGIC);
        dataOutput.writeInt (size);
        dataOutput.write (buffer);
        dataOutput.writeLong (EOM_SIGN);
        dataOutput.flush ();
    }
}
