package com.googlecode.rubex.party.event;

import java.util.EventListener;

/**
 * Receives notifications about party events.
 * 
 * @author Mikhail Vladimirov
 */
public interface PartyListener extends EventListener
{
    /**
     * Called for all orders when listener was added.
     * 
     * @param event event details
     */
    public void onOrder (PartyEvent event);
    
    /**
     * Called when party order was created.
     * 
     * @param event event details
     */
    public void onOrderCreated (PartyEvent event);
    
    /**
     * Called when party order was changed.
     * 
     * @param event event details
     */
    public void onOrderChanged (PartyEvent event);
}
