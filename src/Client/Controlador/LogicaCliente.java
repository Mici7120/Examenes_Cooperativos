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

/**
 *
 * @author ruzbe
 */
public class LogicaCliente implements ActionListener{
    
    private ObjectOutputStream salida; 
    private ObjectInputStream entrada;
    private Socket cliente;
    private GUIClient interfaz;
    private ArrayList<EstadoPregunta> estadoPreguntas;
    
    LogicaMulticast multicast;
    
    public LogicaCliente(GUIClient interfaz)
    {
        this.interfaz = interfaz;
        interfaz.asignarEscuchasBotones(this);
        estadoPreguntas = new ArrayList();
    }
    
    /**
     * Método que conecta al cliente con el servidor ycrea los flujos de entrada y 
     * salida para comunicarse con él
     * @param direccionServer
     */    
    public void conectar(String direccionServer)
    {
        
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
            System.out.println("UnknownHostException: "+ ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: "+ ex.getMessage());
        }
       
        //invoco a escuchar al servidor
        escucharAlServidor();
    }
    
    public void escucharAlServidor() 
    {
        System.out.println("estoy en escuchar al servidor");
        String mensajeRecibido = "";
        do 
        {
            try 
            {   mensajeRecibido = ( String ) entrada.readObject();
                //interfaz.mostrarDatos("\n" + mensajeRecibido);
                System.out.println(mensajeRecibido);
                interfaz.tAreaMensajes.append("\n"+mensajeRecibido);
                
            } catch ( ClassNotFoundException ene ){
                System.out.println("Se recibio un tipo de objeto desconocido"+ene.getMessage());
            } catch (IOException ex) {
                System.out.println("Se arrojó un ioexcepcion cuando se trataba de leer del servidor "+ex.getMessage());
                break;
            }
        } while ( !mensajeRecibido.equals( "SERVIDOR>>> TERMINAR" ) );
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
     * @param mensaje el mensaje que se va a enviaar al servidor
     * @return el mensaje para ser mostrado por pantalla
     */
     public String enviarDatos( String mensaje ) {
         String mensajeS = "";
         
        try 
        {
            salida.writeObject( "CLIENTE>>> " + mensaje );
            salida.flush(); // envía todos los datos a la salida
            mensajeS ="\nCLIENTE>>> " + mensaje;
            
        } catch ( IOException excepcionES ) {
            mensajeS = "\nError al escribir objeto";
        } // fin de catch
        
        return mensajeS;
    } // fin del método enviarDatos
    
    
    
     @Override
    public void actionPerformed(ActionEvent ae) {
        //interfaz.mostrarDatos(enviarDatos(interfaz.obtenerDato()));
        //interfaz.limpiarCampos();
        if (ae.getSource() == interfaz.bObtener) {
            String numPregunta = interfaz.selectPregunta.getItemAt(interfaz.selectPregunta.getSelectedIndex());
            String mensaje = "PEDIR-PREGUNTA : " + numPregunta;
            System.out.println( mensaje );
            System.out.println( enviarDatos(mensaje) );
        } else if (ae.getSource() == interfaz.bEnviar) {

        } else if (ae.getSource() == interfaz.bCancelar) {

        }
    }

    public void iniciarExamen(int cantidadPreguntas) {
        interfaz.tAreaMensajes.append("\nDesde iniciarExamen: "+cantidadPreguntas);
        for (int i=0; i < cantidadPreguntas; i++) {
            estadoPreguntas.add(new EstadoPregunta(i+1, true));
        }
        actualizarCBpreguntas();
    }

    public void actualizarCBpreguntas() {
        for (EstadoPregunta pregunta : estadoPreguntas) {
            if (pregunta.esVisible()) {
                interfaz.selectPregunta.addItem(Integer.toString(pregunta.getNumero()));
            }
        }
        interfaz.pIzquierdo.revalidate();
    }
    
}
