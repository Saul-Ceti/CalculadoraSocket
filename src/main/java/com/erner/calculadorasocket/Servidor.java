package com.erner.calculadorasocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    static final int PUERTO = 8080;
    private static ArrayList<PrintWriter> datosDeClientes = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Servidor de calculadora iniciado...");
        
        try (ServerSocket server = new ServerSocket(PUERTO)) {
            System.out.println("[Listo para recibir y responder peticiones]");
            
            int idSession = 0;

            while (true) {
                Socket socket = server.accept();
                System.out.println("Petición del socket: " + socket + " en la sesión: " + idSession);
                
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                datosDeClientes.add(writer);
                
                Thread thread = new HiloServidor(socket);
                thread.start();
                
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
