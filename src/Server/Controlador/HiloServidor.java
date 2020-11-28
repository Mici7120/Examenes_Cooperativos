/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Modelo.Fachada;
import Server.Vista.GUIServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

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
    private Timer tiempo;

    private InformeExamen informe;
    private ArrayList<InformeExamen> informes;

    private MulticastSocket sMulti;
    private DatagramPacket datagrama;
    
    private Fachada fachada;

    public HiloServidor(Socket socket, int numeroEstudiante, GUIServer interfaz, MulticastSocket multi, DatagramPacket paquete, Timer tiempo, ArrayList informes) {

        sCliente = socket;
        idCliente = numeroEstudiante;
        this.interfaz = interfaz;
        sMulti = multi;
        datagrama = paquete;
        this.tiempo = tiempo;
        this.informes = informes;
    }

    /**
     *
     */
    @Override
    public void run() {
        interfaz.appendEstadoServidor("Server hilo " + idCliente + " por el puerto " + sCliente.getPort() + " iniciado\n");
        abrirFlujos();

        String mensaje = "";
        do {
            try {
                mensaje = (String) entrada.readObject();
                interfaz.appendEstadoServidor("Cliente " + idCliente + ":" + mensaje + "\n");

                StringTokenizer token = new StringTokenizer(mensaje, ":");
                String accion = token.nextToken().trim();
                int numPreguntaSeleccionada = Integer.parseInt(token.nextToken());;

                switch (accion) {
                    case "PEDIR-PREGUNTA":
                        if (examen.getPregunta(numPreguntaSeleccionada).getDisponible()) {
                            String enviarMsg = examen.getPregunta(numPreguntaSeleccionada).getEnunciado();
                            enviarMsg += "\n" + examen.getPregunta(numPreguntaSeleccionada).getCuerpo();
                            enviarMensaje("PREGUNTA:" + enviarMsg);
                            String opciones = "OPCIONES\n";
                            for (String x : examen.getPregunta(numPreguntaSeleccionada).getOpciones()) {
                                opciones += x + "\n";
                            }

                            examen.getPregunta(numPreguntaSeleccionada).setEstado("OCUPADA");

                            enviarMensaje(opciones);
                            enviarMensajeMulticast(getEstadoPreguntas());
                        } else {
                            enviarMensaje("PREGUNTA OCUPADO O RESPONDIDA\n");
                        }
                        break;
                    case "CANCELAR-PREGUNTA":
                        examen.getPregunta(numPreguntaSeleccionada).setEstado("LIBRE");
                        enviarMensajeMulticast(getEstadoPreguntas());
                        break;
                    case "ENVIAR-RESPUESTA":
                        String respuestaCliente = token.nextToken();
                        String nombreCliente = token.nextToken();
                        Pregunta preguntaSelec = examen.getPregunta(numPreguntaSeleccionada);
                        boolean calificacion = respuestaCliente.equals(preguntaSelec.getOpcCorrecta());
                        informe.registrarRespuesta(nombreCliente, preguntaSelec.getEnunciado(), respuestaCliente, calificacion);
                        examen.getPregunta(numPreguntaSeleccionada).setEstado("RESPONDIDA");
                        enviarMensajeMulticast(getEstadoPreguntas());
                        if (todasPreguntasRespondidas()) {
                            terminarExamen();
                        }
                        break;
                }

            } catch (ClassNotFoundException enc) {
                System.out.println("Se recibio un tipo de objeto desconocido\n");
            } catch (IOException ex) {
                System.out.println("Error al leer del flujo de entrada\n");
                cerrar();
                break;
            } // fin de catch
        } while (true);

    }

    /**
     *
     * @return
     */
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

    /**
     *
     */
    public void cerrar() {
        System.out.println("Se invocó el método cerrar");
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
     *
     * @param mens
     */
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
            interfaz.appendEstadoServidor("Se obtuvieron los flujos de E/S del cliente: " + idCliente + "\n\n");
        } catch (IOException ex) {
            System.out.println("Error a abrir los flujos del cliente " + idCliente);
        }
    }

    public boolean todasPreguntasRespondidas() {
        for (Pregunta p : examen.preguntas) {
            if (!p.getEstado().equals("RESPONDIDA")) {
                return false;
            }
        }
        return true;
    }

    public void terminarExamen() {
        interfaz.appendEstadoServidor("Se ha acabado el examen\n");
        informes.add(informe);
        interfaz.addInformeExamenJCB(informe.getNombre());
        enviarMensajeMulticast("FIN-EXAMEN:" + informe.getInforme());
        fachada.guardarInformes(informes);
        tiempo.stop();
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public void setInforme(InformeExamen informe) {
        this.informe = informe;
    }

}
