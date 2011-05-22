package com.googlecode.rubex.data;

/**
 * Data object that holds a binary string, i.e. sequence of bytes.
 * 
 * @author Mikhail Vladimirov
 */
public interface BinaryDataObject extends DataObject
{
    /**
     * Return length of the binary string.
     */
    public int getLength ();
    
    /**
     * Copy part of the binary str4ing into given byte array.
     * 
     * @param sourceOffset offset in binary string to start copying from
     * @param destination byte array to copy into
     * @param destinationOffset offset in the destination to start copying at
     * @param length length of the part of binary string to be copied
     */
    public void copyTo (int sourceOffset, byte [] destination, int destinationOffset, int length);
    
    /**
     * Return the whole binary string as an array of bytes.
     */
    public byte [] getBytes ();
}
