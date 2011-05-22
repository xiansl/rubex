package com.googlecode.rubex.data;

/**
 * Data object that holds 64-bit signed integer value.
 * 
 * @author Mikhail Vladimirov
 */
public interface IntegerDataObject extends DataObject
{
    /**
     * Get integer value as int.
     * 
     * @return integer value as int
     * @throws UnsupportedOperationException if integer value does not fit into
     *         int
     */
    public int getInt () throws UnsupportedOperationException;
    
    /**
     * return integer value as long.
     */
    public long getLong ();
}
