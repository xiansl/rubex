package com.googlecode.rubex.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.rubex.utils.StringUtils;

/**
 * Helps building structure data objects.
 * 
 * @author Mikhail Vladimirov
 */
public class StructureDataObjectBuilder
{
    private final Map <String, DataObject> fields =
        new LinkedHashMap <String, DataObject> ();
    
    /**
     * Reset builder by removing all fields from it.
     * 
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder reset ()
    {
        fields.clear ();
        
        return this;
    }
    
    /**
     * Add new field with null data object value.
     * 
     * @param name field name
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addNullField (String name)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createNullDataObject ());
        
        return this;
    }
    
    /**
     * Add new field with boolean data object value.
     * 
     * @param name field name
     * @param value boolean field value
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addBooleanField (
        String name, boolean value)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createBooleanDataObject (value));
        
        return this;
    }
    
    /**
     * Add new field with integer data object value.
     * 
     * @param name field name
     * @param value long field value
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addIntegerField (
        String name, long value)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createIntegerDataObject (value));
        
        return this;
    }
    
    /**
     * Add new field with real data object value.
     * 
     * @param name field name
     * @param value double field value
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addRealField (
        String name, double value)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createRealDataObject (value));
        
        return this;
    }
    
    /**
     * Add new field with string data object value.
     * 
     * @param name field name
     * @param value string field value
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addStringField (
        String name, String value)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (value == null)
            throw new IllegalArgumentException ("Value is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createStringDataObject (value));
        
        return this;
    }
    
    /**
     * Add new field with binary data object value.
     * 
     * @param name field name
     * @param value binary string value
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addBinaryField (
        String name, byte [] value)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (value == null)
            throw new IllegalArgumentException ("Value is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createBinaryDataObject (value));
        
        return this;
    }
    
    /**
     * Add new field with list data object value.
     * 
     * @param name field name
     * @param elements an array of list elements
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addListField (
        String name, DataObject ... elements)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (elements == null)
            throw new IllegalArgumentException ("Elements is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        addField (name, DataObjectUtils.createListDataObject (elements));
        
        return this;
    }
    
    /**
     * Add new field with structure data object value.
     * 
     * @param name field name
     * @param fieldNames an array string with field names
     * @param fieldValues an array of {@link DataObject} objects with field 
     *        values
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addStructureField (
        String name, String [] fieldNames, DataObject [] fieldValues)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");
        
        if (fieldNames == null)
            throw new IllegalArgumentException ("Field names is null");
        
        if (fieldValues == null)
            throw new IllegalArgumentException ("Field values is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        if (fieldNames.length != fieldValues.length)
            throw new IllegalArgumentException (
                "Length of field names does not match length of field values");
        
        addField (
            name, 
            DataObjectUtils.createStructureDataObject (
                fieldNames, fieldValues));
        
        return this;
    }
    
    /**
     * Add new field with given value.
     * 
     * @param name field name
     * @param value field value
     * @return this structure data object builder
     */
    public StructureDataObjectBuilder addField (String name, DataObject value)
    {
        if (name == null)
            throw new IllegalArgumentException ("Name is null");

        if (value == null)
            throw new IllegalArgumentException ("Value is null");
        
        if (!StringUtils.isJavaIdentifier (name))
            throw new IllegalArgumentException (
                "Name is not a valid Java identifier");
        
        if (fields.containsKey (name))
            throw new IllegalStateException (
                "Field with such name already exists: " + name);
        
        fields.put (name, value);
        
        return this;
    }
    
    /**
     * Return structure data object that was built.
     */
    public StructureDataObject getStructureDataObject ()
    {
        int size = fields.size ();
        return DataObjectUtils.createStructureDataObject (
            fields.keySet ().toArray (new String [size]), 
            fields.values ().toArray (new DataObject [size]));
    }
}
