/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia;

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
    private final float precioPelado;
    private long tiempoPelado;
    private String nombreCliente;
    private static int contadorClientes;

    // Método constructor
    public Barberia(int numSillas, float precio) {
        this.numSillas = numSillas;
        this.precioPelado = precio;
    }

    // Médodos getters y setters
    public static int getContadorClientes() {
        return contadorClientes;
    }

    public static void setContadorClientes(int contadorClientes) {
        Barberia.contadorClientes = contadorClientes;
    }

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

    public synchronized boolean isOcupado() {
        return this.ocupado;
    }

    public synchronized void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public synchronized Deque<String> getListaEspera() {
        return this.listaEspera;
    }

    public boolean isAbierta() {
        return this.abierta;
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

    public long getTiempoPelado() {
        return this.tiempoPelado;
    }

    public void setTiempoPelado(long tiempoPelado) {
        this.tiempoPelado = tiempoPelado;
    }

    public synchronized String getNombreCliente() {
        return nombreCliente;
    }

    public synchronized void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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
    public synchronized void mostrarEstadisticas() {

        System.out.println("\n--- Estadísticas ---");
        System.out.println(String.format("%-25s %d", "Clientes atendidos =", this.getNumClientesAtendidos()));
        System.out.println(String.format("%-25s %.2f", "Total dinero recaudado =", this.getTotalDineroRecaudado()));
        System.out.println(String.format("%-25s %d", "Clientes no atendidos =", this.getNumClientesNoAtendidos()));

    }

    // Método atender del hilo barbero
    public synchronized void atender() {

        // Mientras la sala de espera esté vacía y la barbería esté abierta, el barbero duerme
        while (this.getListaEspera().isEmpty() && this.isAbierta()) {
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

    // Método para extraer el nombre del cliente de la lista de registros de la sala de espera
    public String extraerNombreCliente() {
        
        // Se extrae el nombre del cliente de la lista de espera por orden de llegada
        return this.getListaEspera().pollFirst();
    }

    // Método sincronizado para avisar a un cliente
    public synchronized void avisar() {

        // El barbero avisa uno a uno a los clientes que se encuentran en la sala de espera
        this.notify();

    }

    // Método para registar clientes en la lista de espera
    public synchronized void registrar(String nombre) {

        // El cliente se registra en la lista de espera
        this.getListaEspera().addLast(nombre);
        // El cliente avisa al barbero de su llegada y registro, debe ser un notifyAll() para asegurar que el barbero queda avisado
        this.notifyAll();

        // Mientras el barbero esté ocupado, el cliente espera en la sala de espera
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

        // Se avisa solo al supuestamente hilo barbero que se encuentra durmiendo
        this.notify();

    }

}
