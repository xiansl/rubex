package com.googlecode.rubex.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Extends {@link ResourceBundle} class adding an ability to load
 * resource bundle from XML.
 * 
 * @see Properties#loadFromXML(InputStream)
 * 
 * @author Mikhail Vladimirov
 */
public class XMLResourceBundle extends ResourceBundle 
{
    private final Properties properties;
    
    /**
     * Create new instance of XML resource bundle loading it from given input 
     * stream.
     * 
     * @param input input stream to load resource bundle from
     * @throws IOException if IO error occurred while loading resource bundle
     */
    public XMLResourceBundle (InputStream input) 
        throws IOException 
    {
        properties = new Properties ();
        properties.loadFromXML (input);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object handleGetObject (String key) 
    {
        return properties.getProperty (key);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration <String> getKeys () 
    {
        return new ToStringEnumeration (properties.keys ());
    }
    
    private static class ToStringEnumeration 
        implements Enumeration <String>
    {
        private final Enumeration <? extends Object> enumeration;
        
        public ToStringEnumeration (
            Enumeration <? extends Object> enumeration)
        {
            if (enumeration == null)
                throw new IllegalArgumentException ("Enumeration is null");
            
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements ()
        {
            return enumeration.hasMoreElements ();
        }

        @Override
        public String nextElement ()
        {
            Object object = enumeration.nextElement ();
            return object == null ? null : object.toString ();
        }
    }
}
