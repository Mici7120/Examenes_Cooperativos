/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Vista;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 *
 * @author villa
 */
public class GUIServer extends JFrame {
    
    Container container;
    JTabbedPane pestañas;
    JPanel confiExamenes, iniExamen, infoExamenes;
    JTextField nombreExamen;
    
    public GUIServer() {
        
        container = getContentPane();
        
        //configuracion de la pestaña configurar examenes
        confiExamenes = new JPanel();
        nombreExamen = new JTextField();
        
        //configuracion de las pestañas
        pestañas = new JTabbedPane();
        pestañas.add(confiExamenes, "ConfigurarExamenes");
        pestañas.add(iniExamen, "Iniciar Examen");
        pestañas.add(infoExamenes, "Informacion de Examanes Prestados");
        
       
        setTitle("Cliente");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }
    
    public static void main(String[] args) {
        GUIServer server = new GUIServer();
        
    }
}
