package com.googlecode.rubex.data;

/**
 * Data object that holds structure, i.e. ordered collection fields.  Each 
 * field has a name and value.  All names should be valid Java identifiers,
 * while all values are data objects.
 * 
 * @author Mikhail Vladimirov
 */
public interface StructureDataObject extends DataObject
{
    /**
     * Get all field names.
     * 
     * @return an array of strings
     */
    public String [] getFieldNames ();
    
    /**
     * Tells whether field with given name exists in the structure.
     * 
     * @param name field name
     * @return <code>true</code> if field with given name exists, 
     *         <code>false</code> otherwise.
     */
    public boolean hasField (String name);
    
    /**
     * Get value of the field with given name.
     * 
     * @param name field name
     * @return value of the field
     * @throws IllegalArgumentException if there is no field with given name
     */
    public DataObject getField (String name) throws IllegalArgumentException;
}
