/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia2;

/**
 *
 * @author anaranjo
 */
public class Cliente implements Runnable {
    
    private final Barberia barberia;
    
    public Cliente(Barberia barberia) {
        this.barberia = barberia;
    }
    
    @Override
    public void run() {

        // Declaración de variables
        long tiempoLlegada;
        long tiempoPelado;

        // El cliente se toma su tiempo para llegar a la barbería ( entre 1 y 5 segundos)
        tiempoLlegada = (long) ((Math.random() * 5 + 1) * 1000);
        try {
            Thread.sleep(tiempoLlegada);
        } catch (InterruptedException e) {
            System.out.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                    Thread.currentThread().getName(),
                    e.getMessage()));
            Thread.currentThread().interrupt();
        }

        // El cliente llega a la barbería y saluda
        System.out.println(String.format("El %s dice: Hola!!", Thread.currentThread().getName()));

        // El cliente comprueba si la lista de espera está llena y si no lo está, anota su registro de llegada, en caso contrario se marcha enfadado
        if (barberia.getListaEspera().size() < barberia.getNumSillas()) {
            // El cliente se anota en la lista de espera
            barberia.registarCliente(Thread.currentThread().getName());
            // El cliente avisa al barbero que se encuentra en la lista de espera.
            barberia.despertarBarbero();
//            // Si el barbero está ocupado cortando el pelo el cliente se ubica en la sala de espera
//            barberia.esperar();
//            // El cliente es atendido y se simila el tiempo de pelado
//            tiempoPelado = barberia.getTiempoPelado();
//            try {
//                Thread.sleep(tiempoPelado);
//            } catch (InterruptedException e) {
//                System.out.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
//                        Thread.currentThread().getName(),
//                        e.getMessage()));
//                Thread.currentThread().interrupt();
//            }
//            // El cliente se despide agradecido y paga al barbero; se actualizan las variables de clientes atendidos y salario recaudado
//            System.out.println(String.format("\tEl %s dice: Muchas gracias barbero, hasta la próxima.", Thread.currentThread().getName()));
            
            
        } else {
            System.out.println(String.format("\tEl %s dice: Joder!! Siempre igual, nunca hay sitio en esta barbería..., bueno volveré otro día",
                    Thread.currentThread().getName()));
            // Se actualiza la variable de número de clientes no atendidos
            barberia.setNumClientesNoAtendidos(barberia.getNumClientesNoAtendidos() + 1);
        }
    }
    
}
