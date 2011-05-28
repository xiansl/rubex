package com.googlecode.rubex.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.rubex.net.Connection;
import com.googlecode.rubex.net.event.ConnectionEvent;
import com.googlecode.rubex.net.event.MessageEvent;
import com.googlecode.rubex.net.event.MessageListener;
import com.googlecode.rubex.party.Party;
import com.googlecode.rubex.party.PartyOrder;
import com.googlecode.rubex.party.event.PartyEvent;
import com.googlecode.rubex.party.event.PartyListener;
import com.googlecode.rubex.protocol.CancelOrderProtocolMessage;
import com.googlecode.rubex.protocol.NewOrderProtocolMessage;
import com.googlecode.rubex.protocol.OrderStatusProtocolMessage;
import com.googlecode.rubex.protocol.ProtocolMessage;
import com.googlecode.rubex.protocol.ProtocolMessageVisitor;
import com.googlecode.rubex.protocol.ProtocolUtils;
import com.googlecode.rubex.protocol.RejectProtocolMessage;
import com.googlecode.rubex.protocol.ReplaceOrderProtocolMessage;

public class RubexServerSession
{
    private final static Logger logger =
        Logger.getLogger (RubexServerSession.class.getName ());

    private final MyPartyListener partyListener =
        new MyPartyListener ();
    
    private final Party party;
    private final Connection <ProtocolMessage> connection;
    
    public RubexServerSession (
        Party party, Connection <ProtocolMessage> connection)
    {
        if (party == null)
            throw new IllegalArgumentException ("Party is null");
        
        if (connection == null)
            throw new IllegalArgumentException ("Connection is null");
        
        this.party = party;
        this.connection = connection;
        
        party.addPartyListener (partyListener);
        connection.addMessageListener (new MyMessageListener ());
    }
    
    private synchronized void processMessage (ProtocolMessage message)
    {
        if (message == null)
            throw new IllegalArgumentException ("Message is null");

        message.accept (
            new ProtocolMessageVisitor <Object> ()
            {
                @Override
                public Object visitReject (RejectProtocolMessage reject)
                {
                    if (logger.isLoggable (Level.WARNING))
                        logger.warning (
                            "Message was rejected by client: " + 
                            reject.getRejectReason () + ": " + 
                            reject.getRejectedMessage ());
                    
                    return null;
                }

                @Override
                public Object visitNewOrder (NewOrderProtocolMessage newOrder)
                {
                    try
                    {
                        party.processNewOrderMessage (newOrder);
                    }
                    catch (Exception ex)
                    {
                        if (logger.isLoggable (Level.WARNING))
                            logger.log (
                                Level.WARNING, 
                                "Error processing new order message", ex);
                        
                        connection.sendMessage (
                            ProtocolUtils.createReject (
                                ex.getMessage (), 
                                ProtocolUtils.marshal (newOrder)));
                    }
                    
                    return null;
                }

                @Override
                public Object visitReplaceOrder (
                    ReplaceOrderProtocolMessage replaceOrder)
                {
                    try
                    {
                        party.processReplaceOrderMessage (replaceOrder);
                    }
                    catch (Exception ex)
                    {
                        if (logger.isLoggable (Level.WARNING))
                            logger.log (
                                Level.WARNING, 
                                "Error processing replace order message", ex);
                        
                        connection.sendMessage (
                            ProtocolUtils.createReject (
                                ex.getMessage (), 
                                ProtocolUtils.marshal (replaceOrder)));
                    }
                    
                    return null;
                }

                @Override
                public Object visitCancelOrder (
                    CancelOrderProtocolMessage cancelOrder)
                {
                    try
                    {
                        party.processCancelOrderMessage (cancelOrder);
                    }
                    catch (Exception ex)
                    {
                        if (logger.isLoggable (Level.WARNING))
                            logger.log (
                                Level.WARNING, 
                                "Error processing cancel order message", ex);
                        
                        connection.sendMessage (
                            ProtocolUtils.createReject (
                                ex.getMessage (), 
                                ProtocolUtils.marshal (cancelOrder)));
                    }
                    
                    return null;
                }
                
                @Override
                public Object visitOrderStatus (
                    OrderStatusProtocolMessage orderStatus)
                {
                    if (logger.isLoggable (Level.WARNING))
                        logger.warning ("Order status message received from client");
                    
                    connection.sendMessage (
                        ProtocolUtils.createReject (
                            "Unexpected order status message received", 
                            ProtocolUtils.marshal (orderStatus)));
                    
                    return null;
                }
            });
    }
    
    private synchronized void sendOrderStatus (PartyOrder order)
    {
        connection.sendMessage (
            ProtocolUtils.createOrderStatus (
                order.getOrderID (), order.getOrderState (), 
                order.getAccount (), order.getSymbol (), order.getSide (), 
                order.getQuantity (), order.getFilledQuantity (), 
                order.getFilledValue (), order.getOrderType (), 
                order.getTimeInForce (), order.getLimitPrice (), 
                order.getStopPrice (), order.getVisibleQuantity ()));
    }
    
    private class MyPartyListener implements PartyListener
    {
        @Override
        public void onOrder (PartyEvent event)
        {
            sendOrderStatus (event.getNewOrder ());
        }
        
        @Override
        public void onOrderCreated (PartyEvent event)
        {
            sendOrderStatus (event.getNewOrder ());
        }

        @Override
        public void onOrderChanged (PartyEvent event)
        {
            sendOrderStatus (event.getNewOrder ());
        }
    }
    
    private class MyMessageListener implements MessageListener <ProtocolMessage>
    {
        @Override
        public void onMessage (
            MessageEvent <? extends ProtocolMessage> event)
        {
            if (event == null)
                throw new IllegalArgumentException ("Event is null");
            
            processMessage (event.getMessage ());
        }

        @Override
        public void onDisconnect (
            ConnectionEvent <? extends ProtocolMessage> event)
        {
            party.removePartyListener (partyListener);
        }
    }
}
