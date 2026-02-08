/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia2;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author anaranjo
 */
public class Barberia {

    // Declaración de variables
    private int numClientesAtendidos;
    private float totalDineroRecaudado;
    private int numClientesNoAtendidos;
    private boolean ocupado;
    private final Deque<String> listaEspera = new LinkedList<>();
    private boolean abierta;
    private final int numSillas;
    private long tiempoPelado;
    private final float precioPelado;
    private int opcionElegida;

    // Método constructor
    public Barberia(int numSillas, float precio) {
        this.numSillas = numSillas;
        this.precioPelado = precio;
    }

    // Médodos getters y setters
    public synchronized int getNumClientesAtendidos() {
        return numClientesAtendidos;
    }

    public synchronized void setNumClientesAtendidos(int numClientesAtendidos) {
        this.numClientesAtendidos = numClientesAtendidos;
    }

    public synchronized float getTotalDineroRecaudado() {
        return totalDineroRecaudado;
    }

    public synchronized void setTotalDineroRecaudado(float totalDineroRecaudado) {
        this.totalDineroRecaudado = totalDineroRecaudado;
    }

    public synchronized int getNumClientesNoAtendidos() {
        return numClientesNoAtendidos;
    }

    public synchronized void setNumClientesNoAtendidos(int numClientesNoAtendidos) {
        this.numClientesNoAtendidos = numClientesNoAtendidos;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public synchronized Deque<String> getListaEspera() {
        return listaEspera;
    }

    public boolean isAbierta() {
        return abierta;
    }

    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    public int getNumSillas() {
        return numSillas;
    }

    public float getPrecioPelado() {
        return precioPelado;
    }

    public synchronized long getTiempoPelado() {
        return tiempoPelado;
    }

    public synchronized void setTiempoPelado(long tiempoPelado) {
        this.tiempoPelado = tiempoPelado;
    }

    public int getOpcionElegida() {
        return opcionElegida;
    }

    public void setOpcionElegida(int opcionElegida) {
        this.opcionElegida = opcionElegida;
    }

    // Método lanzador del menú principal
    public synchronized int mostrarMenu() {

        // Se declaran
        Scanner teclado;
        int opcion;

        // Se instancian variables
        teclado = new Scanner(System.in);

        // Se muestra el menú en consola
        do {
            System.out.println("\nMenú de opciones");
            System.out.println("\t1. Enviar X clientes");
            System.out.println("\t2. Consultar estadísticas");
            System.out.println("\t3. Cerrar la barberia");
            System.out.print("\t\tIntroduzca una opción: ");
            opcion = teclado.nextInt();
        } while (opcion < 1 || opcion > 3);

        // Resultado método
        return opcion;

    }

    // Método mostrar estadísticas
    public void mostrarEstadisticas() {

        System.out.println("\n---Estadísticas---");
        System.out.println(String.format("%-25s %d", "Clientes atendidos =", this.getNumClientesAtendidos()));
        System.out.println(String.format("%-25s %.2f", "Total dinero recaudado =", this.getTotalDineroRecaudado()));
        System.out.println(String.format("%-25s %d", "Clientes no atendidos =", this.getNumClientesNoAtendidos()));

    }

    // Método dormir del hilo barbero
    public synchronized void dormir() {

        // El barbero no está ocupado
        this.setOcupado(false);

        // Si la lista de espera está vacía el barbero duerme
        while (this.getListaEspera().isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.err.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s.",
                        Thread.currentThread().getName(),
                        e.getMessage()));
                Thread.currentThread().interrupt();
            }
        }

    }

    // Método para registar clientes en la lista de espera
    public synchronized void registarCliente(String nombre) {

        this.getListaEspera().add(nombre);

    }

    // Método para extraer un cliente de la lista de espera
    public synchronized String llamarCliente() {

        // Declaración de variables
        String nombreCliente;

        // Iniciación de variables
        nombreCliente = this.getListaEspera().pollFirst();

        // Se avisa al cliente que se encuentra en la sala de espera
        this.notifyAll();

        // Resultado del método
        return nombreCliente;

    }

    // Método para despertar al barbero
    public synchronized void despertarBarbero() {

        if (!this.isOcupado()) {
            this.notifyAll();
        }
    }

    // Método de espera de los hilos clientes a ser atendidos por el barbero
    public synchronized void esperar() {

        while (this.isOcupado()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.err.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s.",
                        Thread.currentThread().getName(),
                        e.getMessage()));
                Thread.currentThread().interrupt();
            }
        }

    }

    // Método para cerrar la barbería
    public synchronized void cerrar() {

        // Se cierra la barbería
        this.setAbierta(false);

        // Se avisan a todos los hilos que se encuentren esperando
        this.notifyAll();

    }

}
