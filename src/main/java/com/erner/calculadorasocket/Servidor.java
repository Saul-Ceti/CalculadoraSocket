package com.erner.calculadorasocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor {

    static final int PUERTO = 8080;
    static private DataInputStream dataInput;
    static private DataOutputStream dataOutput;

    public static void main(String[] args) {
        int primerNumero = 0, segundoNumero = 0, agregado = 0, respuestas = 0;
        boolean listo = false;
        String primerOperador, segundoOperador;

        System.out.println("Servidor de calculadora iniciado...");

        try (ServerSocket server = new ServerSocket(PUERTO)) {
            System.out.println("[Listo para recibir y responder peticiones]");

            int idSession = 0;

            while (true) {
                Socket socket = server.accept();
                dataOutput = new DataOutputStream(socket.getOutputStream());
                dataInput = new DataInputStream(socket.getInputStream());

                String datos = dataInput.readUTF();

                if (!datos.equals("") && !listo) {
                    if (datos.matches("\\d+")) { // Verifica si datos contiene solo dígitos
                        if (agregado == 1) {
                            segundoNumero = Integer.parseInt(datos);
                            agregado = 0;
                            listo = true;
                        } else if (agregado == 0) {
                            primerNumero = Integer.parseInt(datos);
                            agregado++;
                        }
                    } else {
                        System.out.println("Error: La cadena no contiene solo números.");
                    }
                }

                if (listo) {
                    Pattern pattern = Pattern.compile("([\\+\\-\\*\\/])");
                    Matcher matcher = pattern.matcher(datos);

                    if (matcher.find()) {
                        String operador = matcher.group(1);

                        int resultado = 0;
                        switch (operador) {
                            case "+":
                                resultado = primerNumero + segundoNumero;
                                respuestas++;
                                break;
                            case "-":
                                resultado = primerNumero - segundoNumero;
                                respuestas++;
                                break;
                            case "*":
                                resultado = primerNumero * segundoNumero;
                                respuestas++;
                                break;
                            case "/":
                                if (segundoNumero != 0) {
                                    resultado = primerNumero / segundoNumero;
                                    respuestas++;
                                } else {
                                    dataOutput.writeUTF("No se puede dividir por cero");
                                }
                                break;
                            default:
                                dataOutput.writeUTF("Operación no válida");
                                break;
                        }

                        dataOutput.writeInt(resultado);
                        if (respuestas == 2) {
                            listo = false;
                            primerNumero = 0;
                            segundoNumero = 0;
                            agregado = 0;
                            respuestas = 0;
                        }
                    } else {
                        dataOutput.writeUTF("Expresión no válida");
                    }
                }

//                Thread thread = new HiloServidor(socket, primerNumero, segundoNumero);
//                thread.start();
                System.out.println("Petición del socket: " + socket + " en la sesión: " + idSession + " con el mensaje: " + datos);
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
