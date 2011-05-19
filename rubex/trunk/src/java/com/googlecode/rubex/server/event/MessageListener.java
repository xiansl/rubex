package com.googlecode.rubex.server.event;

public interface MessageListener
{
    public void onMessage (MessageEvent event);
    
    public void onDisconnect (ConnectionEvent event);
}
