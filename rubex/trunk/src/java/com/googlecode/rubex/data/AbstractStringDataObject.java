package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link StringDataObject} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractStringDataObject 
    extends AbstractDataObject implements StringDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.STRING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitStringDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof StringDataObject)
        {
            StringDataObject other = (StringDataObject)obj;
            
            return getString ().equals (other.getString ());
        }
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        return getString ().hashCode ();
    }
}
