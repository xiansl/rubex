package com.googlecode.rubex.utils;

import java.awt.Dialog;
import java.awt.MenuItem;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SwingL10NHelper
{
    private final static Map <Object, Map <String, L10NEntry>> entries =
        new WeakHashMap <Object, Map<String,L10NEntry>> ();
    
    private static ResourceBundle resourceBundle = null;
    
    private SwingL10NHelper ()
    {
        throw new Error ("Do not instantiate me");
    }
    
    public static synchronized void setResourceBundle (
        ResourceBundle resourceBundle)
    {
        if (resourceBundle == null)
            throw new IllegalArgumentException ("Resource bundle is null");
        
        SwingL10NHelper.resourceBundle = resourceBundle;

        for (Map <String, L10NEntry> entity: entries.values ())
            for (L10NEntry entry: entity.values ())
                entry.update ();
    }

    public static synchronized void localizeJLabelText (
        JLabel label, String key, Object ... parameters)
    {
        if (label == null)
            throw new IllegalArgumentException ("Label is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        if (parameters == null)
            throw new IllegalArgumentException ("Parameters is null");
        
        addEntry (
            label, "text", new L10NJLabelText (label, key, parameters));
    }
    
    public static synchronized void localizeMenuItemLabel (
        MenuItem menuItem, String key, Object ... parameters)
    {
        if (menuItem == null)
            throw new IllegalArgumentException ("Menu item is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        if (parameters == null)
            throw new IllegalArgumentException ("Parameters is null");
        
        addEntry (
            menuItem, "label", 
            new L10NMenuItemLabel (menuItem, key, parameters));
    }
    
    public static synchronized void localizeDialogTitle (
        Dialog dialog, String key, Object ... parameters)
    {
        if (dialog == null)
            throw new IllegalArgumentException ("Dialog is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        if (parameters == null)
            throw new IllegalArgumentException ("Parameters is null");
        
        addEntry (
            dialog, "title", new L10NDialogTitle (dialog, key, parameters));
    }
    
    public static synchronized void localizeAbstractButtonText (
        AbstractButton abstarctButton, String key, Object ... parameters)
    {
        if (abstarctButton == null)
            throw new IllegalArgumentException ("Abstract button is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        if (parameters == null)
            throw new IllegalArgumentException ("Parameters is null");
        
        addEntry (
            abstarctButton, "text", 
            new L10NAbstractButtonText (abstarctButton, key, parameters));
    }
    
    public static synchronized void localizeJProgressBarString (
        JProgressBar progressBar, String key, Object ... parameters)
    {
        if (progressBar == null)
            throw new IllegalArgumentException ("Progress bar is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        if (parameters == null)
            throw new IllegalArgumentException ("Parameters is null");
        
        addEntry (
            progressBar, "string", 
            new L10NJProgressBarString (progressBar, key, parameters));
    }
    
    public static synchronized void localizeJComponentToolTipText (
        JComponent component, String key, Object ... parameters)
    {
        if (component == null)
            throw new IllegalArgumentException ("Component is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        if (parameters == null)
            throw new IllegalArgumentException ("Parameters is null");
        
        addEntry (
            component, "toolTipText", 
            new L10NJComponentToolTipText (component, key, parameters));
    }
    
    private static void addEntry (Object entity, String property, L10NEntry entry)
    {
        if (entity == null)
            throw new IllegalArgumentException ("Entity is null");
        
        if (property == null)
            throw new IllegalArgumentException ("Property is null");
        
        if (entry == null)
            throw new IllegalArgumentException ("Entry is null");
        
        Map <String, L10NEntry> m = entries.get (entity);
        if (m == null)
        {
            m = new HashMap <String, SwingL10NHelper.L10NEntry> ();
            entries.put (entity, m);
        }
        
        m.put (property, entry);
    }
    
    private static interface L10NEntry
    {
        public void update ();
    }
    
    private static abstract class AbstractL10NEntry implements L10NEntry
    {
        private final static Pattern PLACEHOLDER = 
            Pattern.compile ("(?:\\$[0-9])|(?:\\$\\$)|(?:\\$\\{[0-9]+\\})");
        
        private final String key;
        private final Object [] parameters;
        
        public AbstractL10NEntry (String key, Object ... parameters)
        {
            if (key == null)
                throw new IllegalArgumentException ("Key is null");
            
            if (parameters == null)
                throw new IllegalArgumentException ("Parameters is null");
            
            this.key = key;
            this.parameters = parameters.clone ();
        }
        
        protected String getStringValue ()
        {
            StringBuffer result = new StringBuffer ();
            
            Matcher m = PLACEHOLDER.matcher (resourceBundle.getString (key));
            while (m.find ())
            {
                String placeholder = m.group ();
                
                if ("$$".equals (placeholder))
                    m.appendReplacement (result, "$");
                else if (placeholder.startsWith ("${"))
                    m.appendReplacement (
                        result, 
                        parameters [Integer.parseInt (
                            placeholder.substring (2, placeholder.length () - 1)) - 1].toString ());
                else
                    m.appendReplacement (
                        result, 
                        parameters [Integer.parseInt (
                            placeholder.substring (1)) - 1].toString ());
            }
            m.appendTail (result);
            return result.toString ();
        }
    }
    
    private static class L10NJLabelText 
        extends AbstractL10NEntry
    {
        private final WeakReference <JLabel> label;
        
        public L10NJLabelText (JLabel label, String key, Object ... parameters)
        {
            super (key, parameters);
            
            if (label == null)
                throw new IllegalArgumentException ("Label is null");

            this.label = new WeakReference <JLabel> (label);
            
            update ();
        }
        
        @Override
        public void update ()
        {
            JLabel l = label.get ();
            
            if (l != null)
                l.setText (getStringValue ());
        }
    }
    
    private static class L10NMenuItemLabel 
        extends AbstractL10NEntry
    {
        private final WeakReference <MenuItem> menuItem;
        
        public L10NMenuItemLabel (MenuItem menuItem, String key, Object ... parameters)
        {
            super (key, parameters);
            
            if (menuItem == null)
                throw new IllegalArgumentException ("Menu item is null");
            
            this.menuItem = new WeakReference <MenuItem> (menuItem);
            
            update ();
        }
        
        @Override
        public void update ()
        {
            MenuItem m = menuItem.get ();
            
            if (m != null)
                m.setLabel (getStringValue ());
        }
    }
    
    private static class L10NDialogTitle 
    extends AbstractL10NEntry
    {
        private final WeakReference <Dialog> dialog;
        
        public L10NDialogTitle (Dialog dialog, String key, Object ... parameters)
        {
            super (key, parameters);
            
            if (dialog == null)
                throw new IllegalArgumentException ("Dialog is null");
            
            this.dialog = new WeakReference <Dialog> (dialog);
            
            update ();
        }
        
        @Override
        public void update ()
        {
            Dialog m = dialog.get ();
            
            if (m != null)
                m.setTitle (getStringValue ());
        }
    }
    
    private static class L10NAbstractButtonText
        extends AbstractL10NEntry
    {
        private final WeakReference <AbstractButton> abstractButton;
        
        public L10NAbstractButtonText (AbstractButton abstractButton, String key, Object ... parameters)
        {
            super (key, parameters);
            
            if (abstractButton == null)
                throw new IllegalArgumentException ("Abstract button is null");
            
            this.abstractButton = new WeakReference <AbstractButton> (abstractButton);
            
            update ();
        }
        
        @Override
        public void update ()
        {
            AbstractButton m = abstractButton.get ();
            
            if (m != null)
                m.setText (getStringValue ());
        }
    }
    
    private static class L10NJProgressBarString
        extends AbstractL10NEntry
    {
        private final WeakReference <JProgressBar> progressBar;
        
        public L10NJProgressBarString (JProgressBar progressBar, String key, Object ... parameters)
        {
            super (key, parameters);
            
            if (progressBar == null)
                throw new IllegalArgumentException ("Progress bar is null");
            
            this.progressBar = new WeakReference <JProgressBar> (progressBar);
            
            update ();
        }
        
        @Override
        public void update ()
        {
            JProgressBar p = progressBar.get ();
            
            if (p != null)
                p.setString (getStringValue ());
        }
    }
    
    private static class L10NJComponentToolTipText
        extends AbstractL10NEntry
    {
        private final WeakReference <JComponent> component;
        
        public L10NJComponentToolTipText (JComponent component, String key, Object ... parameters)
        {
            super (key, parameters);
            
            if (component == null)
                throw new IllegalArgumentException ("Component is null");
            
            this.component = new WeakReference <JComponent> (component);
            
            update ();
        }
        
        @Override
        public void update ()
        {
            JComponent c = component.get ();
            
            if (c != null)
                c.setToolTipText (getStringValue ());
        }
    }
}
