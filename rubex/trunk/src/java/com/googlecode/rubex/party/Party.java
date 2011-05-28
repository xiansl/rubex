package com.googlecode.rubex.party;

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
    public void processNewOrderMessage (NewOrderProtocolMessage newOrder);
    public void processReplaceOrderMessage (ReplaceOrderProtocolMessage replaceOrder);
    public void processCancelOrderMessage (CancelOrderProtocolMessage cancelOrder);
}
