/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controlador;

import Client.Vista.GUIClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 *
 * @author ruzbe
 */
public class LogicaCliente implements ActionListener {

    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private Socket cliente;
    private GUIClient interfaz;
    private String nombreCliente;
    private ArrayList<EstadoPregunta> estadoPreguntas;
    private int numPregunta;

    LogicaMulticast multicast;
/**
 * Constructor de logicaCliente
 * @param interfaz 
 */
    public LogicaCliente(GUIClient interfaz) {
        this.interfaz = interfaz;
        nombreCliente = null;
        interfaz.asignarEscuchasBotones(this);
        estadoPreguntas = new ArrayList<>();
        numPregunta = 0;
        //Para hacer obligatorio el ingresar nombre cliente
        if(nombreCliente == null)  {
            nombreCliente="";
            while(nombreCliente.equals("")) {
                nombreCliente = JOptionPane.showInputDialog("Ingrese su nombre: ");
                if(nombreCliente == null){
                    nombreCliente="";
                }
            }
        }
    }

    /**
     * Método que conecta al cliente con el servidor y crea los flujos de entrada
     * y salida para comunicarse con él
     * @param direccionServer
     */
    public void conectar(String direccionServer) {

        //--------------------- MULTICAST ------------------------------------
        //creo el hilo del multicast y lo arranco
        multicast = new LogicaMulticast(this, interfaz);
        multicast.start();
        //--------------------------------------------------------------------

        try {

            cliente = new Socket(direccionServer, 12345);
            //interfaz.mostrarDatos( "Conectado con servidor...."+ cliente.getInetAddress().getHostName());
            System.out.println("ya pasé la creación del socket");
            //obtenemos los fujos de entrada y salida
            entrada = new ObjectInputStream(cliente.getInputStream());
            salida = new ObjectOutputStream(cliente.getOutputStream());
            salida.flush();  //se vacia el flujo de salida
            System.out.println("pasé la creación de flujos");
            //interfaz.habilitarCampo(true);

        } catch (UnknownHostException ex) {
            System.out.println("UnknownHostException: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }

        //invoco a escuchar al servidor
        escucharAlServidor();
    }

    /**
     * método para escuchar los mensajes del servidor y 
     * agregarlos al area de texto y a las opciones
     */
    public void escucharAlServidor() {
        System.out.println("estoy en escuchar al servidor");
        String mensajeRecibido = "";
        do {
            try {
                mensajeRecibido = (String) entrada.readObject();
                System.out.println(mensajeRecibido);
                if (mensajeRecibido.contains("PREGUNTA")) {
                    interfaz.getAreaMensajes().setText(mensajeRecibido);
                } else if (mensajeRecibido.contains("OPCIONES")) {
                    StringTokenizer token = new StringTokenizer(mensajeRecibido, "\n");
                    token.nextToken();
                    interfaz.setRBOpciones(token.nextToken(), token.nextToken(), token.nextToken(), token.nextToken());
                } else {
                    interfaz.getAreaMensajes().append(mensajeRecibido);
                }

            } catch (ClassNotFoundException ene) {
                System.out.println("Se recibio un tipo de objeto desconocido" + ene.getMessage());
            } catch (IOException ex) {
                System.out.println("Se arrojó un ioexcepcion cuando se trataba de leer del servidor " + ex.getMessage());
                break;
            }
        } while (!mensajeRecibido.equals("SERVIDOR>>> TERMINAR"));
        try {
            //se cierra la conexion
            entrada.close();
            salida.close();
            cliente.close();
            System.out.println("se cerró la conexion");
        } catch (IOException ex) {
            System.out.println("se produjo error al cerrar la conexion");
        }
    }

    /**
     * método que envia al servidor un mensaje
     * @param mensaje el mensaje que se va a enviar al servidor
     * @return el mensaje para ser mostrado por pantalla
     */
    public String enviarDatos(String mensaje) {
        String mensajeS = "";

        try {
            salida.writeObject(mensaje);
            salida.flush(); // envía todos los datos a la salida
            mensajeS = mensaje;

        } catch (IOException excepcionES) {
            mensajeS = "\nError al escribir objeto";
        } // fin de catch

        return mensajeS;
    } // fin del método enviarDatos

    @Override
    /**
     * método para poner eventos a los botones 
     */
    public void actionPerformed(ActionEvent ae) {
        
        if (ae.getSource() == interfaz.getBObtenerPregunta() && numPregunta == 0) {
            //guarda el numero de pregunta la cual esta contestando
            numPregunta = Integer.parseInt(interfaz.getComboBoxSelectPregunta().getItemAt(interfaz.getComboBoxSelectPregunta().getSelectedIndex()));
            String mensaje = "PEDIR-PREGUNTA:" + numPregunta;
            enviarDatos(mensaje);
            
        } else if (ae.getSource() == interfaz.getBEnviarPregunta()) {
            if(interfaz.getRespuestaSeleccionada() == "") {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna respuesta");
            }else {
                String mensaje = "ENVIAR-RESPUESTA:" + numPregunta + ":" + interfaz.getRespuestaSeleccionada() + ":" + nombreCliente;
                enviarDatos(mensaje);
                numPregunta = 0;
                interfaz.limpiarAreaMensajes();
                interfaz.LimpiarSelecRButton();
                interfaz.limpiarOpciones(); 
            }
            
        } else if (ae.getSource() == interfaz.getBCancelarPregunta()) {
            String mensaje = "CANCELAR-PREGUNTA:"  + numPregunta;
            enviarDatos(mensaje);
            numPregunta = 0;
            interfaz.limpiarAreaMensajes();
            interfaz.LimpiarSelecRButton();
            interfaz.limpiarOpciones();
        }
    }

    /**
     * método que inicia el examen en el cliente diciendo su cantidad de 
     * preguntas y añadiendo las preguntas al listado de preguntas a seleccionar
     * @param cantidadPreguntas 
     */
    public void iniciarExamen(int cantidadPreguntas) {
        interfaz.getAreaMensajes().append("\nInicia examen.\nCantidad de preguntas: " + cantidadPreguntas);
        for (int i = 0; i < cantidadPreguntas; i++) {
            estadoPreguntas.add(new EstadoPregunta(i + 1, true));
        }
        actualizarCBpreguntas();
    }

    /**
     * método que actualiza la lista de preguntas que se pueden seleccionar
     */
    public void actualizarCBpreguntas() {
        interfaz.getComboBoxSelectPregunta().removeAllItems();
        for (EstadoPregunta pregunta : estadoPreguntas) {
            if (pregunta.esVisible()) {
                interfaz.getComboBoxSelectPregunta().addItem(Integer.toString(pregunta.getNumero()));
            }
        }
        interfaz.getPanelIzquierdo().revalidate();
    }

    /**
     * metodo que retorna la lista de estados de las preguntas
     * @return ArrayList de estados de las preguntas
     */
    public ArrayList<EstadoPregunta> getEstadoPreguntas() {
        return estadoPreguntas;
    }

    /**
     * método que termina el examen al cliente, limpiando areas, 
     * opciones y lista de preguntas además de terminar la conexion
     */
    public void examenTerminado(String informe) {
        JOptionPane.showMessageDialog(interfaz, "Se ha acabado el examen");
        interfaz.limpiarAreaMensajes();
        interfaz.limpiarOpciones();
        interfaz.getAreaMensajes().append(informe);
        estadoPreguntas = new ArrayList<>();
        try {
            //se cierra la conexion
            entrada.close();
            salida.close();
            cliente.close();
            System.out.println("Se cerró la conexion");
        } catch (IOException ex) {
            System.out.println("Se produjo error al cerrar la conexion");
        }

    }
}
