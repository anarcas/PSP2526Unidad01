/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package barberia;

import java.util.InputMismatchException;
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
        int opcion;
        Scanner teclado = new Scanner(System.in);
        int numClientes;
        Barberia barberia = new Barberia();
        Thread hiloBarbero;
        Thread hiloCliente;
        Thread[] listaClientes;
        String nombreCliente;
        int contadorClientes = 0;
        String nombreBarbero;

        // Nombre del barbero
        nombreBarbero = "Barbero Juan";
        // Se instancia el hilo barbero
        hiloBarbero = new Thread(new Barbero(barberia), nombreBarbero);

        try {

            do {

                // Mostra menú de opciones
                opcion=barberia.mostrarMenuOpciones();
                

                switch (opcion) {

                    case 1:

                        hiloBarbero.start();
                        // Se reinicia la variable volverAlMenu
                        barberia.setVolverAlMenu(false);

                        // Preguntas al usuario
                        System.out.print("¿Cuántos clientes deseas enviar? ");
                        numClientes = teclado.nextInt();
                        listaClientes = new Thread[numClientes];
                        System.out.print("¿Cuántas sillas dispone la sala de espera? ");
                        barberia.setNumSillas(teclado.nextInt());

                        // Reinicio del contador de clientes
                        // Se lanzan hilos clientes
                        for (int i = 0; i < listaClientes.length; i++) {
                            contadorClientes++;
                            nombreCliente = String.format("Cliente%d", contadorClientes);
                            hiloCliente = new Thread(new Cliente(barberia), nombreCliente);
                            listaClientes[i] = hiloCliente;
                            listaClientes[i].start();
                        }
                        
                        // Los hilos clientes se esperan
                        for (int i = 0; i < listaClientes.length; i++) {
                            listaClientes[i].join();
                        }

                        break;

                    case 2:
                        barberia.mostrarBalance();

                        break;

                    case 3:
                        barberia.cerrarBarberia();
                        barberia.mostrarBalance();
                        // El hilo barbero espera solo cuando se cierre la barbería
                        hiloBarbero.join();
            
                        System.out.println("\n---BARBERIA CERRADA---\n");

                        break;

                    default:
                        System.out.println("Opción no válida.");

                }

            } while (opcion != 3 || barberia.isVolverAlMenu());

            
            // Cierre de recursos
            teclado.close();

        } catch (InputMismatchException e) {
            System.err.println("El usuario ha introducido un valor incorrecto. Error: " + e);
            // Limpieza del buffer
            teclado.nextInt();
            // Se alimenta no admitido para volver al menú
            opcion = -1;
        } catch (InterruptedException e) {
            System.err.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s", Thread.currentThread().getName(), e));
            Thread.currentThread().interrupt();
        }

    }

}
