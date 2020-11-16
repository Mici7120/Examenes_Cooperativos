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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    ArrayList<Examen> Examenes;
    Examen examen;
    boolean examenIniciado;

    GUIServer interfaz;

    public ConexionesServidor(GUIServer gui) {
        interfaz = gui;
        interfaz.asignarEscuchasBotones(this);
        examen = new Examen();
        agregarExamenPrueba();
        Examenes = new ArrayList<>();
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
            interfaz.appendEstadoServidor("\nEsperando conexiones...");
            
            while (estudiantes < 3) {
                try {
                    Socket socket = serverSocket.accept(); // permite al servidor aceptar la conexión                    
                    estudiantes++;
                    hilo = new HiloServidor(socket, estudiantes, interfaz, socketCast, datagrama, examen);
                    interfaz.appendEstadoServidor("\n\nConectado el estudiante: " + estudiantes);

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
            examen.setNombre(interfaz.getNombreExamen());
            Examenes.add(examen);

            interfaz.addExamenJCB(examen.getNombre());
            interfaz.setNumeroPregunta(0);
            interfaz.borrarCamposExamenes();
            interfaz.resetJCBPreguntas();

            examen = new Examen();
        } else if (ae.getSource() == interfaz.getBIniciarExamen()) {
            if (examenIniciado) {
                interfaz.appendEstadoServidor("\n\nYa se ha iniciado el examen");
            } else {
                if (estudiantes == 3) {
                    interfaz.appendEstadoServidor("\n\nSe ha iniciado el examen");
                    examenIniciado = true;
                    String mensaje = "SERVIDOR>>> INICIO: " + examen.numeroPreguntas() + " : " + examen.getNombre();
                    byte[] buffer = mensaje.getBytes();
                    datagrama.setData(buffer);
                    datagrama.setLength(buffer.length);
                    try {
                        socketCast.send(datagrama);
                    } catch (IOException ex) {
                        Logger.getLogger(ConexionesServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    interfaz.appendEstadoServidor("\n\nNo estan conectados los 3 estudiantes");
                }
            }
        } else if (ae.getSource() == interfaz.getBAgregarPregunta()) {
            if (Validaciones.validarCampoOpcionesPregunta(interfaz)) {
                Pregunta p = new Pregunta(interfaz.getEnunciadoPregunta(), interfaz.getCuerpoPregunta(), interfaz.getOpciones(), 4);
                examen.addPregunta(p);
                interfaz.addPreguntaJCB(interfaz.getEnunciadoPregunta());
                interfaz.setNumeroPregunta(1);
                interfaz.borrarCamposOpcionesPregunta();
            }
        } else if (ae.getSource() == interfaz.getBLimpiarAreaEstadoServidor()) {
            interfaz.limpiarAreaEstadoServidor();
        }

    }

    public void agregarExamenPrueba() {
        examen.setNombre("Examen de prueba mixto");
        examen.setDuracion(5);
        Pregunta p1 = new Pregunta(
            "Determine el resultado",
            "1+1",
            new ArrayList<String>(Arrays.asList("pez", "1", "2", "ninguna de las anteriores")),
            3
        );
        Pregunta p2 = new Pregunta(
            "Aproximadamente, ¿cuántos huesos tiene el cuerpo humano?",
            "",
            new ArrayList<String>(Arrays.asList("5", "40", "390", "208")),
            4
        );
        Pregunta p3 = new Pregunta(
            "Responde la siguiente trivia sobre Avengers",
            "¿Cómo se llama la nave de los Guardianes de la galaxia en Avengers: Infinity War?",
            new ArrayList<String>(Arrays.asList("El Milano", "El Comodoro", "El Benatar", "El Halcón milenario")),
            3
        );
        examen.addPregunta(p1);
        examen.addPregunta(p2);
        examen.addPregunta(p3);
        interfaz.addExamenJCB(examen.getNombre());
    }

}
