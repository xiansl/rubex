package com.googlecode.rubex.net;

import java.net.ServerSocket;
import java.net.Socket;

import com.googlecode.rubex.protocol.ProtocolMessage;

public class ProtocolServerSocketServer
    extends AbstractServerSocketServer <ProtocolMessage>
{
    public ProtocolServerSocketServer (ServerSocket serverSocket)
    {
        super (serverSocket);
    }

    @Override
    protected Connection <ProtocolMessage> createConnection (Socket socket)
    {
        return new SimpleProtocolConnection (
            new SimpleDataConnection (
                new SimpleSocketConnection (socket)));
    }
}
