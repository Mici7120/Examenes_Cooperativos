/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Modelo.Fachada;
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
import java.util.Iterator;
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

    ArrayList<Examen> examenes;
    Examen examen;
    boolean examenIniciado;

    GUIServer interfaz;
    Fachada fachada;

    public ConexionesServidor(GUIServer interfaz) {
        this.interfaz = interfaz;
        fachada = new Fachada();
        interfaz.asignarEscuchasBotones(this);
        agregarExamenPrueba();
        examenes = new ArrayList<>();
        ejecutarServidor();
    }

    /**
     * Método que crea el serverSocket, se queda a espera de que un cliente se
     * conecte por el puerto e invoca al método que procesa las solicitudes del
     * cliente
     */
    public void ejecutarServidor() {
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

        try {
            serverSocket = new ServerSocket(12345);

            interfaz.appendEstadoServidor("Iniciado servidor por el puerto " + serverSocket.getLocalPort() + "\n");
            interfaz.appendEstadoServidor("Esperando conexiones...\n\n");

            while (estudiantes < 3) {
                try {
                    Socket socket = serverSocket.accept(); // permite al servidor aceptar la conexión                    
                    estudiantes++;
                    hilo = new HiloServidor(socket, estudiantes, interfaz, socketCast, datagrama, examen);
                    hilo.start();
                    interfaz.appendEstadoServidor("Conectado el estudiante: " + estudiantes);

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
        if (ae.getSource() == interfaz.getbCargarArchivo()) {
            cargarArchivo();
        } else if (ae.getSource() == interfaz.getBAgregarExamen()) {
            examen.setNombre(interfaz.getNombreExamen());
            examen.setDuracion(interfaz.getDuracion());
            examenes.add(examen);
            interfaz.addExamenJCB(interfaz.getNombreExamen());
            interfaz.limpiarCamposPregunta();
            interfaz.borrarTabla();
        } else if (ae.getSource() == interfaz.getBIniciarExamen()) {
            if (examenIniciado) {
                interfaz.appendEstadoServidor("Ya se ha iniciado el examen\n");
            } else {
                if (estudiantes <= 3) {
                    interfaz.appendEstadoServidor("Se ha iniciado el examen\n");
                    examenIniciado = true;
                    String mensaje = "INICIO: " + examen.numeroPreguntas() + " : " + examen.getNombre();
                    byte[] buffer = mensaje.getBytes();
                    datagrama.setData(buffer);
                    datagrama.setLength(buffer.length);
                    try {
                        socketCast.send(datagrama);
                    } catch (IOException ex) {
                        Logger.getLogger(ConexionesServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    interfaz.appendEstadoServidor("No estan conectados los 3 estudiantes\n");
                }
            }
        } else if (ae.getSource() == interfaz.getBLimpiarAreaEstadoServidor()) {
            interfaz.limpiarAreaEstadoServidor();
        }

    }

    public void agregarExamenPrueba() {
        examen = new Examen();
        examen.setNombre("Examen de prueba mixto");
        examen.setDuracion(5);
        Pregunta p1 = new Pregunta(
                "Determine el resultado",
                "1+1",
                new ArrayList<String>(Arrays.asList("pez", "1", "2", "ninguna de las anteriores")),
                "C"
        );
        Pregunta p2 = new Pregunta(
                "Aproximadamente, ¿cuántos huesos tiene el cuerpo humano?",
                "",
                new ArrayList<String>(Arrays.asList("5", "40", "390", "208")),
                "D"
        );
        Pregunta p3 = new Pregunta(
                "Responde la siguiente trivia sobre Avengers",
                "¿Cómo se llama la nave de los Guardianes de la galaxia en Avengers: Infinity War?",
                new ArrayList<String>(Arrays.asList("El Milano", "El Comodoro", "El Benatar", "El Halcón milenario")),
                "C"
        );
        examen.addPregunta(p1);
        examen.addPregunta(p2);
        examen.addPregunta(p3);
        interfaz.addExamenJCB(examen.getNombre());
    }

    public void cargarArchivo() {
        ArrayList<String> datos = fachada.cargarArchivo();
        examen = new Examen();
        Iterator iterator = datos.iterator();
        while (iterator.hasNext()) {
            String enunciado = iterator.next().toString();
            String cuerpo = iterator.next().toString();

            ArrayList<String> opciones = new ArrayList<>();
            for (int x = 0; x < 4; x++) {
                iterator.next().toString();
            }
            String opcCorrecta = iterator.next().toString();
            examen.addPregunta(new Pregunta(enunciado, cuerpo, opciones, opcCorrecta));
        }
        examenes.add(examen);
        interfaz.mostrarPreguntasTabla(examen.getInfoPreguntas());
    }
}
