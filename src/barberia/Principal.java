/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package barberia;

import java.util.Scanner;

/**
 *
 * @author anaranjo
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Declaración de variables
        int numSillas;
        float precioPelado;
        Thread hiloBarbero;
        Thread hiloCliente;
        Scanner teclado;
        int opcion;
        int numClientes;
        Thread[] listaClientes;
        Barberia barberia;
        String nombreCliente;
        int contadorClientes;

        // Instanciación del recurso compartido
        teclado = new Scanner(System.in);
        System.out.println("¿Cuántas sillas dispone la barbería?");
        System.out.print("\tIntroduzca el número de sillas: ");
        numSillas = teclado.nextInt();
        System.out.println("¿Cuál es el precio del corte de pelo?");
        System.out.print("\tIntroduzca el precio del corte de pelo: ");
        precioPelado = teclado.nextFloat();
        barberia = new Barberia(numSillas, precioPelado);

        // Se abre la barbería y se pone el cartel de barbería abierta
        barberia.setAbierta(true);
        System.out.println("\n--- BARBERÍA ABIERTA ---");
        Barberia.setContadorClientes(0);

        // Se lanza el hilo barbero
        hiloBarbero = new Thread(new Barbero(barberia));
        hiloBarbero.start();
        // El barbero no está ocupado
        barberia.setOcupado(false);

        // Mientra la barbería esté abierta seguiran llegando clientes
        while (barberia.isAbierta()) {

            // Mostrar el menú
            opcion = barberia.mostrarMenu();
            
            // Estructura condicional en función de la opción seleccionada
            switch (opcion) {

                case 1:
                    // Se solicitan el número de clientes a lanzar
                    System.out.println("¿Cuántos clientes desea enviar a la barbería?");
                    System.out.print("\tNúmero de clientes: ");
                    numClientes = teclado.nextInt();
                    listaClientes = new Thread[numClientes];

                    // Se lanzan los hilos clientes
                    for (int i = 0; i < listaClientes.length; i++) {
                        Barberia.setContadorClientes(Barberia.getContadorClientes()+1);
                        contadorClientes=Barberia.getContadorClientes();
                        nombreCliente = String.format("Cliente %d", contadorClientes);
                        hiloCliente = new Thread(new Cliente(barberia), nombreCliente);
                        listaClientes[i] = hiloCliente;
                        listaClientes[i].start();
                        
                    }

                    // Los hilos clientes se esperan
                    for (Thread hilo : listaClientes) {
                        try {
                            hilo.join();
                            // Pausa de cortesía para asegurar que el hilo barbero haya concluido y se encuentre en su wait()
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.err.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                                    Thread.currentThread().getName(),
                                    e.getMessage()));
                            Thread.currentThread().interrupt();
                        }
                    }

                    
                    System.out.println(String.format("%sSimulación de clientes terminada.%s","\u001B[32m","\u001B[0m"));
                    
                    break;

                case 2:
                    // Se muestran las estadísticas
                    barberia.mostrarEstadisticas();

                    break;

                case 3:
                    // Cerrar la barbería
                    barberia.cerrar();
                    try {
                        hiloBarbero.join();
                    } catch (InterruptedException e) {
                        System.err.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                                Thread.currentThread().getName(),
                                e.getMessage()));
                        Thread.currentThread().interrupt();
                    }

                    break;

                default:

                    System.err.println("Opción no válida");

            }

        }

        // Se muestran las estadísticas y se cuelga el cartel de barbería cerrada
        barberia.mostrarEstadisticas();
        System.out.println("\n--- BARBERÍA CERRDADA ---");

    }

}
