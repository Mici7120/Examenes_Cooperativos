/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Vista.GUIServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author villa
 */
public class ConexionesServidor implements ActionListener {

    private ServerSocket serverSocket;
    private int estudiantes = 0;
    private HiloServidor hilo;

    private MulticastSocket socketCast;

    private DatagramPacket datagrama;
    byte[] vacio = new byte[0];

    GUIServer interfaz;

    public ConexionesServidor(GUIServer gui) {
        interfaz = gui;
        interfaz.asignarEscuchasBotones(this);
        ejecutarServidor();
    }

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

            interfaz.appendEstadoServidor("Iniciado servidor por el puerto " + serverSocket.getLocalPort());

            while (estudiantes < 3) {
                try {
                    interfaz.appendEstadoServidor("\nEsperando conexiones...\n");
                    Socket socket = serverSocket.accept(); // permite al servidor aceptar la conexión                    
                    estudiantes++;
                    hilo = new HiloServidor(socket, estudiantes, interfaz, socketCast, datagrama);
                    interfaz.appendEstadoServidor("\n Conectado el estudiante: " + estudiantes);

                } catch (EOFException excepcionEOF) {
                    System.out.println("\nServidor termino la conexion");
                } // fin de catch

            } // fin de while
        } catch (IOException exepcionES) {
            System.out.println(exepcionES.getMessage());
        } // fin de catch
    } // fin del método ejecutarServidor

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == interfaz.getBAgregarExamen()) {
            interfaz.getNombreExamen();
            interfaz.setNumeroPregunta(0);
            //interfaz.borrarCampo(interfaz.pregunta);
        }else if(ae.getSource() == interfaz.getBIniciarExamen()){
            //ejecutarServidor();
        }else if(ae.getSource() == interfaz.getBAgregarPregunta()){
            interfaz.setNumeroPregunta(1);
            interfaz.borrarOpcionesPregunta();
        }
        
    }

}
