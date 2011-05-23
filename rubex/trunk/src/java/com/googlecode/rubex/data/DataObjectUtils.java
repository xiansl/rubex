package com.googlecode.rubex.data;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.googlecode.rubex.data.parser.DataObjectParser;
import com.googlecode.rubex.data.parser.ParseException;
import com.googlecode.rubex.utils.StringUtils;

/**
 * Contains various useful classes to work with data objects.
 * 
 * @author Mikhail Vladimirov
 */
public class DataObjectUtils
{
    private final static StringFormattingVisitor STRING_FORMATTING_VISITOR =
        new StringFormattingVisitor ();
    
    private DataObjectUtils ()
    {
        throw new Error ("Do not instantiate me");
    }
    
    /**
     * Format given data object as human-readable string.
     * 
     * @param dataObject data object to format
     * @return human-readable representation of given data object
     */
    public static String formatAsString (DataObject dataObject)
    {
        if (dataObject == null)
            throw new IllegalArgumentException ("Data object is null");
        
        return dataObject.accept (STRING_FORMATTING_VISITOR);
    }
    
    /**
     * Parses human-readable string representation of data object.
     * 
     * @param string human-readable representation of data object
     * @return reconstructed data object
     * @throws IllegalArgumentException if string contents is not a valid 
     *         human-readable representation of data object
     */
    public static DataObject parseFromString (String string)
    {
        if (string == null)
            throw new IllegalArgumentException ("String is null");
        
        DataObjectParser parser = 
            new DataObjectParser (new StringReader (string));
        
        try
        {
            return parser.dataObject ();
        }
        catch (ParseException ex)
        {
            throw new IllegalArgumentException (
                "Invalid string representation of data object");
        }
    }
    
    /**
     * Create null data object.
     * 
     * @return null data object created
     */
    public static NullDataObject createNullDataObject ()
    {
        return MyNullDataObject.INSTANCE;
    }

    /**
     * Create boolean data object with given boolean value.
     * 
     * @param value value for the object
     * @return boolean data object created
     */
    public static BooleanDataObject createBooleanDataObject (boolean value)
    {
        return value ? MyBooleanDataObject.TRUE : MyBooleanDataObject.FALSE;
    }
    
    /**
     * Create integer data object with given long value.
     * 
     * @param value value for the object
     * @return integer data object created
     */
    public static IntegerDataObject createIntegerDataObject (long value)
    {
        return new MyIntegerDataObject (value);
    }
    
    /**
     * Create real data object with given double value.
     * 
     * @param value value for the object
     * @return real data object created
     */
    public static RealDataObject createRealDataObject (double value)
    {
        return new MyRealDataObject (value);
    }
    
    /**
     * Create string data object with given string value.
     * 
     * @param value value for the object
     * @return string data object created
     */
    public static StringDataObject createStringDataObject (String value)
    {
        if (value == null)
            throw new IllegalArgumentException ("Value is null");
        
        return new MyStringDataObject (value);
    }
    
    /**
     * Create binary data object with given binary value.
     * 
     * @param value value for the object
     * @return boolean data object created
     */
    public static BinaryDataObject createBinaryDataObject (byte [] value)
    {
        if (value == null)
            throw new IllegalArgumentException ("Value is null");
        
        return new MyBinaryDataObject (value);
    }
    
    /**
     * Create list data object with given elements.
     * 
     * @param elements an array of list elements
     * @return list data object created
     */
    public static ListDataObject createListDataObject (DataObject ... elements)
    {
        if (elements == null)
            throw new IllegalArgumentException ("Elements is null");
        
        return new MyListDataObject (elements);
    }
    
    /**
     * Create structure data object with given field names and field values.
     * 
     * @param fieldNames an array of field names
     * @param fieldValues an array of field values
     * @return structure data object created
     */
    public static StructureDataObject createStructureDataObject (
        String [] fieldNames, DataObject [] fieldValues)
    {
        if (fieldNames == null)
            throw new IllegalArgumentException ("Field names is null");
        
        if (fieldValues == null)
            throw new IllegalArgumentException ("Field values is null");
        
        if (fieldNames.length != fieldValues.length)
            throw new IllegalArgumentException (
                "Length of field names does not match length of field values");
        
        return new MyStructureDataObject (fieldNames, fieldValues);
    }
    
    private static class StringFormattingVisitor 
        implements DataObjectVisitor <String>
    {
        @Override
        public String visitNullDataObject (NullDataObject nullDataObject)
        {
            return "null";
        }

        @Override
        public String visitBooleanDataObject (
            BooleanDataObject booleanDataObject)
        {
            return Boolean.toString (booleanDataObject.getBoolean ());
        }

        @Override
        public String visitIntegerDataObject (
            IntegerDataObject integerDataObject)
        {
            return Long.toString (integerDataObject.getLong ());
        }

        @Override
        public String visitRealDataObject (RealDataObject realDataObject)
        {
            return Double.toString (realDataObject.getDouble ());
        }

        @Override
        public String visitStringDataObject (StringDataObject stringDataObject)
        {
            return 
                '\"' + 
                StringUtils.escape (stringDataObject.getString ()) + 
                '\"';
        }

        @Override
        public String visitBinaryDataObject (BinaryDataObject binaryDataObject)
        {
            byte [] bytes = binaryDataObject.getBytes ();
            
            StringBuffer result = new StringBuffer (bytes.length * 3 + 2);
            
            result.append ('[');
            for (byte b: bytes)
            {
                if (result.length () > 1)
                    result.append (' ');
                
                result.append ("0123456789ABCDEF".charAt ((b >> 4) & 0x0F));
                result.append ("0123456789ABCDEF".charAt (b & 0x0F));
            }
            result.append (']');
            
            return result.toString ();
        }

        @Override
        public String visitListDataObject (ListDataObject listDataObject)
        {
            StringBuffer result = new StringBuffer ();
            
            result.append ('(');
            int size = listDataObject.getSize ();
            for (int i = 0; i < size; i++)
            {
                if (result.length () > 1)
                    result.append (", ");
                
                result.append (listDataObject.getElementAt (i).accept (this));
            }
            result.append (')');
            
            return result.toString ();
        }

        @Override
        public String visitStructureDataObject (
            StructureDataObject structureDataObject)
        {
            StringBuffer result = new StringBuffer ();
            
            result.append ('{');
            String [] fieldNames = structureDataObject.getFieldNames ();
            for (String fieldName: fieldNames)
            {
                if (result.length () > 1)
                    result.append (", ");
                
                result.append (fieldName);
                result.append (": ");
                result.append (
                    structureDataObject.getField (fieldName).accept (this));
            }
            result.append ('}');
            
            return result.toString ();
        }
    }
    
    private static class MyNullDataObject extends AbstractNullDataObject
    {
        public final static MyNullDataObject INSTANCE =
            new MyNullDataObject ();
        
        private MyNullDataObject ()
        {
            // Do nothing
        }
    }
    
    private static class MyBooleanDataObject 
        extends AbstractBooleanDataObject
    {
        public final static MyBooleanDataObject TRUE =
            new MyBooleanDataObject (true);
        
        public final static MyBooleanDataObject FALSE = 
            new MyBooleanDataObject (false);
        
        private final boolean value;
        
        private MyBooleanDataObject (boolean value)
        {
            this.value = value;
        }
        
        @Override
        public boolean getBoolean ()
        {
            return value;
        }
    }
    
    private static class MyIntegerDataObject extends AbstractIntegerDataObject
    {
        private final long value;
        
        public MyIntegerDataObject (long value)
        {
            this.value = value; 
        }

        @Override
        public int getInt () throws UnsupportedOperationException
        {
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE)
                throw new UnsupportedOperationException (
                    "Cannot convert value to int: " + value);
            
            return (int)value;
        }

        @Override
        public long getLong ()
        {
            return value;
        }
    }
    
    private static class MyRealDataObject extends AbstractRealDataObject
    {
        private final double value;
        
        public MyRealDataObject (double value)
        {
            this.value = value; 
        }
        
        @Override
        public float getFloat ()
        {
            return (float)value;
        }
        
        @Override
        public double getDouble ()
        {
            return value;
        }
    }
    
    private static class MyStringDataObject extends AbstractStringDataObject
    {
        private final String value;
        
        public MyStringDataObject (String value)
        {
            if (value == null)
                throw new IllegalArgumentException ("Value is null");
            
            this.value = value; 
        }
        
        @Override
        public String getString ()
        {
            return value;
        }
    }
    
    private static class MyBinaryDataObject extends AbstractBinaryDataObject
    {
        private final byte [] value;
        
        public MyBinaryDataObject (byte [] value)
        {
            if (value == null)
                throw new IllegalArgumentException ("Value is null");
            
            this.value = value.clone (); 
        }

        @Override
        public int getLength ()
        {
            return value.length;
        }

        @Override
        public void copyTo (int sourceOffset, byte[] destination,
            int destinationOffset, int length)
        {
            if (sourceOffset < 0)
                throw new IllegalArgumentException ("Source offset < 0");
            
            if (destination == null)
                throw new IllegalArgumentException ("Destination is null");
            
            if (destinationOffset < 0)
                throw new IllegalArgumentException ("Destination offset < 0");
            
            if (length < 0)
                throw new IllegalArgumentException ("Length < 0");
            
            if (sourceOffset + length > value.length)
                throw new IllegalArgumentException (
                    "Source offset + length > source length");
            
            if (destinationOffset + length > destination.length)
                throw new IllegalArgumentException (
                    "Destination offset + length > destination length");
            
            System.arraycopy (
                value, sourceOffset, destination, destinationOffset, length);
        }

        @Override
        public byte[] getBytes ()
        {
            return value.clone ();
        }
    }
    
    private static class MyListDataObject extends AbstractListDataObject
    {
        private final DataObject [] elements;
        
        public MyListDataObject (DataObject ... elements)
        {
            if (elements == null)
                throw new IllegalArgumentException ("Elements is null");
            
            this.elements = elements.clone ();
            if (Arrays.asList (this.elements).indexOf (null) >= 0)
                throw new IllegalArgumentException ("Null element");
        }

        @Override
        public int getSize ()
        {
            return elements.length;
        }

        @Override
        public DataObject getElementAt (int index)
        {
            if (index < 0 || index >= elements.length)
                throw new IllegalArgumentException ("Invalid index: " + index);
            
            return elements [index];
        }

        @Override
        public DataObject[] getAllElements ()
        {
            return elements.clone ();
        }
    }
    
    private static class MyStructureDataObject 
        extends AbstractStructureDataObject
    {
        private final HashMap <String, DataObject> fields =
            new LinkedHashMap <String, DataObject> ();
        
        public MyStructureDataObject (
            String [] fieldNames, DataObject [] fieldValues)
        {
            if (fieldNames == null)
                throw new IllegalArgumentException ("Field names is null");
            
            if (fieldValues == null)
                throw new IllegalArgumentException ("Field values is null");
            
            int count = fieldNames.length;
            
            if (count != fieldValues.length)
                throw new IllegalArgumentException (
                    "Length of field names does not match " + 
                    "length of field values");
            
            for (int i = 0; i < count; i++)
            {
                String fieldName = fieldNames [i];
                DataObject fieldValue = fieldValues [i];
                
                if (fieldName == null)
                    throw new IllegalArgumentException (
                        "Field name #" + i + " is null");
                
                if (!StringUtils.isJavaIdentifier (fieldName))
                    throw new IllegalArgumentException (
                        "Field name is not a valid Java identifier: " + 
                        fieldName);
                        
                if (fields.containsKey (fieldName))
                    throw new IllegalArgumentException (
                        "Duplicate field name: " + fieldName);
                
                if (fieldValue == null)
                    throw new IllegalArgumentException ("Field value is null");
                
                fields.put (fieldName, fieldValue);
            }
        }

        @Override
        public String[] getFieldNames ()
        {
            return fields.keySet ().toArray (new String [fields.size ()]);
        }

        @Override
        public boolean hasField (String name)
        {
            if (name == null)
                throw new IllegalArgumentException ("Name is null");
            
            return fields.containsKey (name);
        }
        
        @Override
        public DataObject getField (String name) throws IllegalArgumentException
        {
            if (name == null)
                throw new IllegalArgumentException ("Name is null");
            
            if (fields.containsKey (name))
                return fields.get (name);
            else throw new IllegalArgumentException (
                "No field with sich name: " + name);
        }
    }
}
