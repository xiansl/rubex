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
     * Unescape special characters in given string.
     * 
     * @param string string to unescape special characters in
     * @return string with unescaped special characters
     */
    public static String unescape (String string)
    {
        if (string == null)
            throw new IllegalArgumentException ("String is null");
        
        int length = string.length ();
        StringBuffer result = new StringBuffer (length);
        int state = 0;
        int octal = 0;
        for (int i = 0; i < length; i++)
        {
            char ch = string.charAt (i);
            
            switch (state)
            {
            case 0:
                switch (ch)
                {
                case '\\':
                    state = 1;
                    break;
                default:
                    result.append (ch);
                }
                break;
            case 1:
                switch (ch)
                {
                case '0':
                case '1':
                case '2':
                case '3':
                    octal = ch - '0';
                    state = 2;
                    break;
                case '4':
                case '5':
                case '6':
                case '7':
                    octal = ch - '0';
                    state = 3;
                    break;
                case 'b':
                    result.append ('\b');
                    state = 0;
                    break;
                case 't':
                    result.append ('\t');
                    state = 0;
                    break;
                case 'n':
                    result.append ('\n');
                    state = 0;
                    break;
                case 'f':
                    result.append ('\f');
                    state = 0;
                    break;
                case 'r':
                    result.append ('\r');
                    state = 0;
                    break;
                default:
                    result.append (ch);
                    state = 0;
                    break;
                }
                break;
            case 2:
                switch (ch)
                {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    octal = octal * 8 + (ch - '0');
                    state = 3;
                    break;
                default:
                    result.append ((char)octal);
                    state = 0;
                    i--;
                    continue;
                }
                break;
            case 3:
                switch (ch)
                {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    octal = octal * 8 + (ch - '0');
                    result.append ((char)octal);
                    state = 0;
                    break;
                default:
                    result.append ((char)octal);
                    state = 0;
                    i--;
                    continue;
                }
                break;
            default:
                throw new Error ("Unknown state: " + state);
            }
        }
        
        switch (state)
        {
        case 0:
            break;
        case 1:
            result.append ('\\');
            break;
        case 2:
        case 3:
            result.append ((char)octal);
            break;
        default:
            throw new Error ("Unknown state: " + state);
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
            if (!Character.isJavaIdentifierPart (string.charAt (i)))
                return false;
        
        return true;
    }
}
