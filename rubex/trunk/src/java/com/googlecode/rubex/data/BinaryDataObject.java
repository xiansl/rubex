package com.googlecode.rubex.data;

public interface BinaryDataObject extends DataObject
{
    public int getLength ();
    public void copyTo (int sourceOffset, byte [] destination, int destinationOffset, int length);
    public byte [] getBytes ();
}
