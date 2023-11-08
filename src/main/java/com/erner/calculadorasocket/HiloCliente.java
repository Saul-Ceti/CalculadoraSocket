package com.erner.calculadorasocket;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloCliente extends Thread {

    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    private int id;

    public HiloCliente(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Calculadora Cliente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JTextField display = new JTextField();
            display.setEditable(false);
            frame.add(display, BorderLayout.NORTH);

            JPanel panel = new JPanel(new GridLayout(4, 4));

            String[] buttonLabels = {"7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"};

            for (String label : buttonLabels) {
                JButton button = new JButton(label);
                button.addActionListener(e -> {
                    try {
                        sk = new Socket("127.0.0.1", 8080);
                        dos = new DataOutputStream(sk.getOutputStream());
                        dis = new DataInputStream(sk.getInputStream());
                        
                        String result = display.getText();                        
                        dos.writeUTF(result);
                        
                        if (label.equals("C")) {
                            display.setText("");
                        } 
                        else if (label.equals("=")) {
                            dos.writeUTF(result);
                            int respuesta = dis.readInt();
                            display.setText(String.valueOf(respuesta));
                        } 
                        else {
                            String currentDisplay = display.getText();
                            display.setText(currentDisplay + label);
                        }

                        dis.close();
                        dos.close();
                        sk.close();
                    } catch (IOException ex) {
                        Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                panel.add(button);
            }

            frame.add(panel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new HiloCliente(1).start();
    }
}
