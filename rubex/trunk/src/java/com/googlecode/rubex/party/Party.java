package com.googlecode.rubex.party;

import com.googlecode.rubex.party.event.PartyListener;
import com.googlecode.rubex.protocol.CancelOrderProtocolMessage;
import com.googlecode.rubex.protocol.NewOrderProtocolMessage;
import com.googlecode.rubex.protocol.ReplaceOrderProtocolMessage;

/**
 * A party being trading on the exchange.
 * 
 * @author Mikhail Vladimirov
 */
public interface Party
{
    /**
     * Add listener to be notified about party events.
     * 
     * @param listener listener to be added
     */
    public void addPartyListener (PartyListener listener);
    
    /**
     * Remove party listener.
     * 
     * @param listener listener to be removed
     */
    public void removePartyListener (PartyListener listener);
    
    /**
     * Process new order message.
     * 
     * @param newOrder new order message to be processed
     */
    public void processNewOrderMessage (NewOrderProtocolMessage newOrder);
    
    /**
     * Process replace order message.
     * 
     * @param replaceOrder replace order message to be processed
     */
    public void processReplaceOrderMessage (ReplaceOrderProtocolMessage replaceOrder);
    
    /**
     * Process cancel order message.
     * 
     * @param cancelOrder cancel order message to be processed
     */
    public void processCancelOrderMessage (CancelOrderProtocolMessage cancelOrder);
    
    /**
     * Get all orders for this party.
     * 
     * @return an array of {@link PartyOrder} objects
     */
    public PartyOrder [] getAllOrders ();
}
