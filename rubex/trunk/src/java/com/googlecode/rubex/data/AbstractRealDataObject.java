package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link RealDataObject} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractRealDataObject 
    extends AbstractDataObject implements RealDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.REAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitRealDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof RealDataObject)
        {
            RealDataObject other = (RealDataObject)obj;
            
            return Double.doubleToLongBits (
                getDouble ()) == Double.doubleToLongBits (other.getDouble ());
        }
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        long bits = Double.doubleToLongBits (getDouble ());
        return (int)(bits ^ (bits >>> 32));
    }
}
