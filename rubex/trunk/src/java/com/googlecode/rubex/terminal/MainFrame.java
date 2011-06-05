package com.googlecode.rubex.terminal;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.googlecode.rubex.utils.SwingL10NHelper;

public class MainFrame extends JFrame
{
    private final static Image RUBEX_ICON_16x16 = 
        new ImageIcon (MainFrame.class.getResource ("rubex-16x16.png")).getImage ();
    
    private final static Image RUBEX_ICON_24x24 = 
        new ImageIcon (MainFrame.class.getResource ("rubex-24x24.png")).getImage ();
    
    private final static Image RUBEX_ICON_32x32 = 
        new ImageIcon (MainFrame.class.getResource ("rubex-32x32.png")).getImage ();
    
    private final static Image RUBEX_ICON_48x48 = 
        new ImageIcon (MainFrame.class.getResource ("rubex-48x48.png")).getImage ();
    
    private final static Image RUBEX_ICON_64x64 = 
        new ImageIcon (MainFrame.class.getResource ("rubex-64x64.png")).getImage ();
    
    private final static Image RUBEX_ICON_256x256 = 
        new ImageIcon (MainFrame.class.getResource ("rubex-256x256.png")).getImage ();
    
    public MainFrame ()
    {
        Box topPanel = Box.createHorizontalBox ();
        
        Box centerPanel = Box.createVerticalBox ();
        
        Box rightPanel = Box.createVerticalBox ();
        
        JTabbedPane bottomPanel = new JTabbedPane ();
        
        JPanel mainPanel = new JPanel (new BorderLayout (5, 5));  
        mainPanel.add (topPanel, BorderLayout.NORTH);
        mainPanel.add (centerPanel, BorderLayout.CENTER);
        mainPanel.add (rightPanel, BorderLayout.EAST);
        mainPanel.add (bottomPanel, BorderLayout.SOUTH);
        
        JLabel statusMessageLabel = new JLabel ();
        SwingL10NHelper.localizeJLabelText (
            statusMessageLabel, "main-frame.status-message-label.text.disconnected");
        
        MemoryStatusIndicator memoryStatusIndicator = 
            new MemoryStatusIndicator ();
        int w = memoryStatusIndicator.getPreferredSize ().height;
        Dimension s = new Dimension (100, w);
        memoryStatusIndicator.setPreferredSize (s);
        memoryStatusIndicator.setMaximumSize (s);
        
        JButton gcButton = new JButton ();
        SwingL10NHelper.localizeAbstractButtonText (gcButton, "main-frame.gc-button.text");
        SwingL10NHelper.localizeJComponentToolTipText (gcButton, "main-frame.gc-button.toolTipText");
        gcButton.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                System.gc ();
            }
        });
        gcButton.setPreferredSize (new Dimension (gcButton.getPreferredSize ().width, w));
        
        Box statusLine = Box.createHorizontalBox ();
        statusLine.add (Box.createHorizontalStrut (8));
        statusLine.add (statusMessageLabel);
        statusLine.add (Box.createHorizontalGlue ());
        statusLine.add (memoryStatusIndicator);
        statusLine.add (gcButton);
        
        Container contentPane = getContentPane ();
        contentPane.setLayout (new BorderLayout ());
        contentPane.add (mainPanel, BorderLayout.CENTER);
        contentPane.add (statusLine, BorderLayout.SOUTH);
        
        setIconImages (Arrays.asList (RUBEX_ICON_16x16, RUBEX_ICON_24x24, RUBEX_ICON_32x32, RUBEX_ICON_48x48, RUBEX_ICON_64x64, RUBEX_ICON_256x256));
        
        MenuItem englishMenuItem = new MenuItem ();
        SwingL10NHelper.localizeMenuItemLabel (englishMenuItem, "main-frame.menu.language.english.label");
        englishMenuItem.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                SwingL10NHelper.setResourceBundle (RubexTerminal.EN);
            }
        });
        
        MenuItem russianMenuItem = new MenuItem ();
        SwingL10NHelper.localizeMenuItemLabel (russianMenuItem, "main-frame.menu.language.russian.label");
        russianMenuItem.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                SwingL10NHelper.setResourceBundle (RubexTerminal.RU);
            }
        });
        
        Menu languageMenu = new Menu ();
        SwingL10NHelper.localizeMenuItemLabel (languageMenu, "main-frame.menu.language.label");
        languageMenu.add (englishMenuItem);
        languageMenu.add (russianMenuItem);
        
        MenuBar menuBar = new MenuBar ();
        menuBar.add (languageMenu);
        setMenuBar (menuBar);
        
        pack ();
    }
}
