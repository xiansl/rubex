package com.googlecode.rubex.message;

public interface Message
{
    public int getSize ();
    
    public void copyData (int sourceOffset, byte [] destination, int destinationOffset, int length);
}
