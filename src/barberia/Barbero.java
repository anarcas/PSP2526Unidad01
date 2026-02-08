/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia;

/**
 *
 * @author anaranjo
 */
public class Barbero implements Runnable {

    // Declaración de atributos
    private final Barberia barberia;

    // Método constructor
    public Barbero(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        // Declaración de variables
        long tiempoCorte;
        String nombreCliente;
        long tiempoSiesta;

        // Mientras la barbería se encuentre abierta
        do {
            // Si la sala de espera se encuentra vacía el barbero se va a dormir
            if (barberia.getSalaEspera().isEmpty()) {
                barberia.dormir();
                // En caso contrario avisa a un cliente de la sala de espera e intenta cortarle el pelo
            } else {
                try {
                    nombreCliente = barberia.avisarCliente();
                    // Simulación del tiempo de corte de pelo
                    tiempoCorte = (long) ((int) (Math.random() * 3 + 1)) * 1000;
                    Thread.sleep(tiempoCorte);
                    barberia.cortarPelo(nombreCliente);
                    // Tras el corte de pelo el barbero se va a sestear
                    System.out.println(String.format("El %s se va a dormir una siesta tras cortar el pelo al %s y se mostrará el menú.",
                            Thread.currentThread().getName(),
                            nombreCliente));
                    // Simulación de dormir la siesta entre 1 y 2 segundos
                    tiempoSiesta = (long) ((int) (Math.random() * 2 + 1)) * 1000;
                    Thread.sleep(tiempoSiesta);
                    barberia.siesta();

                    // Si la sala de espera se queda vacía el barbero que debe lanzar el menú
                    if (barberia.getSalaEspera().isEmpty()) {
                        barberia.mostrarMenu();
                        barberia.mostrarMenuOpciones();
                    }

                } catch (InterruptedException e) {
                    System.err.println(String.format("El hilo %s ha sido interrumpido inesperadamente", Thread.currentThread().getName()));
                    Thread.currentThread().interrupt();
                }
            }
        } while (!barberia.isCerrado());

        // Mensaje de despedida
        System.out.println(String.format("El %s limpia la barbería y se marcha hasta el día siguiente.", Thread.currentThread().getName()));

    }

}
