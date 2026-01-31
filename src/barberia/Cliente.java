/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia;

/**
 *
 * @author anaranjo
 */
public class Cliente implements Runnable {

    // Declaración de atributos
    private Barberia barberia;

    // Método constructor
    public Cliente(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        long tiempoLlegada;

        // Si la barbería se encuentra abierta el cliente entra en la sala de espera
        if (!barberia.isCerrado()) {
            // Simulación de tiempo del cliente para llegar a la barbería de 1 a 3 segundos
            try {
                tiempoLlegada = (long) ((int) (Math.random() * 3 + 1)) * 1000;
                Thread.sleep(tiempoLlegada);
            } catch (InterruptedException e) {
                System.out.println(String.format("El hilo %s ha sido interrumpido inesperadamente.", Thread.currentThread().getName()));
                Thread.currentThread().interrupt();
            }
            // El cliente entra en la sala de espera
            barberia.entrarSalaEspera();
            // En caso contrario se va enfadado
        } else {
            System.out.println(String.format("El %s dice: ¡Joder! siempre que vengo me encuentro la barbería cerrada.", Thread.currentThread().getName()));
            // Se actualiza la variable estática
            Barberia.setClientesNoAtendidos(Barberia.getClientesNoAtendidos() + 1);
        }

    }

}
