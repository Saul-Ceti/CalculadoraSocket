package com.erner.calculadorasocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    static final int PUERTO = 8080;
    
    public static void main(String[] args) {
        ServerSocket server;
        System.out.println("Servidor de calculadora prendido...");
        
        try {
            server = new ServerSocket(PUERTO);
            System.out.println("\t[OK]");
            
            int idSession = 0;
            
            while (true) {
                Socket socket;
                socket = server.accept();
                System.out.println("Nueva conexión entrante: " + socket);
                
                ((HiloServidor) new HiloServidor(socket, idSession)).start();
                
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
