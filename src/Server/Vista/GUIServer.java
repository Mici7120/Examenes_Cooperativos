/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Vista;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
    JPanel pConfiExamenes, pIniExamen, pInfoExamenes, pIngresarExamen, pOpIniExamen, pIngresarPreguntas, pPreguntas;
    JTextField nombreExamen, pregunta, tfOpcion1, tfOpcion2, tfOpcion3, tfOpcion4;
    JTextArea estadoServidor;
    JLabel lNombreExamen, lNumeroPregunta;
    JButton bIniciarExamen, bAgregarPregunta, bAgregarExamen;
    JComboBox jcbPreguntas, jcbExamenes;
    JRadioButton rbOpcion1, rbOpcion2, rbOpcion3, rbOpcion4;
    
    int numeroPregunta = 1;

    public GUIServer() {
        super("Servidor - Examenes Cooperativos");

        container = getContentPane();
        container.setLayout(new BorderLayout());

        lNombreExamen = new JLabel("Nombre del examen:");
        nombreExamen = new JTextField("");
        estadoServidor = new JTextArea();
        bIniciarExamen = new JButton("Iniciar Examen");
        bAgregarExamen = new JButton("Agregar Examen");
        jcbExamenes = new JComboBox();

        //configuracion de la pestaña configurar examenes
        pConfiExamenes = new JPanel();
        pConfiExamenes.setLayout(new BorderLayout());

        pIngresarExamen = new JPanel();
        pIngresarExamen.setLayout(new GridLayout(1, 3));
        pIngresarExamen.add(lNombreExamen);
        pIngresarExamen.add(nombreExamen);
        pIngresarExamen.add(bAgregarExamen);
        
        pIngresarPreguntas = new JPanel();
        pIngresarPreguntas.setLayout(new GridLayout(5, 2));
        lNumeroPregunta = new JLabel("Pregunta #" + numeroPregunta + ":");
        pregunta = new JTextField();                
        rbOpcion1 = new JRadioButton("Opcion1");
        tfOpcion1 = new JTextField();
        rbOpcion2 = new JRadioButton("Opcion2");
        tfOpcion2 = new JTextField();
        rbOpcion3 = new JRadioButton("Opcion3");
        tfOpcion3 = new JTextField();
        rbOpcion4 = new JRadioButton("Opcion4");
        tfOpcion4 = new JTextField();
        
        pIngresarPreguntas.add(lNumeroPregunta);
        pIngresarPreguntas.add(pregunta);
        pIngresarPreguntas.add(rbOpcion1);
        pIngresarPreguntas.add(tfOpcion1);
        pIngresarPreguntas.add(rbOpcion2);
        pIngresarPreguntas.add(tfOpcion2);
        pIngresarPreguntas.add(rbOpcion3);
        pIngresarPreguntas.add(tfOpcion3);
        pIngresarPreguntas.add(rbOpcion4);
        pIngresarPreguntas.add(tfOpcion4);
        
        pPreguntas = new JPanel();
        jcbPreguntas = new JComboBox();
        pPreguntas.add(new JLabel("Preguntas:"));
        pPreguntas.add(jcbPreguntas);
        
        bAgregarPregunta = new JButton("Agregar Pregunta");

        pConfiExamenes.add(pIngresarExamen, BorderLayout.NORTH);
        pConfiExamenes.add(pIngresarPreguntas, BorderLayout.CENTER);
        pConfiExamenes.add(pPreguntas, BorderLayout.EAST);        
        pConfiExamenes.add(bAgregarPregunta, BorderLayout.SOUTH);

        //configuracion de la pestaña iniciar examen
        pIniExamen = new JPanel();
        pIniExamen.setLayout(new BorderLayout());

        pOpIniExamen = new JPanel();
        pOpIniExamen.add(jcbExamenes);
        pOpIniExamen.add(bIniciarExamen);

        pIniExamen.add(pOpIniExamen, BorderLayout.NORTH);
        pIniExamen.add(estadoServidor, BorderLayout.CENTER);

        //configuracion de la pestaña informacion de examenes prestados
        pInfoExamenes = new JPanel();

        //configuracion de las pestañas
        pestañas = new JTabbedPane();
        pestañas.add(pConfiExamenes, "ConfigurarExamenes");
        pestañas.add(pIniExamen, "Iniciar Examen");
        pestañas.add(pInfoExamenes, "Informacion de Examanes Prestados");
        container.add(pestañas);

        setVisible(true);
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void asignarEscuchasBotones(ActionListener escucha) {
        bIniciarExamen.addActionListener(escucha);
        bAgregarExamen.addActionListener(escucha);
        bAgregarPregunta.addActionListener(escucha);
    }

    public String getExamenSeleccionado() {
        return jcbExamenes.getSelectedItem().toString();
    }
    
    public JButton getBIniciarExamen(){
        return bIniciarExamen;
    }
    
    public JButton getBAgregarPregunta(){
        return bAgregarPregunta;
    }
    
    public JButton getBAgregarExamen(){
        return bAgregarExamen;
    }
    
    public void appendEstadoServidor(String mensaje){
        estadoServidor.append(mensaje);
    }
    
    public String getNombreExamen(){
        return nombreExamen.getText();
    }
    
    public void borrarCampo(JTextField campo){
        campo.setText("");
    }
    
    public void borrarOpcionesPregunta(){
        pregunta.setText("");
        tfOpcion1.setText("");
        tfOpcion2.setText("");
        tfOpcion3.setText("");
        tfOpcion4.setText("");
    }
    
    public void setNumeroPregunta(int x){
        if(x == 1){
            numeroPregunta ++;
        }else{
            numeroPregunta = 1;
        }
        lNumeroPregunta.setText("Pregunta #" + numeroPregunta + ":");
    }
}
