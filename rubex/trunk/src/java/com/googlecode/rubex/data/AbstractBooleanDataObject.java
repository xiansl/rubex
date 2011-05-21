package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link BooleanDataObject} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractBooleanDataObject 
    extends AbstractDataObject implements BooleanDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.BOOLEAN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitBooleanDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof BooleanDataObject)
        {
            BooleanDataObject other = (BooleanDataObject)obj;
            
            return getBoolean () == other.getBoolean ();
        }
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        return getBoolean () ? 1231 : 1237;
    }
}
