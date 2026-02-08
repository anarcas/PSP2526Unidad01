/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia2;

/**
 *
 * @author anaranjo
 */
public class Barbero implements Runnable {

    private final Barberia barberia;

    public Barbero(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        // Declaración de variables
        long tiempoPelado;
        String nombreCliente;

        // Mientras la barbería esté abierta el barbero comprueba la lista de espera y trata de atender a un cliente, si no hay clientes se va a dormir
        while (barberia.isAbierta()) {
            // El barbero comprueba la lista de espera y si no hay clientes se va a dormir
            barberia.dormir();
            // Si hay clientes en la lista de espera, extrae el registro y se le avisa para que salga de su método wait()
            nombreCliente = barberia.llamarCliente();
            // El barbero está ocupado atendiendo al cliente y le corta el pelo
            barberia.setOcupado(true);
            //System.out.println(String.format("El barbero atiende al %s", nombreCliente));
            tiempoPelado = (long) ((Math.random() * 3 + 1) * 1000);
            barberia.setTiempoPelado(tiempoPelado);
            try {
                Thread.sleep(tiempoPelado);
            } catch (InterruptedException e) {
                System.out.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                        Thread.currentThread().getName(),
                        e.getMessage()));
                Thread.currentThread().interrupt();
            }
            // El barbero despide al cliente
            //System.out.println(String.format("El barbero ha atendido al %s", nombreCliente));
            // Se actualizan las variables
            barberia.setNumClientesAtendidos(barberia.getNumClientesAtendidos() + 1);
            barberia.setTotalDineroRecaudado(barberia.getNumClientesAtendidos() * barberia.getPrecioPelado());

        }

        System.out.println("El barbero dice: Mañana será otro día!");
    }

}
