package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link StructureDataObject} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractStructureDataObject 
    extends AbstractDataObject implements StructureDataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectType getType ()
    {
        return DataObjectType.STRUCTURE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (DataObjectVisitor <T> visitor)
    {
        return visitor.visitStructureDataObject (this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj)
    {
        if (obj instanceof StructureDataObject)
        {
            StructureDataObject other = (StructureDataObject)obj;
            
            String [] fieldNames = getFieldNames ();
            int fieldCount = fieldNames.length;
            
            String [] otherFieldNames = other.getFieldNames ();
            if (fieldCount != otherFieldNames.length)
                return false;
            
            for (int i = 0; i < fieldCount; i++)
            {
                String fieldName = fieldNames [i];
                
                if (!fieldName.equals (otherFieldNames [i]))
                    return false;
                
                if (!getField (fieldName).equals (other.getField (fieldName)))
                    return false;
            }
            
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
        int hashCode = 1;
        String [] fieldNames = getFieldNames ();
        for (String fieldName: fieldNames)
        {
            hashCode = hashCode * 31 + fieldName.hashCode ();
            hashCode = hashCode * 31 + getField (fieldName).hashCode ();
        }
        
        return hashCode;
    }
}
