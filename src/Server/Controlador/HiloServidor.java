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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private int numPreguntaSeleccionada;

    private MulticastSocket sMulti;
    private DatagramPacket datagrama;

    public HiloServidor(Socket socket, int numeroEstudiante, GUIServer interfaz, MulticastSocket multi, DatagramPacket paquete, Examen examen) {

        sCliente = socket;
        idCliente = numeroEstudiante;
        this.interfaz = interfaz;
        this.examen = examen;
        sMulti = multi;
        datagrama = paquete;
    }

    @Override
    public void run() {
        interfaz.appendEstadoServidor("\nServer hilo " + idCliente + " por el puerto " + sCliente.getPort() + " iniciado.");
        abrirFlujos();

        String mensaje = "";
        do {
            try {
                mensaje = (String) entrada.readObject();
                interfaz.appendEstadoServidor("\nCliente " + idCliente + ": " + mensaje);

                if (mensaje.contains("PEDIR-PREGUNTA")) {
                    String[] partes = mensaje.split(":");
                    int numPregunta = Integer.parseInt(partes[1].trim());
                    // HACER: usar numPreguntaSeleccionada para verificar respuesta y guardar
                    if (examen.getPregunta(numPregunta - 1).getDisponible()) {
                        numPreguntaSeleccionada = numPregunta;
                        String enviarMsg = examen.getPregunta(numPregunta - 1).getEnunciado();
                        enviarMsg += "\n" + examen.getPregunta(numPregunta - 1).getCuerpo();
                        enviarMensaje("PREGUNTA:" + enviarMsg);
                        // HACER: enviar opciones
                        String opciones = "OPCIONES\n";
                        for (String x : examen.getPregunta(numPregunta - 1).getOpciones()) {
                            opciones += x + "\n";
                        }

                        examen.getPregunta(numPregunta - 1).setDisponible(false);
                        
                        enviarMensaje(opciones);
                        enviarMensajeMulticast(getEstadoPreguntas());
                    }else{
                        enviarMensaje("PREGUNTA OCUPADO O RESPONDIDA\n");
                    }
                } else if (mensaje.contains("CANCELAR-PREGUNTA")) {
                    examen.getPregunta(numPreguntaSeleccionada - 1).setDisponible(true);
                    enviarMensajeMulticast(getEstadoPreguntas());
                }

            } catch (ClassNotFoundException enc) {
                System.out.println("\nSe recibio un tipo de objeto desconocido");
            } catch (IOException ex) {
                System.out.println("error al leer del flujo de entrada");
                cerrar();
                break;
            } // fin de catch
        } while (true);

    }

    private String getEstadoPreguntas() {
        String estadoPreguntas = "ACTUALIZAR-PREGUNTA:";
        for (Pregunta x : examen.preguntas) {
            if (x.getDisponible()) {
                estadoPreguntas += "DISPONIBLE,";
            } else {
                estadoPreguntas += "NO-DISPONIBLE,";
            }
        }
        return estadoPreguntas;
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

    public void enviarMensajeMulticast(String mens) {
        byte[] buffer = mens.getBytes();
        datagrama.setData(buffer);
        datagrama.setLength(buffer.length);
        try {
            sMulti.send(datagrama);
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
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
            interfaz.appendEstadoServidor("\nSe obtuvieron los flujos de E/S del cliente: " + idCliente + "\n\n");
        } catch (IOException ex) {
            System.out.println("error a abrir los flujos del cliente " + idCliente);
        }
    }
}
