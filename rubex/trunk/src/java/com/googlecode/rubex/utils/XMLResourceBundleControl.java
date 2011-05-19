package com.googlecode.rubex.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * Extends {@link Control} class adding an ability to load resource bundles
 * from XML.
 * 
 * @see Properties#loadFromXML(InputStream)
 * 
 * @author Mikhail Vladimirov
 */
public class XMLResourceBundleControl extends Control
{
    /**
     * {@inheritDoc}
     */
    @Override
    public List <String> getFormats (String baseName) 
    {
        if (baseName == null)
            throw new IllegalArgumentException ("Base name is null");
        
        List <String> result = new ArrayList <String> (
            super.getFormats (baseName));
        
        result.add ("xml");
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceBundle newBundle (
        String baseName, Locale locale, String format, 
        ClassLoader loader, boolean reload)
        throws IllegalAccessException, InstantiationException, IOException 
    {
        if (baseName == null)
            throw new IllegalArgumentException ("Base name is null");
        
        if (locale == null)
            throw new IllegalArgumentException ("ocale is null");
        
        if (format == null)
            throw new IllegalArgumentException ("Format is null");
        
        if (loader == null)
            throw new IllegalArgumentException ("Loader is null");
        
        if (format.equals("xml")) 
        {
            String bundleName = toBundleName (baseName, locale);
            String resourceName = toResourceName (bundleName, format);
            InputStream stream;
            if (reload) 
            {
                URL url = loader.getResource (resourceName);
                if (url != null) 
                {
                    URLConnection connection = url.openConnection ();
                    if (connection != null) 
                    {
                        connection.setUseCaches (false);
                        stream = connection.getInputStream ();
                    }
                    else return null;
                }
                else return null;
            } 
            else 
            {
                stream = loader.getResourceAsStream (resourceName);
            }
            
            if (stream != null) 
            {
                try
                {
                    return new XMLResourceBundle (
                        new BufferedInputStream (stream));
                }
                finally
                {
                    stream.close ();
                }
            }
            else return null;
        }
        else 
            return super.newBundle (
                baseName, locale, format, loader, reload);
    }
}