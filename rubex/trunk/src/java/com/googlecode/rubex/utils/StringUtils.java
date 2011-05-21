package com.googlecode.rubex.utils;

/**
 * Contains various useful methods to work with strings.
 * 
 * @author Mikhail Vladimirov
 */
public class StringUtils
{
    private StringUtils ()
    {
        throw new Error ("Do not instantiate me");
    }
    
    /**
     * Escapes special characters in a string.
     * 
     * @param string original string
     * @return string with special characters escaped
     */
    public static String escape (String string)
    {
        if (string == null)
            throw new IllegalArgumentException ("String is null");
        
        int length = string.length ();
        StringBuffer result = new StringBuffer (length);
        
        for (int i = 0; i < length; i++)
        {
            char ch = string.charAt (i);
            
            switch (ch)
            {
            case '\b':
                result.append ("\\b");
                break;
            case '\t':
                result.append ("\\t");
                break;
            case '\n':
                result.append ("\\n");
                break;
            case '\f':
                result.append ("\\f");
                break;
            case '\r':
                result.append ("\\r");
                break;
            case '\"':
                result.append ("\\\"");
                break;
            case '\'':
                result.append ("\\\'");
                break;
            case '\\':
                result.append ("\\\\");
                break;
            case '\0':
                result.append ("\\000");
                break;
            default:
                result.append (ch);
            }
        }
        
        return result.toString ();
    }
    
    /**
     * Tests whether given string is a valid Java identifier.
     * 
     * @param string string to be tested
     * @return <code>true</code> if given string is a valid Java identifier, 
     *         <code>false</code> otherwise
     */
    public static boolean isJavaIdentifier (String string)
    {
        if (string == null)
            throw new IllegalArgumentException ("String is null");
        
        if (string.isEmpty ())
            return false;
        
        if (!Character.isJavaIdentifierStart (string.charAt (0)))
            return false;
        
        for (int length = string.length (), i = 1; i < length; i++)
            if (!Character.isJavaIdentifierPart (string.charAt (0)))
                return false;
        
        return true;
    }
}
