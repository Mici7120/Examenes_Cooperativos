/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Vista;

import Server.Controlador.ConexionesServidor;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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
    public JTextArea estadoServidor;
    JLabel lNombreExamen;
    
    public GUIServer() {
        
        container = getContentPane();
        container.setLayout(new BorderLayout());
        
        //configuracion de la pestaña configurar examenes
        confiExamenes = new JPanel();
        confiExamenes.setLayout(new BorderLayout());
        
        lNombreExamen = new JLabel("Nombre del examen:");
        confiExamenes.add(lNombreExamen, BorderLayout.NORTH);
        
        nombreExamen = new JTextField();
        confiExamenes.add(nombreExamen, BorderLayout.CENTER);
        
        //configuracion de la pestaña iniciar examen
        iniExamen = new JPanel();
        iniExamen.setLayout(new BorderLayout());
        
        estadoServidor = new JTextArea();
        iniExamen.add(estadoServidor, BorderLayout.CENTER);
        
        
        //configuracion de la pestaña informacion de examenes prestados
        infoExamenes = new JPanel();
        
        
        //configuracion de las pestañas
        pestañas = new JTabbedPane();
        pestañas.add(confiExamenes, "ConfigurarExamenes");
        pestañas.add(iniExamen, "Iniciar Examen");
        pestañas.add(infoExamenes, "Informacion de Examanes Prestados");
        container.add(pestañas);
        
       
        setTitle("Cliente");
        setVisible(true);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    
    
}
