package com.googlecode.rubex.data.test.unit;

import org.junit.Test;

import static org.junit.Assert.*;

import com.googlecode.rubex.data.BinaryDataObject;
import com.googlecode.rubex.data.BooleanDataObject;
import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.data.IntegerDataObject;
import com.googlecode.rubex.data.ListDataObject;
import com.googlecode.rubex.data.NullDataObject;
import com.googlecode.rubex.data.RealDataObject;
import com.googlecode.rubex.data.StringDataObject;
import com.googlecode.rubex.data.StructureDataObject;

public class DataObjectUtilsTest
{
    @Test
    public void testCreateNullDataObject () throws Exception
    {
        NullDataObject nullObject = DataObjectUtils.createNullDataObject ();
        assertNotNull (nullObject);
    }
    
    @Test
    public void testCreateBooleanDataObject () throws Exception
    {
        BooleanDataObject trueObject = DataObjectUtils.createBooleanDataObject (true);
        assertNotNull (trueObject);
        assertEquals (true, trueObject.getBoolean ());
        
        BooleanDataObject falseObject = DataObjectUtils.createBooleanDataObject (false);
        assertNotNull (falseObject);
        assertEquals (false, falseObject.getBoolean ());
    }
    
    @Test
    public void testCreateIntegerDataObject () throws Exception
    {
        IntegerDataObject integerObject = DataObjectUtils.createIntegerDataObject (-1234567890123456789L);
        assertNotNull (integerObject);
        assertEquals (-1234567890123456789L, integerObject.getLong ());
    }
    
    @Test
    public void testCreateRealDataObject () throws Exception
    {
        RealDataObject realDataObject = DataObjectUtils.createRealDataObject (1234567890.123456789);
        assertNotNull (realDataObject);
        assertEquals (Double.doubleToLongBits (1234567890.123456789), Double.doubleToLongBits (realDataObject.getDouble ()));
    }
    
    @Test
    public void testCreateStringDataObject () throws Exception
    {
        StringDataObject stringDataObject = DataObjectUtils.createStringDataObject ("FooBar");
        assertNotNull (stringDataObject);
        assertEquals ("FooBar", stringDataObject.getString ());
    }
    
    @Test
    public void testCreateBinaryDataObject () throws Exception
    {
        BinaryDataObject binaryDataObject = DataObjectUtils.createBinaryDataObject ("Hello, World!".getBytes ("UTF-8"));
        assertNotNull (binaryDataObject);
        assertEquals ("Hello, World!", new String (binaryDataObject.getBytes (), "UTF-8"));
    }
    
    @Test
    public void testCreateListDataObject () throws Exception
    {
        DataObject foo = DataObjectUtils.createStringDataObject ("foo");
        DataObject bar = DataObjectUtils.createStringDataObject ("bar");
        DataObject foobar = DataObjectUtils.createStringDataObject ("foobar");
        
        ListDataObject listDataObject = DataObjectUtils.createListDataObject (
            foo, bar, foobar);
        assertNotNull (listDataObject);
        assertEquals (3, listDataObject.getSize ());
        assertSame (foo, listDataObject.getElementAt (0));
        assertSame (bar, listDataObject.getElementAt (1));
        assertSame (foobar, listDataObject.getElementAt (2));
    }
    
    @Test
    public void testCreateStructureDataObject () throws Exception
    {
        DataObject foo = DataObjectUtils.createStringDataObject ("foo");
        DataObject bar = DataObjectUtils.createStringDataObject ("bar");
        DataObject foobar = DataObjectUtils.createStringDataObject ("foobar");
        
        StructureDataObject structureDataObject = 
            DataObjectUtils.createStructureDataObject (
                new String [] {"foo", "bar", "foobar"}, 
                new DataObject [] {foo, bar, foobar});
        assertNotNull (structureDataObject);
        String [] fieldNames = structureDataObject.getFieldNames ();
        assertEquals (3, fieldNames.length);
        assertEquals ("foo", fieldNames [0]);
        assertEquals ("bar", fieldNames [1]);
        assertEquals ("foobar", fieldNames [2]);
        assertSame (foo, structureDataObject.getField ("foo"));
        assertSame (bar, structureDataObject.getField ("bar"));
        assertSame (foobar, structureDataObject.getField ("foobar"));
    }
    
    @Test
    public void testFormatAsString () throws Exception
    {
        assertEquals (
            "null", 
            DataObjectUtils.createNullDataObject ().toString ());
        assertEquals (
            "true", 
            DataObjectUtils.createBooleanDataObject (true).toString ());
        assertEquals (
            "false", 
            DataObjectUtils.createBooleanDataObject (false).toString ());
        assertEquals (
            "-1234567890123456789", 
            DataObjectUtils.createIntegerDataObject (
                -1234567890123456789L).toString ());
        assertEquals (
            Double.toString (1234567890.123456789), 
            DataObjectUtils.createRealDataObject (
                1234567890.123456789).toString ());
        assertEquals (
            "\"\"", 
            DataObjectUtils.createStringDataObject ("").toString ());
        assertEquals (
            "\"FooBar\"", 
            DataObjectUtils.createStringDataObject ("FooBar").toString ());
        assertEquals (
            "\"\\\\\\\"\\\'\\r\\n\\b\\t\\f\\000\"", 
            DataObjectUtils.createStringDataObject (
                "\\\"\'\r\n\b\t\f\0").toString ());
        assertEquals (
            "[]", 
            DataObjectUtils.createBinaryDataObject (new byte [0]).toString ());
        assertEquals (
            "[00 01 E5 F7 FF]", 
            DataObjectUtils.createBinaryDataObject (
                new byte [] {
                    (byte)0x00, 
                    (byte)0x01, 
                    (byte)0xE5, 
                    (byte)0xF7, 
                    (byte)0xFF}).toString ());
        assertEquals (
            "()", 
            DataObjectUtils.createListDataObject ().toString ());
        assertEquals (
            "(\"Foo\", \"Bar\", \"FooBar\")", 
            DataObjectUtils.createListDataObject (
                DataObjectUtils.createStringDataObject ("Foo"), 
                DataObjectUtils.createStringDataObject ("Bar"), 
                DataObjectUtils.createStringDataObject ("FooBar")).toString ());
        assertEquals (
            "{}", 
            DataObjectUtils.createStructureDataObject (
                new String [0], new DataObject [0]).toString ());
        assertEquals (
            "{foo: \"Foo\", bar: \"Bar\", foobar: \"FooBar\"}", 
            DataObjectUtils.createStructureDataObject (
                new String [] {"foo", "bar", "foobar"},
                new DataObject [] {
                    DataObjectUtils.createStringDataObject ("Foo"), 
                    DataObjectUtils.createStringDataObject ("Bar"), 
                    DataObjectUtils.createStringDataObject ("FooBar")
                }).toString ());
    }
}
