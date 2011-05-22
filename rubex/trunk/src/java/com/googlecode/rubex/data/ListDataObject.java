package com.googlecode.rubex.data;

/**
 * Data object that holds list of data objects.
 * 
 * @author Mikhail Vladimirov
 */
public interface ListDataObject extends DataObject
{
    /**
     * Return side of the list.
     */
    public int getSize ();
    
    /**
     * Get list element by index.
     * 
     * @param index of the element to be returned
     * @return list element with given index
     */
    public DataObject getElementAt (int index);
    
    /**
     * Get all list elements as an array.
     * 
     * @return an array of {@link DataObject} objects
     */
    public DataObject [] getAllElements ();
}
