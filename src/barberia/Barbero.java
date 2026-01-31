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
    private Barberia barberia;

    // Método constructor
    public Barbero(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        // Declaración de variables
        long tiempoCorte;
        String nombreCliente;

        // Mientras la barbería se encuentre abierta
        do {
            // Si la sala de espera se encuentra vacía el barbero se va a dormir
            if (barberia.getSalaEspera().isEmpty()) {
                barberia.dormir();
                // En caso contrario avisa a un cliente de la sala de espera e intenta cortarle el pelo
            } else {
                nombreCliente=barberia.avisarCliente();
                tiempoCorte = (long) ((int) (Math.random() * 3 + 1)) * 1000;
                try {
                    Thread.sleep(tiempoCorte);
                    barberia.cortarPelo(nombreCliente);
                    
                    // Si la sala de espera se queda vacía el barbero que debe lanzar el menú
                    if (barberia.getSalaEspera().isEmpty()) {
                        barberia.setVolverAlMenu(true);
                    }
                    
                    System.out.println(String.format("El %s se va a dormir tras cortar el pelo al %s",Thread.currentThread().getName(),nombreCliente));
                    barberia.dormir();
                } catch (InterruptedException e) {
                    System.err.println(String.format("El hilo %s ha sido interrumpido inesperadamente", Thread.currentThread().getName()));
                    Thread.currentThread().interrupt();
                }
            }
        } while (!barberia.isCerrado() && !barberia.isVolverAlMenu());

        // Mensaje de despedida
        System.out.println(String.format("El %s solicita más clientes o el cierre de la barberia.", Thread.currentThread().getName()));

    }

}
