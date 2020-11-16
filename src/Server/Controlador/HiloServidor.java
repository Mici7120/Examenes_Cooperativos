/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Vista.GUIServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author villa
 */
public class HiloServidor extends Thread {

    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private Socket sCliente;
    private int idCliente;
    private GUIServer interfaz;
    private Examen examen;
    private int preguntaSeleccionada;

    //--------------------multicast -------------------------------
    private MulticastSocket sMulti;
    private DatagramPacket datagrama;
    //------------------------------------------------

    public HiloServidor(Socket socket, int numeroEstudiante, GUIServer interfaz, MulticastSocket multi, DatagramPacket paquete, Examen examen) {

        sCliente = socket;
        idCliente = numeroEstudiante;
        this.interfaz = interfaz;
        this.examen = examen;
        //-----------------Multicast ----------------------
        sMulti = multi;
        datagrama = paquete;
        //-----------------------------------------------
    }

    @Override
    public void run() {
        interfaz.appendEstadoServidor("\n Server hilo" + idCliente + " por el puerto " + sCliente.getPort() + " iniciado.");
        abrirFlujos();
        
        String mensaje = "";
        do {
            try {
                mensaje = (String) entrada.readObject();
                interfaz.appendEstadoServidor("\nCliente " + idCliente + ": " + mensaje);

                if (mensaje.contains("CLIENTE>>> PEDIR-PREGUNTA")) {
                    String[] partes = mensaje.split(":");
                    int numPregunta = Integer.parseInt(partes[1].trim());
                    // HACER: si numPregunta está libre, preguntaSeleccionada = numPregunta
                    // Luego, usar preguntaSeleccionada para verificar respuesta y guardar
                    String enviarMsg = examen.getPregunta(numPregunta-1).getEnunciado();
                    enviarMsg += "\n" + examen.getPregunta(numPregunta-1).getCuerpo();
                    enviarMensaje("SERVIDOR>>> PREGUNTA:" + enviarMsg);
                    // HACER: enviar opciones
                }

                if (mensaje.toLowerCase().contains("hola")) {
                    enviarMensaje("Hola, como estas cliente " + idCliente + " ?");
                }

                //-------------ENVIO MENSAJE POR MULTICAST ------------------------------
                /**
                 * para mandar un mensaje de multicast, cuando lea un "chao"
                 */
                if (mensaje.toLowerCase().contains("chao")) {

                    // mensaje a enviar por multicast a los clientes
                    String men = "[s] mensaje a todos los que estan conectados multicast!!!!\n El cliente: " + idCliente + " dijo chao!";
                    //----- ***** se prepara el mensaje a enviar por el datagrama ***** -----
                    byte[] buffer = men.getBytes();
                    //Pasamos los datos al datagrama
                    datagrama.setData(buffer);
                    //Establecemos la longitud
                    datagrama.setLength(buffer.length);
                    //------SE ENVIA EL DATAGRAMA POR EL SOCKET MULTICAST-------------
                    sMulti.send(datagrama);
                    System.out.println("DESPUES DE ENVIAR DATAGRAMA");
                    //---------fin envio mensaje por multicast----------------------------------
                    //método para salirse de un grupo un participante.  para tenerlo en cuenta
                    //en el cliente, cuando se salga o se termine el examen, entonces que abandone el grupo
                    //// Si recibe "Adios" abandona el grupo
                    //socket.leaveGroup(grupo);

                }
                //-----------------------------------------    
            } catch (ClassNotFoundException enc) {
                System.out.println("\nSe recibio un tipo de objeto desconocido");
            } catch (IOException ex) {
                System.out.println("error al leer del flujo de entrada");
                cerrar();
                break;
            } // fin de catch
        } while (true);

    }

    public void cerrar() {
        System.out.println(" --se invocó el método cerrar");
        try {
            salida.close();
            entrada.close();
            sCliente.close();
            //-------------------Multicast cerrar el socket---------
            sMulti.leaveGroup(sMulti.getInetAddress());
            //-------------------------------
        } catch (IOException exepcionES) {
            System.out.println(exepcionES.getMessage());
        } // fin de catch

    }

    /**
     * método que envía al cliente el mensaje de respuesta a la solicitud
     *
     * @param mens
     */
    public void enviarMensaje(String mens) {
        try {
            salida.writeObject(mens);
            salida.flush(); // envía toda la salida al cliente
        } catch (IOException exepcionES) {
            System.out.println("\nError al escribir objeto");
        }
    }
    

    /**
     * crea los flujos de entrada y salida para la comunicación con el cliente
     */
    public void abrirFlujos() {
        try {
            salida = new ObjectOutputStream(sCliente.getOutputStream());
            salida.flush();

            entrada = new ObjectInputStream(sCliente.getInputStream());
            interfaz.appendEstadoServidor("\n Se obtuvieron los flujos de E/S del cliente: " + idCliente);

        } catch (IOException ex) {
            System.out.println("error a abrir los flujos del cliente " + idCliente);
        }
    }
}
