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
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author villa
 */
public class ConexionesServidor implements ActionListener {

    private ServerSocket serverSocket;
    private int estudiantes = 0;
    private ArrayList<HiloServidor> hilos;
    private Timer tiempo;
    int s;

    private MulticastSocket socketCast;

    private DatagramPacket datagrama;
    byte[] vacio = new byte[0];

    ArrayList<Examen> examenes;
    Examen examen;
    boolean examenIniciado;

    InformeExamen informe;
    ArrayList<InformeExamen> informes;

    GUIServer interfaz;
    Fachada fachada;

    public ConexionesServidor(GUIServer interfaz) {
        this.interfaz = interfaz;
        fachada = new Fachada();
        interfaz.asignarEscuchasBotones(this);
        agregarExamenPrueba();
        examenes = new ArrayList<>();
        hilos = new ArrayList<>();
        informes = new ArrayList<>();
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
                    HiloServidor hilo = new HiloServidor(socket, estudiantes, interfaz, socketCast, datagrama, informes);
                    hilo.start();
                    hilos.add(hilo);
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

                    for (HiloServidor x : hilos) {
                        //x.start();
                        x.setExamen(examen);
                    }
                    interfaz.appendEstadoServidor("Se ha iniciado el examen\n");
                    examenIniciado = true;
                    String mensaje = "INICIO:" + examen.numeroPreguntas(); //+ " : " + examen.getNombre();
                    byte[] buffer = mensaje.getBytes();
                    datagrama.setData(buffer);
                    datagrama.setLength(buffer.length);
                    iniciarTiempo();

                    informe = new InformeExamen(examen.getNombre());
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
        } else if (ae.getSource() == interfaz.getBConsultarInforme()) {
            for (InformeExamen x : informes) {
                if (x.getNombre().equals(interfaz.getInformeSeleccionado())) {
                    interfaz.printInformeExamem(x.getInforme());
                    break;
                }
            }
        }

    }

    public void agregarExamenPrueba() {
        examen = new Examen();
        examen.setNombre("Examen de prueba mixto");
        examen.setDuracion(30);
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

    public void enviarMensajeMulticast(String mens) {
        byte[] buffer = mens.getBytes();
        datagrama.setData(buffer);
        datagrama.setLength(buffer.length);
        try {
            socketCast.send(datagrama);
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                opciones.add(iterator.next().toString());
            }
            String opcCorrecta = iterator.next().toString();
            examen.addPregunta(new Pregunta(enunciado, cuerpo, opciones, opcCorrecta));
        }
        examenes.add(examen);
        interfaz.mostrarPreguntasTabla(examen.getInfoPreguntas());
    }

    public void iniciarTiempo() {
        s = examen.getDuracion();
        ActionListener acciones = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (s == 0) {
                    terminarExamen();
                    tiempo.stop();
                } else {
                    s--;
                    interfaz.setDuracionRestante(s);
                    String mens = "TIEMPO-RESTANTE:" + s;
                    enviarMensajeMulticast(mens);
                }
            }
        };
        tiempo = new Timer(1000, acciones);
        tiempo.start();
    }

    public void terminarExamen() {
        interfaz.appendEstadoServidor("Se ha acabado el examen\n");
        informes.add(informe);
        interfaz.addInformeExamenJCB(informe.getNombre());
        enviarMensajeMulticast("FIN-EXAMEN");
    }
}
