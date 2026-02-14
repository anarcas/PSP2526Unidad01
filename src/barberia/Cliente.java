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

    private final Barberia barberia;

    public Cliente(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        // Declaración de variables
        long tiempoLlegada;
        long tiempoPelado;

        // El cliente se toma su tiempo para llegar a la barbería (entre 1 y 5 segundos)
        tiempoLlegada = (long) ((Math.random() * 5 + 1) * 1000);
        try {
            Thread.sleep(tiempoLlegada);
        } catch (InterruptedException e) {
            System.out.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                    Thread.currentThread().getName(),
                    e.getMessage()));
            Thread.currentThread().interrupt();
        }

        // Si la barbería está cerrada, el cliente volverá otro día
        if (!barberia.isAbierta()) {
            System.out.println(String.format("El %s dice: vaya, la barbería está cerrada, volveré otro día.", Thread.currentThread().getName()));
        } else {
            // El cliente entra en la barbería y saluda
            System.out.println(String.format("El %s dice: Hola!!", Thread.currentThread().getName()));
            // El cliente comprueba si la lista de espera está llena y si no lo está, anota su registro de llegada, en caso contrario se marcha enfadado
            if (barberia.getListaEspera().size() < barberia.getNumSillas()) {
                // El cliente se anota en la lista de espera y avisa al barbero
                barberia.registrar(Thread.currentThread().getName());
                // El cliente es atendido
                // Se le atribuye un segundo más al cliente para garantizar que el barbero duerme antes de haber sido atendido el cliente y por lo tanto antes de mostrarse el menú
                tiempoPelado = barberia.getTiempoPelado();
                try {
                    Thread.sleep(tiempoPelado);
                } catch (InterruptedException e) {
                    System.out.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                            Thread.currentThread().getName(),
                            e.getMessage()));
                    Thread.currentThread().interrupt();
                }
                System.out.println(String.format("%sEl %s dice: gracias barbero por el corte de pelo%s",
                        "\u001B[36m",
                        Thread.currentThread().getName(),
                        "\u001B[0m"));
            } else {
                System.out.println(String.format("%sEl %s dice: Joder!! Siempre igual, nunca hay sitio en esta barbería..., bueno..., volveré otro día%s",
                        "\u001B[31m",
                        Thread.currentThread().getName(),
                        "\u001B[0m"));
                // Se actualiza la variable de número de clientes no atendidos
                barberia.setNumClientesNoAtendidos(barberia.getNumClientesNoAtendidos() + 1);
            }
            // Se imprime por consola la finalización del método run del hilo cliente
            //System.out.println(String.format("\tEl %s se terminado su método run()",Thread.currentThread().getName()));
        }
    }

}
