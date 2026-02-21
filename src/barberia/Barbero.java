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

    private final Barberia barberia;

    public Barbero(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {

        // Declaración de variables
        long tiempoPelado;
        String nombreCliente;

        // Mientras la barbería esté abierta el barbero trabaja
        while (barberia.isAbierta()) {
            // El barbero intenta atender a un cliente
            barberia.atender();
            
            if (!barberia.getListaEspera().isEmpty()){
            nombreCliente=barberia.extraerNombreCliente();

            System.out.println(String.format("%sEl barbero atiende al %s ...%s", "\u001B[32m",nombreCliente,"\u001B[0m"));

            // Se simula el tiempo de pelado
            tiempoPelado=((long) ((Math.random() * 3 + 1) * 1000));
            barberia.setTiempoPelado(tiempoPelado);
            try {
                // El barbero no está ocupado
                barberia.setOcupado(true);
                Thread.sleep(tiempoPelado);
            } catch (InterruptedException e) {
                System.out.println(String.format("Hilo %s interrumpido inesperadamente. Error: %s",
                        Thread.currentThread().getName(),
                        e.getMessage()));
                Thread.currentThread().interrupt();
            }
            // El barbero no está ocupado
            barberia.setOcupado(false);
            // El barbero avisa al cliente que su corte de pelo ha terminado
            barberia.avisar();
            // El barbero despide al cliente
            System.out.println(String.format("%s... El barbero ha terminado el corte de pelo del %s%s", "\u001B[33m",nombreCliente,"\u001B[0m"));
            // Se actualizan las variables
            barberia.setNumClientesAtendidos(barberia.getNumClientesAtendidos() + 1);
            barberia.setTotalDineroRecaudado(barberia.getNumClientesAtendidos() * barberia.getPrecioPelado());
            }
        }

        System.out.println("\nEl barbero dice: Mañana será otro día!");
    }

}
