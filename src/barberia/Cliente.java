/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia;

/**
 * Clase hilo cliente. Importante: para que se muestre el menú se deben
 * finalizar todos los métodos run() de todos los clientes, pero no debe
 * finalizar el método run del hilo barbero, por lo tanto, los hilos clientes se
 * limitan a registrar su nombre en una lista de espera (sala de espera) y
 * posteriormente terminarán su método run, no deben esperar con un wait() a ser
 * atendidos por el barbero. Para mostrar el menú de nuevo no solo deben haber
 * terminado todos los hilos clientes, sino que también se necesitará de un
 * booleano que determine cuándo mostrar el menú, que será cada vez que el
 * barbero se vaya a dormir después de un corte de pelo.
 *
 *
 * @author anaranjo
 */
public class Cliente implements Runnable {

    // Declaración de atributos
    private final Barberia barberia;

    // Método constructor
    public Cliente(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        // Declaración de variables
        long tiempoLlegada;

        // Simulación de tiempo del cliente para llegar a la barbería de 1 a 8 segundos
        try {
            tiempoLlegada = (long) ((int) (Math.random() * 8 + 1)) * 1000;
            Thread.sleep(tiempoLlegada);

            // El cliente entra en la sala de espera y registra su nombre en la lista de espera, luego termina su método run()
            barberia.entrarSalaEspera();

        } catch (InterruptedException e) {
            System.out.println(String.format("El hilo %s ha sido interrumpido inesperadamente.", Thread.currentThread().getName()));
            Thread.currentThread().interrupt();
        }
    }

}

