package com.googlecode.rubex.protocol;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.data.DataObject;
import com.googlecode.rubex.protocol.event.DataConnectionEvent;
import com.googlecode.rubex.protocol.event.DataEvent;
import com.googlecode.rubex.protocol.event.DataListener;

/**
 * Abstract base class for implementations of {@link DataConnection} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractDataConnection implements DataConnection
{
    private final List <DataListener> dataListeners = 
        new ArrayList <DataListener> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDataListener (DataListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        dataListeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDataListener (DataListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        dataListeners.remove (listener);
    }

    /**
     * Get all data listeners.
     * 
     * @return an array of {@link DataListener} objects
     */
    public DataListener [] getAllDataListeners ()
    {
        return dataListeners.toArray (
            new DataListener [dataListeners.size ()]);
    }
    
    /**
     * Notify all data listeners about incoming data object.
     * 
     * @param data incoming data object
     */
    protected void fireOnIncomingData (DataObject data)
    {
        DataEvent event = null;
        
        for (DataListener listener: dataListeners)
        {
            if (event == null)
                event = new DataEvent (this, data);
            
            listener.onIncomingData (event);
        }
    }
    
    /**
     * Notify all data listeners about disconnect.
     */
    protected void fireOnDisconnect ()
    {
        DataConnectionEvent event = null;
        
        for (DataListener listener: dataListeners)
        {
            if (event == null)
                event = new DataConnectionEvent (this, this);
            
            listener.onDisconnect (event);
        }
    }
}
