/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia;

import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author anaranjo
 */
public class Barberia {

    // Declaración de atributos públicos estáticos
    public static int clientesAtendido = 0;
    public static double dineroRecaudado = 0d;
    public static int clientesNoAtendidos = 0;

    // Declaración de atributos
    private boolean cerrado = false;
    private Deque<String> salaEspera = new LinkedList<>();
    private boolean volverAlMenu=false;
    private int numSillas=10;

    // Métodos Getters y Setters
    public static int getClientesAtendido() {
        return clientesAtendido;
    }

    public static void setClientesAtendido(int clientesAtendido) {
        Barberia.clientesAtendido = clientesAtendido;
    }

    public static double getDineroRecaudado() {
        return dineroRecaudado;
    }

    public static void setDineroRecaudado(double dineroRecaudado) {
        Barberia.dineroRecaudado = dineroRecaudado;
    }

    public static synchronized int getClientesNoAtendidos() {
        return clientesNoAtendidos;
    }

    public static synchronized void setClientesNoAtendidos(int clientesNoAtendidos) {
        Barberia.clientesNoAtendidos = clientesNoAtendidos;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }

    public synchronized Deque<String> getSalaEspera() {
        return salaEspera;
    }

    public boolean isVolverAlMenu() {
        return volverAlMenu;
    }

    public void setVolverAlMenu(boolean volverAlMenu) {
        this.volverAlMenu = volverAlMenu;
    }

    public synchronized int getNumSillas() {
        return numSillas;
    }

    public synchronized void setNumSillas(int numSillas) {
        this.numSillas = numSillas;
    }

    
    
    // Método sincronizado cerrarBarberia()
    public synchronized void cerrarBarberia() {

        // Se cierra la barberia
        this.setCerrado(true);
        // Se avisa a todos los clintes que se encuentren en la lista de espera que vuelvan otro día
        notifyAll();
        // Mensaje de cierre
        System.out.println(String.format("El %s limpia la barberia y se dispone a cerrar.",Thread.currentThread().getName()));

    }

    // Método sincronizado para que el cliente guarde la cola hasta ser avisado por el barbero
    public synchronized void entrarSalaEspera() {

        if (this.getSalaEspera().size()<this.getNumSillas()){
        // Mensaje de entrada en la sala de espera
        System.out.println(String.format("El %s entra en la sala de espera.", Thread.currentThread().getName()));

        // El cliente entra en la sala de espera
        this.getSalaEspera().add(Thread.currentThread().getName());
        // La sala de espera ya no se encuentra vacía, por lo tanto, se volverAlMenu se reinicia
        this.setVolverAlMenu(false);
        // El cliente avisa al barbero (y al resto de clientes) que se encuentra en la sala de espera
        notifyAll();
        } else {
            System.out.println(String.format("El %s se va de la barberia porque no hay sitio en la sala de espera.",Thread.currentThread().getName()));
            Barberia.setClientesNoAtendidos(Barberia.getClientesNoAtendidos()+1);
        }
    }

    // Método sincronizado barbero duerme
    public synchronized void dormir() {
        // Mientras la sala de espera esté vacía y la barbería abierta el barbero duerme
        while (this.getSalaEspera().isEmpty() && !this.isCerrado() && !this.isVolverAlMenu()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("El hilo barbero ha sido interrumpido inesperadamente. Error: " + e);
                Thread.currentThread().interrupt();
            }
        }
        // Mensaje barbero se acaba de despertar
        System.out.println(String.format("El %s se acaba de despertar de una siesta.", Thread.currentThread().getName()));
    }

    // Método para avisar a un cliente de la sala de espera para ser atendido
    public String avisarCliente() {
        // Avisa a un cliente de la sala de espera para cortarle el pelo
        String nombreCliente = this.getSalaEspera().pollFirst();
        // Mensaje de cliente avisado
        System.out.println(String.format("El %s ha sido avisado para cortarle el pelo y sale de la sala de espera.", nombreCliente));
        
        return nombreCliente;

    }

    // Método sincronizado barbero corta el pelo a un cliente
    public synchronized void cortarPelo(String nombreCliente) {

        // Se actualizan las variables estáticas
        Barberia.setClientesAtendido(Barberia.getClientesAtendido() + 1);
        Barberia.setDineroRecaudado(Barberia.getDineroRecaudado() + 5d);
        // Mensaje de corte finalizado
        System.out.println(String.format("Se le acaba de cortar el pelo al %s", nombreCliente));

    }

    // Método sincronizado para mostrar el balance del día
    public synchronized void mostrarBalance() {

        System.out.println("\nBALANCE DEL DÍA");
        System.out.println(String.format("Clientes atendidos = %d", Barberia.getClientesAtendido()));
        System.out.println(String.format("Dinero recaudado = %.2f", Barberia.getDineroRecaudado()));
        System.out.println(String.format("Clientes no atendidos = %d", Barberia.getClientesNoAtendidos()));

    }
}
