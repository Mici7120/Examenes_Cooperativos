/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Vista.GUIServer;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author villa
 */
public class ConexionesServidor {

    private ServerSocket serverSocket;
    private int estudiantes = 0;
    private HiloServidor hilo;

    private MulticastSocket socketCast;

    private DatagramPacket datagrama;
    byte[] vacio = new byte[0];
    
    private GUIServer interfaz = new GUIServer();

    /**
     * Método que crea el serverSocket, se queda a espera de que un cliente se
     * conecte por el puerto e invoca al método que procesa las solicitudes del
     * cliente
     */
    public void ejecutarServidor() {
        //-------------------------------MULTICAST -----------------------
        /**
         * vamos a hacer la creación del socket de multicast y el datagrama
         * declarado
         */
        try {
            socketCast = new MulticastSocket();
            // Creamos la ip multicast donde se va a poner el mensaje
            InetAddress ipDestino = InetAddress.getByName("231.0.0.1");
            /**
             * parámetros: datos, longitud de los datos, ip multicast destino,
             * puerto
             */
            datagrama = new DatagramPacket(vacio, 0, ipDestino, 10000);

        } catch (IOException ex) {
            System.out.println("error al crear socket de multicast" + ex);
        }
        //-----------------Fin creación multicast -----------------------------------------------    

        try {
            serverSocket = new ServerSocket(12345);
            interfaz.estadoServidor.append("Iniciado servidor por el puerto " + serverSocket.getLocalPort());

            while (estudiantes < 4) {
                try {
                    interfaz.estadoServidor.append("\nEsperando conexiones...\n");
                    Socket socket = serverSocket.accept(); // permite al servidor aceptar la conexión                    
                    estudiantes++;
                    hilo = new HiloServidor(socket, estudiantes, interfaz, socketCast, datagrama);
                    interfaz.estadoServidor.append("\n Conectado el estudiante: " + estudiantes);

                } catch (EOFException excepcionEOF) {
                    System.out.println("\nServidor termino la conexion");
                } // fin de catch

            } // fin de while
        } catch (IOException exepcionES) {
            System.out.println(exepcionES.getMessage());
        } // fin de catch
    } // fin del método ejecutarServidor
    
    public static void main(String[] args) {
        ConexionesServidor conexiones = new ConexionesServidor();
        conexiones.ejecutarServidor();
    }
}
