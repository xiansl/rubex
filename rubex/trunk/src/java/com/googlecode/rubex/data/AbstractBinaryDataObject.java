package com.googlecode.rubex.data;

import java.util.Arrays;

/**
 * Abstract base class for implementations of {@link BinaryDataObject} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractBinaryDataObject 
    extends AbstractDataObject implements BinaryDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.BINARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitBinaryDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof BinaryDataObject)
        {
            BinaryDataObject other = (BinaryDataObject)obj;
            
            return Arrays.equals (getBytes (), other.getBytes ());
        }
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        return Arrays.hashCode (getBytes());
    }
}
