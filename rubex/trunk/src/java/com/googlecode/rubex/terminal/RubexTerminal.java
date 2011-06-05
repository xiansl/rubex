package com.googlecode.rubex.terminal;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.googlecode.rubex.utils.SwingL10NHelper;
import com.googlecode.rubex.utils.XMLResourceBundleControl;

public class RubexTerminal
{
    public final static ResourceBundle EN = 
        ResourceBundle.getBundle (
            RubexTerminal.class.getName (), 
            new Locale ("en"),
            new XMLResourceBundleControl ());
    
    public final static ResourceBundle RU = 
        ResourceBundle.getBundle (
            RubexTerminal.class.getName (), 
            new Locale ("ru"),
            new XMLResourceBundleControl ());
    
    public static void main (String [] args) throws Exception
    {
        SwingL10NHelper.setResourceBundle (RU);
        
        UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());

        MainFrame frame = new MainFrame ();
        
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        
        frame.setVisible (true);
    }
}
