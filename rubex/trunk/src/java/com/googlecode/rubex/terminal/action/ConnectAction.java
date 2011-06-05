package com.googlecode.rubex.terminal.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.googlecode.rubex.utils.SwingL10NHelper;

public class ConnectAction extends AbstractAction
{
    private final static Icon CONNECT_ICON =
        new ImageIcon (ConnectAction.class.getResource ("connect.png"));
    
    public ConnectAction ()
    {
        super ();

        SwingL10NHelper.localizeActionName (this, "connect-action.name");
        putValue (SMALL_ICON, CONNECT_ICON);
    }
    
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // Do nothing
    }
}
