package com.erner.calculadorasocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiloServidor extends Thread {

    private Socket socket;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;

    //Constructor del hilo
    public HiloServidor(Socket socket) {
        this.socket = socket;

        //Se inicializan la entrada y salida de datos
        try {
            dataOutput = new DataOutputStream(socket.getOutputStream());
            dataInput = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String accion;

        try {
            accion = dataInput.readUTF();

            // Usamos una expresión regular para buscar el patrón de dos números y un operador matemático.
            Pattern pattern = Pattern.compile("(\\d+)([\\+\\-\\*\\/])(\\d+)");
            Matcher matcher = pattern.matcher(accion);

            if (matcher.find()) {
                int num1 = Integer.parseInt(matcher.group(1));
                String operador = matcher.group(2);
                int num2 = Integer.parseInt(matcher.group(3));

                int resultado = 0;
                switch (operador) {
                    case "+":
                        resultado = num1 + num2;
                        break;
                    case "-":
                        resultado = num1 - num2;
                        break;
                    case "*":
                        resultado = num1 * num2;
                        break;
                    case "/":
                        if (num2 != 0) {
                            resultado = num1 / num2;
                        } else {
                            dataOutput.writeUTF("No se puede dividir por cero");
                        }
                        break;
                    default:
                        dataOutput.writeUTF("Operación no válida");
                        break;
                }

                dataOutput.writeInt(resultado);
            } else {
                dataOutput.writeUTF("Expresión no válida");
            }

        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        desconectar();
    }
    
    //Función para cerrar la conexión del socket
    public void desconectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
