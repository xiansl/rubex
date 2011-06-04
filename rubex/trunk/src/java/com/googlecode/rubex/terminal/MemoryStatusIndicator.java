package com.googlecode.rubex.terminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;
import javax.swing.Timer;

public class MemoryStatusIndicator extends JProgressBar
{
    private final Timer timer;
    
    public MemoryStatusIndicator ()
    {
        timer = new Timer (1000, new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                updateState ();
            }
        });
        setStringPainted (true);
        
        addPropertyChangeListener ("ancestor", new PropertyChangeListener()
        {
            @Override
            public void propertyChange (PropertyChangeEvent evt)
            {
                if (isDisplayable ())
                {
                    updateState ();
                    timer.start ();
                }
                else
                {
                    timer.stop ();
                }
            }
        });
    }
    
    private void updateState ()
    {
        Runtime runtime = Runtime.getRuntime ();
        
        long freeMemory = runtime.freeMemory ();
        long totalMemory = runtime.totalMemory ();
        long usedMemory = totalMemory - freeMemory;
        
        long totalMemoryScaled = totalMemory;
        long usedMemoryScaled = usedMemory;
        while (totalMemoryScaled > Integer.MAX_VALUE || usedMemoryScaled > Integer.MAX_VALUE)
        {
            totalMemoryScaled >>= 1;
            usedMemoryScaled >>= 1;
        }
        
        setMinimum (0);
        setMaximum ((int)totalMemoryScaled);
        setValue ((int)usedMemoryScaled);
        setString (usedMemory / (1024 * 1024) + "M of " + totalMemory / (1024 * 1024) + "M");
    }
}
