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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author villa
 */
public class GUIServer extends JFrame {

    Container container;
    JTabbedPane pestañas;
    JPanel /*1 Pestaña*/ pConfiExamenes, pConfiguracion,/*2 Pestaña*/ pIniExamen, pInfoExamenes, pCargarExamen, pOpIniExamen, /*3 Pestañ
            a*/ pOpInfoExamenes;
    JTextField nombreExamen, tfDuracion, estadoTiempo;
    JTextArea estadoServidor, taInformeExamen;
    JScrollPane jcpEstadoServidor, spTabla;
    JLabel lNombreExamen, lDuracion, lInfoDuracion, lInformes;
    JButton bAgregarExamen, bCargarArchivo, bIniciarExamen, bLimpiarAreaEstadoServidor, bConsultarInforme;
    JComboBox jcbExamenes, jcbInfoExamenes;
    JTable tablePreguntas;

    int numeroPregunta = 1;

    /**
     * contructor de la GUI del server donde se inicializan todos los componentes
     */
    public GUIServer() {
        super("Servidor - Examenes Cooperativos");

        container = getContentPane();

        //configuracion de la pestaña configurar examenes
        pConfiExamenes = new JPanel();
        pConfiExamenes.setLayout(new BorderLayout());
        lNombreExamen = new JLabel("Nombre del examen:");
        nombreExamen = new JTextField();
        lDuracion = new JLabel("Duracion en minutos");
        bAgregarExamen = new JButton("Agregar Examen");
        bCargarArchivo = new JButton("Cargar Archivo");

        String[] columnNames = {"Enunciado", "Cuerpo"};
        DefaultTableModel modelo = new DefaultTableModel(columnNames, 0);
        tablePreguntas = new JTable(modelo);
        spTabla = new JScrollPane(tablePreguntas);

        pCargarExamen = new JPanel();
        pCargarExamen.setLayout(new BorderLayout());
        pCargarExamen.add(bCargarArchivo, BorderLayout.WEST);
        pCargarExamen.add(spTabla, BorderLayout.CENTER);

        MaskFormatter mask;
        try {
            mask = new MaskFormatter("##");
            tfDuracion = new JFormattedTextField(mask);
        } catch (ParseException ex) {
            Logger.getLogger(GUIServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        pConfiguracion = new JPanel(new GridLayout(1, 4, 25, 0));
        pConfiguracion.add(lNombreExamen);
        pConfiguracion.add(nombreExamen);
        pConfiguracion.add(lDuracion);
        pConfiguracion.add(tfDuracion);

        pConfiExamenes.add(pConfiguracion, BorderLayout.NORTH);
        pConfiExamenes.add(pCargarExamen, BorderLayout.CENTER);
        pConfiExamenes.add(bAgregarExamen, BorderLayout.SOUTH);

        //configuracion de la pestaña iniciar examen
        pIniExamen = new JPanel();
        pIniExamen.setLayout(new BorderLayout());
        estadoServidor = new JTextArea();
        estadoTiempo = new JTextField();
        bIniciarExamen = new JButton("Iniciar Examen");
        bLimpiarAreaEstadoServidor = new JButton("Limpiar Area");
        jcbExamenes = new JComboBox();
        lInfoDuracion = new JLabel("Duracion Restante");

        pOpIniExamen = new JPanel(new GridLayout(0, 5, 10, 0));
        pOpIniExamen.add(jcbExamenes);
        pOpIniExamen.add(bIniciarExamen);
        pOpIniExamen.add(bLimpiarAreaEstadoServidor);
        pOpIniExamen.add(lInfoDuracion);
        pOpIniExamen.add(estadoTiempo);

        jcpEstadoServidor = new JScrollPane(estadoServidor);
        pIniExamen.add(pOpIniExamen, BorderLayout.NORTH);
        pIniExamen.add(jcpEstadoServidor, BorderLayout.CENTER);

        //configuracion de la pestaña informacion de examenes prestados
        pInfoExamenes = new JPanel(new BorderLayout());
        taInformeExamen = new JTextArea();

        pOpInfoExamenes = new JPanel();
        lInformes = new JLabel("Seleccione Informe");
        jcbInfoExamenes = new JComboBox();
        bConsultarInforme = new JButton("Consultar");

        pOpInfoExamenes.add(lInformes);
        pOpInfoExamenes.add(jcbInfoExamenes);
        pOpInfoExamenes.add(bConsultarInforme);

        pInfoExamenes.add(pOpInfoExamenes, BorderLayout.NORTH);
        pInfoExamenes.add(taInformeExamen, BorderLayout.CENTER);

        //configuracion de las pestañas
        pestañas = new JTabbedPane();
        pestañas.add(pConfiExamenes, "ConfigurarExamenes");
        pestañas.add(pIniExamen, "Iniciar Examen");
        pestañas.add(pInfoExamenes, "Informacion de Examenes Presentados");
        container.add(pestañas);
        
        setVisible(true);
        setSize(800, 500);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * asigna las escuchas a los botones
     * @param escucha 
     */
    public void asignarEscuchasBotones(ActionListener escucha) {
        bCargarArchivo.addActionListener(escucha);
        bAgregarExamen.addActionListener(escucha);
        bIniciarExamen.addActionListener(escucha);
        bLimpiarAreaEstadoServidor.addActionListener(escucha);
        bConsultarInforme.addActionListener(escucha);
    }

    //Retornar botones
    /**
     * retorna el boton de agregar examen
     * @return 
     */
    public JButton getBAgregarExamen() {
        return bAgregarExamen;
    }

    /**
     * retorna el boton de cargar archivo
     * @return 
     */
    public JButton getbCargarArchivo() {
        return bCargarArchivo;
    }

    /**
     * retorna el boton de iniciar examen
     * @return 
     */
    public JButton getBIniciarExamen() {
        return bIniciarExamen;
    }

    /**
     * retorna el boton de limpiar el area del servidor
     * @return 
     */
    public JButton getBLimpiarAreaEstadoServidor() {
        return bLimpiarAreaEstadoServidor;
    }

    /**
     * retorna el boton de consultar informe
     * @return 
     */
    public JButton getBConsultarInforme() {
        return bConsultarInforme;
    }

    /**
     * imprime el enunciado y el cuerpo de las preguntas del examen a cargar
     * @param infoPreguntas 
     */
    public void mostrarPreguntasTabla(ArrayList<String> infoPreguntas) {
        DefaultTableModel model = (DefaultTableModel) tablePreguntas.getModel();
        Object[] datos = new Object[model.getColumnCount()];
        Iterator iterator = infoPreguntas.iterator();
        while (iterator.hasNext()) {
            datos[0] = iterator.next().toString();
            datos[1] = iterator.next().toString();
            model.addRow(datos);
        }
        tablePreguntas.setModel(model);
    }

    /**
     * borro todos los elementos de la tabla
     */
    public void borrarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablePreguntas.getModel();

        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    /**
     * retorna el nombre del examen seleccionado en el JComboBox para iniciar un examen
     * @return 
     */
    public String getExamenSeleccionado() {
        return jcbExamenes.getSelectedItem().toString();
    }

    /**
     * retorna el nombre configurado para un examen
     * @return 
     */
    public String getNombreExamen() {
        return nombreExamen.getText();
    }

    /**
     * retorna la duracion configurada para un examen
     * @return 
     */
    public String getDuracion() {
        return tfDuracion.getText();
    }

    /**
     * añade al JComboBox de examenes el nombre de examen recibido
     * @param nombreExamen 
     */
    public void addExamenJCB(String nombreExamen) {
        jcbExamenes.addItem(nombreExamen);
    }

    /**
     * añade al JComboBox de informes el nombre del informe de un examen
     * @param nombreInforme 
     */
    public void addInformeExamenJCB(String nombreInforme) {
        jcbInfoExamenes.addItem(nombreInforme);
    }

    /**
     * añade el mensaje recibido en el area de texto del estado del servidor
     * @param mensaje 
     */
    public void appendEstadoServidor(String mensaje) {
        estadoServidor.append(mensaje);
    }

    /**
     * imprime en la GUI el informe correspondiente
     * @param mensaje 
     */
    public void printInformeExamem(String mensaje) {
        taInformeExamen.setText(mensaje);
    }

    /**
     * retorna el nombre del informe seleccionado
     * @return 
     */
    public String getInformeSeleccionado() {
        return jcbInfoExamenes.getSelectedItem().toString();
    }

    /**
     * pone en blanco los campos de texto del nombre del examen y el de duracion
     */
    public void limpiarCamposPregunta() {
        nombreExamen.setText("");
        tfDuracion.setText("");
    }

    /**
     * pone en blanco el area de texto del estado del servidor
     */
    public void limpiarAreaEstadoServidor() {
        estadoServidor.setText("");
    }

    /**
     * imprime en la GUI la duracion restante del examen
     * @param tiempo 
     */
    public void setDuracionRestante(String tiempo) {
        estadoTiempo.setText("" + tiempo);
    }
}
