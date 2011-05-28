package junk;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server
{
    public static void main (String[] args) throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance ("JKS");
        
        keyStore.load (new FileInputStream ("keystore.dat"), "secret".toCharArray ());
        
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance ("SunX509");
        keyManagerFactory.init (keyStore, "secret".toCharArray ());
        
        SSLContext sslContext = SSLContext.getInstance ("SSLv3");
        sslContext.init (keyManagerFactory.getKeyManagers (), null, null);
        
        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory ();
        
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory
                .createServerSocket (9999);
        
        SSLSocket sslsocket = (SSLSocket) serverSocket.accept ();

        InputStream inputstream = sslsocket.getInputStream ();
        InputStreamReader inputstreamreader = new InputStreamReader (
                inputstream);
        BufferedReader bufferedreader = new BufferedReader (inputstreamreader);

        String string = null;
        while ((string = bufferedreader.readLine ()) != null)
        {
            System.out.println (string);
            System.out.flush ();
        }
    }
}
