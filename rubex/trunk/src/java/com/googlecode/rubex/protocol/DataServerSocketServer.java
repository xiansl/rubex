package com.googlecode.rubex.protocol;

import java.net.ServerSocket;
import java.net.Socket;

import com.googlecode.rubex.data.DataObject;

public class DataServerSocketServer
    extends AbstractServerSocketServer <DataObject>
{
    public DataServerSocketServer (ServerSocket serverSocket)
    {
        super (serverSocket);
    }

    @Override
    protected Connection <DataObject> createConnection (Socket socket)
    {
        return new SimpleDataConnection (
            new SimpleSocketConnection (socket));
    }
}
