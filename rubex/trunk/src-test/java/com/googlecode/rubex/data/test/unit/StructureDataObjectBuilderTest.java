package com.googlecode.rubex.data.test.unit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.data.DataObjectUtils;
import com.googlecode.rubex.data.StructureDataObjectBuilder;

public class StructureDataObjectBuilderTest
{
    @Test
    public void testAddField () throws Exception
    {
        assertSame (builder, builder.addNullField ("null"));
        assertSame (builder, builder.addBooleanField ("boolean", true));
        assertSame (
            builder, builder.addIntegerField ("integer", -1234567890123456789L));
        assertSame (
            builder, builder.addRealField ("real", 1234567890.123456789));
        assertSame (builder, builder.addStringField ("string", "foo"));
        assertSame (builder, builder.addBinaryField (
            "binary", 
            new byte [] {
                (byte)0x00, (byte)0x01, (byte)0xE5, (byte)0xF7, (byte)0xFF}));
        assertSame (builder, builder.addListField (
            "list",
            DataObjectUtils.createStringDataObject ("foo"), 
            DataObjectUtils.createStringDataObject ("bar"), 
            DataObjectUtils.createStringDataObject ("foobar")));
        assertSame (builder, builder.addStructureField (
            "structure",
            new String [] {"foo", "bar", "foobar"},
            new DataObject [] {
                DataObjectUtils.createStringDataObject ("foo"), 
                DataObjectUtils.createStringDataObject ("bar"), 
                DataObjectUtils.createStringDataObject ("foobar")
            }));
        assertSame (
            builder, 
            builder.addField (
                "field", 
                DataObjectUtils.createStringDataObject ("foobar")));
        
        assertEquals (
            "{null: null, " + 
            "boolean: true, " + 
            "integer: -1234567890123456789, " + 
            "real: " + Double.toString (1234567890.123456789)+ ", " + 
            "string: \"foo\", " +
            "binary: [00 01 E5 F7 FF], " + 
            "list: (\"foo\", \"bar\", \"foobar\"), " + 
            "structure: {foo: \"foo\", bar: \"bar\", foobar: \"foobar\"}, " + 
            "field: \"foobar\"}", 
            builder.getStructureDataObject ().toString ());
    }
    
    @Test
    public void testReset () throws Exception
    {
        assertEquals ("{}", builder.getStructureDataObject ().toString ());
        builder.addStringField ("foo", "bar");
        assertEquals (
            "{foo: \"bar\"}", builder.getStructureDataObject ().toString ());
        builder.reset ();
        assertEquals ("{}", builder.getStructureDataObject ().toString ());
    }
    
    private StructureDataObjectBuilder builder;

    @Before
    public void setUp () throws Exception
    {
        builder = new StructureDataObjectBuilder ();
    }

    @After
    public void tearDown () throws Exception
    {
        builder = null;
    }
}
