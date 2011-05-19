package com.googlecode.rubex.server.event;

import java.util.EventListener;

public interface ConnectionListener extends EventListener
{
    public void onNewConnection (ConnectionEvent event);
}
