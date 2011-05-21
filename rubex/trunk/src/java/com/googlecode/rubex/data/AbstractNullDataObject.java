package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link NullDataObject} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractNullDataObject 
    extends AbstractDataObject implements NullDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.NULL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitNullDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof NullDataObject)
            return true;
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        return 17;
    }
}
