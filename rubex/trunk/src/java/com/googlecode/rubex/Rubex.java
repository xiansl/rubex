package com.googlecode.rubex;

import java.util.logging.LogManager;

import com.googlecode.rubex.server.RubexServer;

public class Rubex
{
    public static void main (String[] args) throws Exception
    {
        LogManager.getLogManager ().readConfiguration (
            Rubex.class.getResourceAsStream ("logging.properties"));
        
        RubexServer server = new RubexServer (1234);
        
        server.start ();
        Thread.sleep (50000);
        server.stop ();
    }
}
