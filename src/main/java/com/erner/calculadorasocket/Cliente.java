package com.erner.calculadorasocket;

import java.util.ArrayList;

public class Cliente {

    static final int MAX_HILOS = 10;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        ArrayList<Thread> clientes = new ArrayList<Thread>();
        for (int i = 0; i < 2; i++) {
            clientes.add(new HiloCliente(i));
        }
        for (Thread thread : clientes) {
            thread.start();
        }
    }
}
