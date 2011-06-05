package com.googlecode.rubex.terminal.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.googlecode.rubex.utils.SwingL10NHelper;

public class DisconnectAction extends AbstractAction
{
    private final static Icon DISCONNECT_ICON =
        new ImageIcon (DisconnectAction.class.getResource ("disconnect.png"));
    
    public DisconnectAction ()
    {
        super ();

        SwingL10NHelper.localizeActionName (this, "disconnect-action.name");
        putValue (SMALL_ICON, DISCONNECT_ICON);
    }
    
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // Do nothing
    }
}
