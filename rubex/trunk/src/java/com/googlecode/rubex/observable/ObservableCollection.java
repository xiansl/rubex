package com.googlecode.rubex.observable;

import com.googlecode.rubex.observable.event.ObservableCollectionListener;

/**
 * Collection of objects that can notify listeners about updates.
 * 
 * @author Mikhail Vladimirov
 */
public interface ObservableCollection <T>
{
    /**
     * Add listener to be notified about observable collection events.
     * 
     * @param listener listener to be added
     */
    public void addListener (ObservableCollectionListener <T> listener);
    
    /**
     * Remove observable collection listener.
     * 
     * @param listener listener to be removed
     */
    public void removeListener (ObservableCollectionListener <T> listener);
    
    /**
     * Return all elements of the collection.
     * 
     * @return an array of elements
     */
    public T [] getAllElements ();
}
