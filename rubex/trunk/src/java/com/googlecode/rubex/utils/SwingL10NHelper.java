package com.googlecode.rubex.utils;

import java.awt.Dialog;
import java.awt.MenuItem;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JLabel;

public class SwingL10NHelper
{
    private final static ReferenceQueue <Object> referenceQueue = 
        new ReferenceQueue <Object> ();
    
    private final static Set <L10NEntry> entries = 
        new HashSet <L10NEntry> ();
    
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

        for (L10NEntry entry: entries)
            entry.update ();
    }

    public static synchronized void localizeJLabelText (
        JLabel label, String key)
    {
        if (label == null)
            throw new IllegalArgumentException ("Label is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        drainQueue ();
        
        entries.add (new L10NJLabelText (label, key));
    }
    
    public static synchronized void localizeMenuItemLabel (
        MenuItem menuItem, String key)
    {
        if (menuItem == null)
            throw new IllegalArgumentException ("Menu item is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        drainQueue ();
        
        entries.add (new L10NMenuItemLabel (menuItem, key));
    }
    
    public static synchronized void localizeDialogTitle (
        Dialog dialog, String key)
    {
        if (dialog == null)
            throw new IllegalArgumentException ("Dialog is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        drainQueue ();
        
        entries.add (new L10NDialogTitle (dialog, key));
    }
    
    public static synchronized void localizeAbstractButtonText (
        AbstractButton abstarctButton, String key)
    {
        if (abstarctButton == null)
            throw new IllegalArgumentException ("Abstract button is null");
        
        if (key == null)
            throw new IllegalArgumentException ("Key is null");
        
        drainQueue ();
        
        entries.add (new L10NAbstractButtonText (abstarctButton, key));
    }
    
    private static void drainQueue ()
    {
        Reference <?> reference;
        while ((reference = referenceQueue.poll ()) != null)
            entries.remove (reference);
    }
    
    private static interface L10NEntry
    {
        public void update ();
    }
    
    private static class L10NJLabelText 
        extends WeakReference <JLabel>
        implements L10NEntry
    {
        private final WeakReference <JLabel> label;
        private final String key;
        
        public L10NJLabelText (JLabel label, String key)
        {
            super (label, referenceQueue);
            
            if (label == null)
                throw new IllegalArgumentException ("Label is null");
            
            if (key == null)
                throw new IllegalArgumentException ("Key is null");
            
            this.label = new WeakReference <JLabel> (label);
            this.key = key;
            label.setText (resourceBundle.getString (key));
        }
        
        @Override
        public void update ()
        {
            JLabel l = label.get ();
            
            if (l != null)
                l.setText (resourceBundle.getString (key));
        }
    }
    
    private static class L10NMenuItemLabel 
        extends WeakReference <MenuItem>
        implements L10NEntry
    {
        private final WeakReference <MenuItem> menuItem;
        private final String key;
        
        public L10NMenuItemLabel (MenuItem menuItem, String key)
        {
            super (menuItem, referenceQueue);
            
            if (menuItem == null)
                throw new IllegalArgumentException ("Menu item is null");
            
            if (key == null)
                throw new IllegalArgumentException ("Key is null");
            
            this.menuItem = new WeakReference <MenuItem> (menuItem);
            this.key = key;
            menuItem.setLabel (resourceBundle.getString (key));
        }
        
        @Override
        public void update ()
        {
            MenuItem m = menuItem.get ();
            
            if (m != null)
                m.setLabel (resourceBundle.getString (key));
        }
    }
    
    private static class L10NDialogTitle 
        extends WeakReference <Dialog>
        implements L10NEntry
    {
        private final WeakReference <Dialog> dialog;
        private final String key;
        
        public L10NDialogTitle (Dialog dialog, String key)
        {
            super (dialog, referenceQueue);
            
            if (dialog == null)
                throw new IllegalArgumentException ("Dialog is null");
            
            if (key == null)
                throw new IllegalArgumentException ("Key is null");
            
            this.dialog = new WeakReference <Dialog> (dialog);
            this.key = key;
            dialog.setTitle (resourceBundle.getString (key));
        }
        
        @Override
        public void update ()
        {
            Dialog m = dialog.get ();
            
            if (m != null)
                m.setTitle (resourceBundle.getString (key));
        }
    }
    
    private static class L10NAbstractButtonText
        extends WeakReference <AbstractButton>
        implements L10NEntry
    {
        private final WeakReference <AbstractButton> abstractButton;
        private final String key;
        
        public L10NAbstractButtonText (AbstractButton abstractButton, String key)
        {
            super (abstractButton, referenceQueue);
            
            if (abstractButton == null)
                throw new IllegalArgumentException ("Dialog is null");
            
            if (key == null)
                throw new IllegalArgumentException ("Key is null");
            
            this.abstractButton = new WeakReference <AbstractButton> (abstractButton);
            this.key = key;
            abstractButton.setText (resourceBundle.getString (key));
        }
        
        @Override
        public void update ()
        {
            AbstractButton m = abstractButton.get ();
            
            if (m != null)
                m.setText (resourceBundle.getString (key));
        }
    }
}
