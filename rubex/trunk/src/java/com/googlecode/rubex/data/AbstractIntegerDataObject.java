package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link IntegerDataObject} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractIntegerDataObject 
    extends AbstractDataObject implements IntegerDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.INTEGER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitIntegerDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof IntegerDataObject)
        {
            IntegerDataObject other = (IntegerDataObject)obj;
            
            return getLong () == other.getLong ();
        }
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        long value = getLong ();
        
        return (int)(value ^ (value >>> 32));
    }
}
