package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link ListDataObject} interface.
 */
public abstract class AbstractListDataObject 
    extends AbstractDataObject implements ListDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.LIST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitListDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof ListDataObject)
        {
            ListDataObject other = (ListDataObject)obj;
            
            int size = getSize ();
            if (size != other.getSize ())
                return false;
            
            for (int i = 0; i < size; i++)
                if (!getElementAt (i).equals (other.getElementAt (i)))
                    return false;
            
            return true;
        }
        else return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        int result = 1;
        int size = getSize ();
        for (int i = 0; i < size; i++)
            result = result * 31 + getElementAt (i).hashCode ();
        
        return result;
    }
}
