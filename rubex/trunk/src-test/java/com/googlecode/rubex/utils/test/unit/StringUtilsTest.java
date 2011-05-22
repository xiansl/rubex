package com.googlecode.rubex.utils.test.unit;


import org.junit.Test;
import static org.junit.Assert.*;

import com.googlecode.rubex.utils.StringUtils;

public class StringUtilsTest
{
    @Test
    public void testEscape () throws Exception
    {
        assertEquals ("", StringUtils.escape (""));
        assertEquals ("foo", StringUtils.escape ("foo"));
        assertEquals ("\\\"\\\'\\\\\\b\\t\\n\\f\\r\\000", StringUtils.escape ("\"\'\\\b\t\n\f\r\0"));
    }
    
    @Test
    public void testUnescape () throws Exception
    {
        assertEquals ("", StringUtils.unescape (""));
        assertEquals ("foo", StringUtils.unescape ("foo"));
        assertEquals ("\"\'\\\b\t\n\f\r\0xyz", StringUtils.unescape ("\\\"\\\'\\\\\\b\\t\\n\\f\\r\\000\\x\\y\\z"));
        
        assertEquals ("foo\\", StringUtils.unescape ("foo\\"));
        
        for (int i = 0; i < 256; i++)
        {
            String octal = "" + (i >> 6 & 7) + (i >> 3 & 7) + (i & 7);
            
            assertEquals ("foo" + (char)i + "bar", StringUtils.unescape ("foo\\" + octal + "bar"));
            assertEquals ("000" + (char)i + "000", StringUtils.unescape ("000\\" + octal + "000"));
            assertEquals ("'" + (char)i + "'", StringUtils.unescape ("'\\" + octal + "\\'"));
            assertEquals ("foo" + (char)i, StringUtils.unescape ("foo\\" + octal));
            assertEquals ("" + (char)i, StringUtils.unescape ("\\" + octal));
        }
        
        for (int i = 32; i < 64; i++)
        {
            String octal = "" + (i >> 3 & 7) + (i & 7);
            
            assertEquals ("foo" + (char)i + "bar", StringUtils.unescape ("foo\\" + octal + "bar"));
            assertEquals ("000" + (char)i + "000", StringUtils.unescape ("000\\" + octal + "000"));
            assertEquals ("'" + (char)i + "'", StringUtils.unescape ("'\\" + octal + "\\'"));
            assertEquals ("foo" + (char)i, StringUtils.unescape ("foo\\" + octal));
            assertEquals ("" + (char)i, StringUtils.unescape ("\\" + octal));
        }
        
        for (int i = 0; i < 64; i++)
        {
            String octal = "" + (i >> 3 & 7) + (i & 7);
            
            assertEquals ("foo" + (char)i + "bar", StringUtils.unescape ("foo\\" + octal + "bar"));
            assertEquals ("'" + (char)i + "'", StringUtils.unescape ("'\\" + octal + "\\'"));
            assertEquals ("foo" + (char)i, StringUtils.unescape ("foo\\" + octal));
            assertEquals ("" + (char)i, StringUtils.unescape ("\\" + octal));
        }
        
        for (int i = 0; i < 8; i++)
        {
            String octal = "" + (i & 7);
            
            assertEquals ("foo" + (char)i + "bar", StringUtils.unescape ("foo\\" + octal + "bar"));
            assertEquals ("'" + (char)i + "'", StringUtils.unescape ("'\\" + octal + "\\'"));
            assertEquals ("foo" + (char)i, StringUtils.unescape ("foo\\" + octal));
            assertEquals ("" + (char)i, StringUtils.unescape ("\\" + octal));
        }
    }
    
    @Test
    public void testIsJavaIdentifier () throws Exception
    {
        assertFalse (StringUtils.isJavaIdentifier (""));
        assertFalse (StringUtils.isJavaIdentifier ("^"));
        assertTrue (StringUtils.isJavaIdentifier ("_"));
        assertTrue (StringUtils.isJavaIdentifier ("f"));
        assertFalse (StringUtils.isJavaIdentifier ("6"));
        assertTrue (StringUtils.isJavaIdentifier ("_6H7d5"));
        assertTrue (StringUtils.isJavaIdentifier ("f5Jg_"));
        assertFalse (StringUtils.isJavaIdentifier ("6FGH"));
        assertFalse (StringUtils.isJavaIdentifier ("^FGH"));
        assertFalse (StringUtils.isJavaIdentifier ("t6FG:H"));
    }
}
