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

    public GUIServer() {
        super("Servidor - Examenes Cooperativos");

        container = getContentPane();

        //configuracion de la pestaña configurar examenes
        pConfiExamenes = new JPanel();
        pConfiExamenes.setLayout(new BorderLayout());
        lNombreExamen = new JLabel("Nombre del examen:");
        nombreExamen = new JTextField();
        lDuracion = new JLabel("Duracion en segundos");
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

        tfDuracion = new JFormattedTextField();

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

    public void asignarEscuchasBotones(ActionListener escucha) {
        bCargarArchivo.addActionListener(escucha);
        bAgregarExamen.addActionListener(escucha);
        bIniciarExamen.addActionListener(escucha);
        bLimpiarAreaEstadoServidor.addActionListener(escucha);
    }

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

    public void borrarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablePreguntas.getModel();

        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    public JButton getBAgregarExamen() {
        return bAgregarExamen;
    }

    public String getExamenSeleccionado() {
        return jcbExamenes.getSelectedItem().toString();
    }

    public JButton getBIniciarExamen() {
        return bIniciarExamen;
    }

    public String getNombreExamen() {
        return nombreExamen.getText();
    }

    public int getDuracion() {
        return Integer.parseInt(tfDuracion.getText());
    }

    public void addExamenJCB(String nombreExamen) {
        jcbExamenes.addItem(nombreExamen);
    }
    
    public void addInformeExamenJCB(String nombreInforme){
        jcbInfoExamenes.addItem(nombreInforme);
    }

    public void appendEstadoServidor(String mensaje) {
        estadoServidor.append(mensaje);
    }
    
    public void printInformeExamem(String mensaje){
        taInformeExamen.setText(mensaje);
    }

    public JButton getbCargarArchivo() {
        return bCargarArchivo;
    }

    public JButton getBLimpiarAreaEstadoServidor() {
        return bLimpiarAreaEstadoServidor;
    }
    
    public JButton getBConsultarInforme(){
        return bConsultarInforme;
    }
    
    public String getInformeSeleccionado() {
        return jcbInfoExamenes.getSelectedItem().toString();
    }

    public void limpiarCamposPregunta() {
        nombreExamen.setText("");
        tfDuracion.setText("");
    }

    public void limpiarAreaEstadoServidor() {
        estadoServidor.setText("");
    }

    public void setDuracionRestante(int segundos) {
        estadoTiempo.setText("" + segundos);
    }
}
