/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controlador;

import Client.Vista.GUIClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author ruzbe
 */
public class LogicaMulticast extends Thread{
    
    private GUIClient interfaz;
    /*Lo necesario para escuchar de un multicast */
    private MulticastSocket socketMultiEscucha;
    private InetAddress grupo;  //para especificar la dirección de red
    
    public LogicaMulticast(GUIClient inter)
    {
        interfaz = inter;
        try {
            //Creamos un socket multicast en el puerto local 10000:
            socketMultiEscucha = new MulticastSocket (10000);       
            //Configuramos el grupo (IP) a la que nos conectaremos
            grupo = InetAddress.getByName("231.0.0.1");            
            //Nos unimos al grupo:
            socketMultiEscucha.joinGroup (grupo);
         } catch (IOException ex) {
            System.out.println("error al crear el socket multicast"+ ex);
        }
    }
    
     @Override
    public void run()
    {
        while(true){
            // Se reserva espacio para recibir un paquete del servidor.  
            //Los paquetes recibidos no deben superara los 1024 bits 
            byte [] datoRecibido = new byte [2048];
            //Creamos el datagrama en el que recibiremos el paquete del socket multicast
            DatagramPacket dgp = new DatagramPacket (datoRecibido, datoRecibido.length);
            
            try{
                // Recibimos el paquete del socket:
                 socketMultiEscucha.receive(dgp);
                 String salida = new String (dgp.getData());
                 
                 // Adaptamos la información al tamaño de lo que se envió por si se envió menos de 1024):
                // byte [] buffer2 = new byte [dgp.getLength ()];
                 // Copiamos los datos en el nuevo array de tamaño adecuado:
                // System.arraycopy (dgp.getData(),0,buffer2,0, dgp.getLength());

                 //Vemos los datos recibidos por pantalla:
                 //String salida = new String (buffer2);
                 //interfaz.mostrarDatos("\nDesde multicast: "+salida);
            }catch(IOException e){
                System.out.println("Error en el multicast al recibir mensaje en el cliente");
            }
       }//fin while
        
        
    }//fin run
}