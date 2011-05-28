package com.googlecode.rubex.observable;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.observable.event.ObservableCollectionListener;

/**
 * Abstract base class for implementations of {@link ObservableCollection} 
 * interface.
 * 
 * @author Mikhail Vladimirov 
 */
public abstract class AbstractObservableCollection <T>
    implements ObservableCollection <T>
{
    private final List <ObservableCollectionListener <T>> listeners = 
        new ArrayList <ObservableCollectionListener <T>> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener (ObservableCollectionListener <T> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        listeners.add (listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener (ObservableCollectionListener <T> listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        listeners.remove (listener);
    }
    
    /**
     * Get all observable connection listeners.
     * 
     * @return an array of observable connection listeners
     */
    @SuppressWarnings ("unchecked")
    public ObservableCollectionListener <T> [] getAllListeners ()
    {
        return (ObservableCollectionListener <T> [])listeners.toArray (
            new ObservableCollectionListener <?> [listeners.size ()]);
    }
}
