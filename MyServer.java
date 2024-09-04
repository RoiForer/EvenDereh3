

package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {

    private int port;
    private volatile boolean stop;
    private ClientHandler ch;
    

    public MyServer (int port, ClientHandler ch) {
        this.port = port;
        this.ch=ch;
        this.stop = false;

    }

    private void startServer()throws Exception {
        ServerSocket server= new ServerSocket(port);
        server.setSoTimeout(1000);
        
        while (!stop) {
            try {
                Socket aClient = server.accept();
                try {
                    ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    aClient.getInputStream().close();
                    aClient.getOutputStream().close();
                    aClient.close();
                } catch (IOException e) {}
            } catch (SocketTimeoutException e) {}
        }
        server.close();
    }

    public void start() {
       Thread t = new Thread(()-> {
        try {
            startServer();
        }catch (Exception e) {}
       });
       t.start();
    }

   

    public void close() {
         stop = true; // Set the stop flag to true to stop the server from accepting new connections


    }
    
}