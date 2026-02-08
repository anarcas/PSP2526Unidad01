/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barberia;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author anaranjo
 */
public class Barberia {

    // Declaración de atributos públicos estáticos
    public static int clientesAtendidos = 0;
    public static double dineroRecaudado = 0d;
    public static int clientesNoAtendidos = 0;

    // Declaración de atributos
    private boolean cerrado = false;
    private Deque<String> salaEspera = new LinkedList<>();
    private boolean volverAlMenu = false;
    private int numSillas;
    private boolean ocupado = false;

    // Métodos Getters y Setters
    public static synchronized int getClientesAtendidos() {
        return clientesAtendidos;
    }

    public static synchronized void setClientesAtendidos(int clientesAtendido) {
        Barberia.clientesAtendidos = clientesAtendido;
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

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    // Método sincronizado cerrarBarberia()
    public synchronized void cerrarBarberia() {

        // Se cierra la barberia
        this.setCerrado(true);
        // Mensaje de cierre
        System.out.println("Se va a proceder a cerrar la barbería.");
        // Se vacia la lista de espera como clinetes no atendidos
        for (String cliente : this.getSalaEspera()) {
            // Mensaje de cliente desantendido
            System.out.println(String.format("El %s dice: ¡Joder! depués de tanto esperar me tengo que ir sin ser servido.",
                    cliente));
            // Se actualiza la variable estática
            Barberia.setClientesNoAtendidos(Barberia.getClientesAtendidos() + 1);
        }

        // Se avisa a todos los hilos que se encuentren en su wait() que deben terminar su método run()
        notifyAll();

    }

    // Método sincronizado para que el cliente guarde la cola hasta ser avisado por el barbero
    public synchronized void entrarSalaEspera() {

        if (this.getSalaEspera().size() < this.getNumSillas()) {
            // Mensaje de entrada del cliente en la sala de espera
            System.out.println(String.format("El %s entra en la sala de espera.", Thread.currentThread().getName()));

            // El cliente registra su nombre en la lista de epsera
            this.getSalaEspera().add(Thread.currentThread().getName());
            // La sala de espera ya no se encuentra vacía, por lo tanto, se reestablece la variable volverAlMenu = false
            this.setVolverAlMenu(false);

            // El cliente avisa al barbero (y al resto de clientes) que se encuentra en la sala de espera
            notifyAll();

            try {
                // Mientra el barbero no tenga habilitado el booleano para mostrar el menú, mantenemos a los clientes en espera para que no finalicen su método run()r
                while (this.isOcupado()) {

                    wait();
                }

                // Mientra el barbero no tenga habilitado el booleano para mostrar el menú, mantenemos a los clientes en espera para que no finalicen su método run()r
                while (this.isVolverAlMenu()) {

                    wait();
                }
                // Mensaje educado de despedida tras haber sido atendido
//                System.out.println(String.format("El %s dice: Muchas gracias barbero hasta la próxima.",
//                        Thread.currentThread().getName()));
            } catch (InterruptedException e) {
                System.out.println(String.format("El hilo %s ha sido interrumpido inesperadamente.", Thread.currentThread().getName()));
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println(String.format("El %s se va de la barberia porque no hay sitio en la sala de espera.", Thread.currentThread().getName()));
            Barberia.setClientesNoAtendidos(Barberia.getClientesNoAtendidos() + 1);
        }
    }
    // Método sincronizado barbero duerme

    public synchronized void dormir() {
        // Se establece el barbero como no ocupado
        this.setOcupado(false);
        // Mientras la sala de espera esté vacía y la barbería abierta el barbero duerme
        while (this.getSalaEspera().isEmpty() && !this.isCerrado()) {
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

    // Método que manda al barbero a dormir una siesta después de cada pelado y oblica a volver al menú
    public synchronized void siesta() {

        // Se muestra el menú
        this.mostrarMenu();

        // El barbero se va a dormir
        this.dormir();

    }

    // Método mostrar menú
    public synchronized void mostrarMenu() {
        // Volver al menú principal
        this.setVolverAlMenu(true);
        // Avisa a los clientes que pueden terminar su método run()
        notifyAll();
    }

    // Método para avisar a un cliente de la sala de espera para ser atendido
    public synchronized String avisarCliente() {
        
        // Avisa a un cliente de la sala de espera para cortarle el pelo
        String nombreCliente = this.getSalaEspera().pollFirst();

        // Mensaje de cliente avisado
        System.out.println(String.format("El %s ha sido avisado por %s para cortarle el pelo y sale de la sala de espera.",
                nombreCliente,
                Thread.currentThread().getName()));
        // Se avisa al cliente que está en estado de espera en la sala de espera
        notifyAll();
        // Se establece el barbero como ocupado
        this.setOcupado(true);

        return nombreCliente;

    }

    // Método sincronizado barbero corta el pelo a un cliente
    public synchronized void cortarPelo(String nombreCliente) {

        // Se actualizan las variables estáticas
        Barberia.setClientesAtendidos(Barberia.getClientesAtendidos() + 1);
        Barberia.setDineroRecaudado(Barberia.getDineroRecaudado() + 5d);
        // Mensaje de corte finalizado
        System.out.println(String.format("Se le acaba de cortar el pelo al %s", nombreCliente));
        // El barbero vuelte a estar operativo
        this.setOcupado(false);

    }

    // Método sincronizado para mostrar el balance del día
    public synchronized void mostrarBalance() {

        System.out.println("\nBALANCE DEL DÍA");
        System.out.println(String.format("Clientes atendidos = %d", Barberia.getClientesAtendidos()));
        System.out.println(String.format("Dinero recaudado = %.2f", Barberia.getDineroRecaudado()));
        System.out.println(String.format("Clientes no atendidos = %d", Barberia.getClientesNoAtendidos()));

    }
    
    public synchronized int mostrarMenuOpciones(){
        
        Scanner teclado=new Scanner(System.in);
        int option;
        
                // Menú
                System.out.println("\nMenú");
                System.out.println("1. Enviar clientes");
                System.out.println("2. Consultar estadísticas");
                System.out.println("3. Cerrar barbería");
                System.out.print("Introduce una opción: ");
                option = teclado.nextInt();
                
                return option;
    }
}
