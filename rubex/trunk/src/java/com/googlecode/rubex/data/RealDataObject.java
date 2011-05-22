package com.googlecode.rubex.data;

/**
 * Data object that holds double-precision real value.
 * 
 * @author Mikhail Vladimirov
 */
public interface RealDataObject extends DataObject
{
    /**
     * Return real value as float.
     */
    public float getFloat ();
    
    /**
     * Return real value as double.
     */
    public double getDouble ();
}
