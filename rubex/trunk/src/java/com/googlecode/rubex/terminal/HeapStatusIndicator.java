package com.googlecode.rubex.terminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.googlecode.rubex.utils.SwingL10NHelper;

/**
 * GUI component that displays status of JVM heap.
 * 
 * @author Mikhail Vladimirov
 */ 
public class HeapStatusIndicator extends JProgressBar
{
    private final Timer timer;
    
    /**
     * Create new heap status indicator.
     */
    public HeapStatusIndicator ()
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
        long maxMemory = runtime.maxMemory ();
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
        
        SwingL10NHelper.localizeJProgressBarString (
            this, "memory-status-indicator.string", Long.valueOf (usedMemory / (1024 * 1024)), Long.valueOf (totalMemory / (1024 * 1024)));
        
        SwingL10NHelper.localizeJComponentToolTipText (
            this, 
            "memory-status-indicator.toolTipText", 
            Long.valueOf (freeMemory / (1024 * 1024)),
            Long.valueOf (totalMemory / (1024 * 1024)),
            Long.valueOf (maxMemory / (1024 * 1024)));
    }
}
