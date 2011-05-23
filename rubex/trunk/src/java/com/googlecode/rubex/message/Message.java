package com.googlecode.rubex.message;

/**
 * Message that can be sent or received via connection.
 * 
 * @author Mikhail Vladimirov
 */
public interface Message
{
    /**
     * Return size of the message in bytes.
     */
    public int getSize ();

    /**
     * Copy part of the message into given array of bytes.
     * 
     * @param sourceOffset offset in the message to start copying from
     * @param destination array of bytes to copy to
     * @param destinationOffset offset within destination to start copying at
     * @param length of message part to copy
     */
    public void copyData (int sourceOffset, byte [] destination, int destinationOffset, int length);
    
    /**
     * Get the whole message as array of bytes.
     * 
     * @return an array of bytes
     */
    public byte [] getBytes ();
}
