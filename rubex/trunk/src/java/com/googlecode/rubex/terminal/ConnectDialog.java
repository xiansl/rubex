package com.googlecode.rubex.terminal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.googlecode.rubex.utils.SwingL10NHelper;

public class ConnectDialog extends JDialog
{
    private final static Image KEY_ICON_16x16 = 
        Toolkit.getDefaultToolkit ().createImage (ConnectDialog.class.getResource ("key-icon-16x16.png"));
    
    private final static Image KEY_ICON_32x32 = 
        Toolkit.getDefaultToolkit ().createImage (ConnectDialog.class.getResource ("key-icon-32x32.png"));
    
    private final static Image KEY_ICON_48x48 = 
        Toolkit.getDefaultToolkit ().createImage (ConnectDialog.class.getResource ("key-icon-48x48.png"));
    
    private final static Image KEY_ICON_64x64 = 
        Toolkit.getDefaultToolkit ().createImage (ConnectDialog.class.getResource ("key-icon-64x64.png"));
    
    private final JTextField hostField;
    private final JTextField portField;
    private final JTextField keyFileField;
    private final JButton browseKeyButton;
    private final JButton generateKeyButton;
    private final JTextField userNameField;
    private final JTextField keyPasswordField;
    private final JTextArea publicKeyArea;
    private final JTextField entropyField;
    private final JButton cancelButton;
    private final JButton connectButton;
    
    public ConnectDialog ()
    {
        hostField = new JTextField ();
        
        portField = new JTextField (5);
        portField.setMaximumSize (portField.getPreferredSize ());
        
        keyFileField = new JTextField ();
        
        browseKeyButton = new JButton ("...");
        Dimension d = keyFileField.getPreferredSize ();
        browseKeyButton.setPreferredSize (new Dimension (d.height, d.height));
        browseKeyButton.setMinimumSize (browseKeyButton.getPreferredSize ());
        browseKeyButton.setMaximumSize (browseKeyButton.getPreferredSize ());
        
        generateKeyButton = new JButton ();
        SwingL10NHelper.localizeAbstractButtonText (
            generateKeyButton, "connect-dialog.generate-key-button.text");
        generateKeyButton.setPreferredSize (
            new Dimension (
                generateKeyButton.getPreferredSize ().width, 
                d.height));
        
        userNameField = new JTextField ();
        keyPasswordField = new JTextField ();
        
        publicKeyArea = new JTextArea ("Hello\nWorld\nFoo\nBar\nFooBar");
        publicKeyArea.setFont(
            new Font (
                Font.MONOSPACED, Font.PLAIN, 
                getContentPane ().getFont ().getSize ()));
        publicKeyArea.setEditable (false);
     
        JLabel hostLabel = new JLabel ();
        hostLabel.setLabelFor (hostField);
        SwingL10NHelper.localizeJLabelText (
            hostLabel, "connect-dialog.host-label.text");
        
        JLabel portLabel = new JLabel ();
        portLabel.setLabelFor (portField);
        SwingL10NHelper.localizeJLabelText (
            portLabel, "connect-dialog.port-label.text");
        
        Box hostPortBox = Box.createHorizontalBox ();
        hostPortBox.add (hostLabel);
        hostPortBox.add (Box.createHorizontalStrut (5));
        hostPortBox.add (hostField);
        hostPortBox.add (Box.createHorizontalStrut (5));
        hostPortBox.add (portLabel);
        hostPortBox.add (Box.createHorizontalStrut (5));
        hostPortBox.add (portField);
        
        JLabel keyFileLabel = new JLabel ();
        keyFileLabel.setLabelFor (keyFileField);
        SwingL10NHelper.localizeJLabelText (
            keyFileLabel, "connect-dialog.key-file-label.text");
        
        Box keyFileBox = Box.createHorizontalBox ();
        keyFileBox.add (keyFileLabel);
        keyFileBox.add (Box.createHorizontalStrut (5));
        keyFileBox.add (keyFileField);
        keyFileBox.add (browseKeyButton);
        keyFileBox.add (Box.createHorizontalStrut (5));
        keyFileBox.add (generateKeyButton);
        
        JLabel userNameLabel = new JLabel ();
        userNameLabel.setLabelFor (userNameField);
        SwingL10NHelper.localizeJLabelText (
            userNameLabel, "connect-dialog.user-name-label.text");
        
        JLabel keyPasswordLabel = new JLabel ();
        keyPasswordLabel.setLabelFor (keyPasswordField);
        SwingL10NHelper.localizeJLabelText (
            keyPasswordLabel, "connect-dialog.key-password-label.text");
        
        Box userPasswordBox = Box.createHorizontalBox ();
        userPasswordBox.add (userNameLabel);
        userPasswordBox.add (Box.createHorizontalStrut (5));
        userPasswordBox.add (userNameField);
        userPasswordBox.add (Box.createHorizontalStrut (5));
        userPasswordBox.add (keyPasswordLabel);
        userPasswordBox.add (Box.createHorizontalStrut (5));
        userPasswordBox.add (keyPasswordField);
        
        Box topBox = Box.createVerticalBox ();
        topBox.setBorder (
            BorderFactory.createCompoundBorder (
                BorderFactory.createEtchedBorder (),
                BorderFactory.createEmptyBorder (5, 5, 5, 5)));
        topBox.add (hostPortBox);
        topBox.add (Box.createVerticalStrut (5));
        topBox.add (keyFileBox);
        topBox.add (Box.createVerticalStrut (5));
        topBox.add (userPasswordBox);
        
        JLabel publicKeyLabel = new JLabel ();
        publicKeyLabel.setLabelFor (publicKeyArea);
        SwingL10NHelper.localizeJLabelText (
            publicKeyLabel, "connect-dialog.public-key-label.text");

        Box publicKeyLabelBox = Box.createHorizontalBox ();
        publicKeyLabelBox.add (publicKeyLabel);
        publicKeyLabelBox.add (Box.createHorizontalGlue ());
        
        JScrollPane publicKeyScrollPane = new JScrollPane (
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        publicKeyScrollPane.getViewport ().add (publicKeyArea);        
        
        Box centerBox = Box.createVerticalBox ();
        centerBox.add (publicKeyLabelBox);
        centerBox.add (Box.createVerticalStrut (5));
        centerBox.add (publicKeyScrollPane);
        
        entropyField = new JTextField ("");
        entropyField.setFont(new Font (Font.MONOSPACED, Font.PLAIN, entropyField.getFont ().getSize ()));
        entropyField.setBorder (BorderFactory.createEtchedBorder ());
        entropyField.setEnabled (false);
        
        cancelButton = new JButton ();
        SwingL10NHelper.localizeAbstractButtonText (
            cancelButton, "connect-dialog.cancel-button.text");
        
        connectButton = new JButton ();
        SwingL10NHelper.localizeAbstractButtonText (
            connectButton, "connect-dialog.connect-button.text");

        Box bottomBox = Box.createHorizontalBox ();
        bottomBox.add (entropyField);
        bottomBox.add (Box.createHorizontalStrut (5));
        bottomBox.add (cancelButton);
        bottomBox.add (Box.createHorizontalStrut (5));
        bottomBox.add (connectButton);
        
        JPanel mainPanel = new JPanel (new BorderLayout (8, 8));
        mainPanel.setBorder (BorderFactory.createEmptyBorder (8, 8, 8, 8));
        mainPanel.add (topBox, BorderLayout.NORTH);
        mainPanel.add (centerBox, BorderLayout.CENTER);
        mainPanel.add (bottomBox, BorderLayout.SOUTH);
        
        Container contentPane = getContentPane ();
        contentPane.setLayout (new BorderLayout ());
        contentPane.add (mainPanel, BorderLayout.CENTER);

        getRootPane ().setDefaultButton (connectButton);
        
        recursivelyAddMouseMotionListener (this, new EntropyMouseMotionListener ());
        
        SwingL10NHelper.localizeDialogTitle (this, "connect-dialog.title");
        setIconImages (Arrays.asList (KEY_ICON_16x16, KEY_ICON_32x32, KEY_ICON_48x48, KEY_ICON_64x64));
        pack ();
        setModal (true);
        setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
        setVisible (true);
    }
    
    private void recursivelyAddMouseMotionListener (
        Component component, MouseMotionListener listener)
    {
        component.addMouseMotionListener (listener);
        if (component instanceof Container)
        {
            Component [] components = ((Container)component).getComponents ();
            for (Component c: components)
                recursivelyAddMouseMotionListener (c, listener);
        }
    }
    
    private class EntropyMouseMotionListener 
        implements MouseMotionListener
    {
        private final byte [] buffer = new byte [16];
        private final SecureRandom secureRandom;

        public EntropyMouseMotionListener ()
        {
            try
            {
                secureRandom = SecureRandom.getInstance ("SHA1PRNG");
                secureRandom.setSeed (secureRandom.generateSeed (256));
            }
            catch (NoSuchAlgorithmException ex)
            {
                throw new Error (ex);
            }
            
            updateEntropyField ();
        }
        
        @Override
        public void mouseDragged (MouseEvent e)
        {
            update (e);
        }

        @Override
        public void mouseMoved (MouseEvent e)
        {
            update (e);
        }
        
        private void update (MouseEvent e)
        {
            secureRandom.setSeed (e.getXOnScreen ());
            secureRandom.setSeed (e.getYOnScreen ());
            
            updateEntropyField ();
        }
        
        private void updateEntropyField ()
        {
            secureRandom.nextBytes (buffer);
            StringBuffer sb = new StringBuffer (buffer.length * 2);
            for (byte b: buffer)
            {
                sb.append ("0123456789ABCDEF".charAt (b >> 4 & 0x0F));
                sb.append ("0123456789ABCDEF".charAt (b & 0x0F));
            }
            
            entropyField.setText (sb.toString ());
        }
    }
}
