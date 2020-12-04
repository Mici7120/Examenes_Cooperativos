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
import java.util.Iterator;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Timer;

/**
 *
 * @author villa
 */
public class ConexionesServidor implements ActionListener {

    private ServerSocket serverSocket;
    private int estudiantes = 0;
    private ArrayList<HiloServidor> hilos;
    private Timer tiempo;
    int m, s;

    private MulticastSocket socketCast;

    private DatagramPacket datagrama;
    byte[] vacio = new byte[0];

    ArrayList<Examen> examenes;
    Examen examen;
    boolean examenIniciado;
    boolean archivoCargado;

    InformeExamen informe;
    ArrayList<InformeExamen> informes;

    GUIServer interfaz;
    Fachada fachada;

    public ConexionesServidor(GUIServer interfaz) {
        this.interfaz = interfaz;
        interfaz.asignarEscuchasBotones(this);

        examenes = new ArrayList<>();
        hilos = new ArrayList<>();

        fachada = new Fachada();
        informes = fachada.cargarInformes();
        actualizarCBInformes();

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
            /*
            InetAddress ipDestino = InetAddress.getByName("190.99.134.17");
            serverSocket = new ServerSocket(12345, 1,ipDestino);
             */
            serverSocket = new ServerSocket(12345);

            interfaz.appendEstadoServidor("Iniciado servidor por el puerto " + serverSocket.getLocalPort() + "\n");
            interfaz.appendEstadoServidor("Esperando conexiones...\n\n");

            while (estudiantes < 3) {
                try {
                    Socket socket = serverSocket.accept(); // permite al servidor aceptar la conexión                    
                    estudiantes++;
                    HiloServidor hilo = new HiloServidor(socket, estudiantes, interfaz, socketCast, datagrama);
                    hilo.start();
                    hilos.add(hilo);
                    interfaz.appendEstadoServidor("Conectado el estudiante: " + estudiantes + "\n");

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
            if (Validaciones.validarBotonAgregarExamen(interfaz) && archivoCargado) {
                examen.setNombre(interfaz.getNombreExamen());
                examen.setDuracion(Integer.parseInt(interfaz.getDuracion()));
                examenes.add(examen);
                interfaz.addExamenJCB(interfaz.getNombreExamen());
                interfaz.limpiarCamposPregunta();
                interfaz.borrarTabla();
                archivoCargado = false;
            } else {
                JOptionPane.showMessageDialog(interfaz, "Por favor complete los campos y cargue el archivo correctamente");
            }
        } else if (ae.getSource() == interfaz.getBIniciarExamen()) {
            if (examenIniciado) {
                interfaz.appendEstadoServidor("Ya se ha iniciado el examen\n");
            } else {
                if (estudiantes == 3 && !examenes.isEmpty()) {
                    Examen examenSeleccionado = new Examen();
                    for (Examen e : examenes) {
                        if (e.getNombre().equals(interfaz.getExamenSeleccionado())) {
                            examenSeleccionado = e;
                            break;
                        }
                    }
                    informe = new InformeExamen();
                    informe.setTotalPreguntas(examenSeleccionado.numeroPreguntas());
                    interfaz.getExamenSeleccionado();
                    informe.setNombre(examenSeleccionado.getNombre());
                    for (HiloServidor x : hilos) {
                        x.setExamen(examenSeleccionado);
                        x.setInforme(informe);
                    }
                    interfaz.appendEstadoServidor("Se ha iniciado el examen\n");
                    examenIniciado = true;
                    String mensaje = "INICIO:" + examenSeleccionado.numeroPreguntas(); //+ " : " + examen.getNombre();
                    byte[] buffer = mensaje.getBytes();
                    datagrama.setData(buffer);
                    datagrama.setLength(buffer.length);
                    iniciarTiempo(examenSeleccionado.getDuracion());

                    try {
                        socketCast.send(datagrama);
                    } catch (IOException ex) {
                        Logger.getLogger(ConexionesServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (examenes.isEmpty()) {
                        interfaz.appendEstadoServidor("\nNo hay examenes cargados");
                    } else {
                        interfaz.appendEstadoServidor("\nNo estan conectados los 3 estudiantes\n");
                    }
                }
            }
        } else if (ae.getSource() == interfaz.getBLimpiarAreaEstadoServidor()) {
            interfaz.limpiarAreaEstadoServidor();
        } else if (ae.getSource() == interfaz.getBConsultarInforme()) {
            if (!informes.isEmpty()) {
                for (InformeExamen x : informes) {
                    if (x.getNombre().equals(interfaz.getInformeSeleccionado())) {
                        interfaz.printInformeExamem(x.getInforme());
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(interfaz, "No hay informes por consultar");
            }
        } else if (ae.getSource() == interfaz.getBConsultarProcentaje()) {
            calcularPorcentajeExamenesPerdidos();
        } else if (ae.getSource() == interfaz.getBPreguntasPerdidas()) {
            preguntasPerdidas();
        }

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

    public void actualizarCBInformes() {
        if (!informes.isEmpty()) {
            for (InformeExamen i : informes) {
                interfaz.addInformeExamenJCB(i.getNombre());
            }
        }
    }

    public void cargarArchivo() {
        ArrayList<String> datos = fachada.cargarArchivo();
        examen = new Examen();
        Iterator iterator = datos.iterator();
        try {
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
            archivoCargado = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(interfaz, "Error al cargar el archivo");
        }
    }

    public void iniciarTiempo(int duracion) {
        m = duracion;
        s = 0;
        TimerTask time = new TimerTask() {
            @Override
            public void run() {
                if ((m == 0 && s == 0) || todasPreguntasRespondidas()) {
                    terminarExamen();
                    cancel();
                } else if (s > 0) {
                    s--;
                    interfaz.setDuracionRestante(m + ":" + s);
                    String mens = "TIEMPO-RESTANTE:" + m + ":" + s;
                    enviarMensajeMulticast(mens);
                } else {
                    m--;
                    s = 60;
                }
            }
        };
        tiempo = new Timer();
        tiempo.scheduleAtFixedRate(time, 0, 1000);
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
        interfaz.addInformeExamenJCB(informe.getNombre());
        enviarMensajeMulticast("FIN-EXAMEN:" + informe.getInforme());

        informes.add(informe);
        fachada.guardarInformes(informes);

        examenIniciado = false;
    }

    public void calcularPorcentajeExamenesPerdidos() {
        double totalExamenes = informes.size();
        double examenesPerdidos = 0;
        String nombreExamPerdidos = "Examenes Perdidos:\n";
        for (InformeExamen i : informes) {
            if (i.getNota() < 3) {
                examenesPerdidos++;
                nombreExamPerdidos += "-" + i.getNombre() + "\n";
            }
        }
        double porcentajePerdido = (examenesPerdidos / totalExamenes) * 100;
        String mensaje = "Porcentaje de examenes perdidos: " + porcentajePerdido
                + "%\n" + nombreExamPerdidos;
        interfaz.printInformeExamem(mensaje);
    }

    public void preguntasPerdidas() {
        
    }
}
