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
import java.util.ArrayList;
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
    JTextArea cuerpoPregunta;
    JTextField nombreExamen, enunciadoPregunta, tfOpcion1, tfOpcion2, tfOpcion3, tfOpcion4;
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
        pIngresarPreguntas.setLayout(new BorderLayout());
        lNumeroPregunta = new JLabel("Pregunta #" + numeroPregunta + ":");
        enunciadoPregunta = new JTextField();
        cuerpoPregunta = new JTextArea();
        rbOpcion1 = new JRadioButton("Opcion1");
        tfOpcion1 = new JTextField();
        rbOpcion2 = new JRadioButton("Opcion2");
        tfOpcion2 = new JTextField();
        rbOpcion3 = new JRadioButton("Opcion3");
        tfOpcion3 = new JTextField();
        rbOpcion4 = new JRadioButton("Opcion4");
        tfOpcion4 = new JTextField();
        
        JPanel pEnunciado = new JPanel(new BorderLayout());
        pEnunciado.add(lNumeroPregunta, BorderLayout.WEST);
        pEnunciado.add(enunciadoPregunta, BorderLayout.CENTER);
        pIngresarPreguntas.add(pEnunciado, BorderLayout.NORTH);
        
        pIngresarPreguntas.add(cuerpoPregunta, BorderLayout.CENTER);
        
        JPanel pOpciones = new JPanel(new GridLayout(4, 2));
        pOpciones.add(rbOpcion1);
        pOpciones.add(tfOpcion1);
        pOpciones.add(rbOpcion2);
        pOpciones.add(tfOpcion2);
        pOpciones.add(rbOpcion3);
        pOpciones.add(tfOpcion3);
        pOpciones.add(rbOpcion4);
        pOpciones.add(tfOpcion4);
        pIngresarPreguntas.add(pOpciones, BorderLayout.SOUTH);
        
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
        pestañas.add(pInfoExamenes, "Informacion de Examanes Presentados");
        container.add(pestañas);

        setVisible(true);
        setSize(700, 500);
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
    
    public void borrarCamposExamenes(){
        nombreExamen.setText("");
    }
    
    public void borrarCamposOpcionesPregunta(){
        enunciadoPregunta.setText("");
        cuerpoPregunta.setText("");
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
    
    public String getEnunciadoPregunta(){
        return enunciadoPregunta.getText();
    }
    
    public String getCuerpoPregunta(){
        return cuerpoPregunta.getText();
    }
    
    public ArrayList getOpciones(){
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add(tfOpcion1.getText());
        opciones.add(tfOpcion2.getText());
        opciones.add(tfOpcion3.getText());
        opciones.add(tfOpcion4.getText());
        return opciones;
    }
    
    public void actualizarPreguntas(){

    }
    
    public int opcionVerdadera(){
        if(rbOpcion1.getAutoscrolls()){
            return 1;
        }
        else if(rbOpcion2.getAutoscrolls()){
            return 2;
        }
        else if (rbOpcion3.getAutoscrolls()){
            return 3;
        }
        else if(rbOpcion4.getAutoscrolls()){
            return 4;
        }else{
            return 0;
        }
    }
    
    public void addExamen(String nombreExamen){
        jcbExamenes.addItem(nombreExamen);
    }

}
