package junk;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Client
{
    public static void main (String[] args) throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance ("JKS");
        
        keyStore.load (new FileInputStream ("clientkeystore.dat"), "secret".toCharArray ());
        
        SSLContext sslContext = SSLContext.getInstance ("SSLv3");
        
        sslContext.init (null, new TrustManager [] {new X509TrustManagerWrapper (keyStore)}, null); 

        SSLSocketFactory socketFactory = sslContext.getSocketFactory ();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 9999);
        
        socket.setNeedClientAuth (false);
        socket.setWantClientAuth (false);

        InputStream inputStream = System.in;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        String string = null;
        while ((string = bufferedReader.readLine()) != null) {
            bufferedWriter.write(string + '\n');
            bufferedWriter.flush();
        }    
    }
    
    private static class X509TrustManagerWrapper implements X509TrustManager
    {
        private final KeyStore keyStore;
        
        public X509TrustManagerWrapper (KeyStore keyStore)
        {
            if (keyStore == null)
                throw new IllegalArgumentException ("Key store is null");
            
            this.keyStore = keyStore;
        }

        @Override
        public void checkClientTrusted (X509Certificate[] chain, String authType)
            throws CertificateException
        {
        }

        @Override
        public void checkServerTrusted (X509Certificate[] chain, String authType)
            throws CertificateException
        {
            try
            {
                if (keyStore.getCertificateAlias (chain [0]) == null)
                {
                    System.out.println ("Adding new certificate");
                    System.out.println (chain [0]);
                
                    KeyStore keyStore = KeyStore.getInstance ("JKS");
                    InputStream input = new FileInputStream ("clientkeystore.dat");
                    keyStore.load (input, "secret".toCharArray ());
                    input.close ();
                    keyStore.setCertificateEntry ("server", chain [0]);
                    OutputStream output = new FileOutputStream ("clientkeystore.dat"); 
                    keyStore.store (output, "secret".toCharArray ());
                    output.close ();
                }
            }
            catch (Throwable eex)
            {
                throw new RuntimeException (eex);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers ()
        {
            return null;
        }
    }
}
