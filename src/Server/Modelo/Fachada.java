/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Modelo;

import Server.Controlador.InformeExamen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author villa
 */
public class Fachada {

    JFileChooser fileChooser;
    File archivo;
    FileWriter guardar;
    PrintWriter pWriter;

    public Fachada() {

    }

    public ArrayList<String> cargarArchivo() {
        ArrayList<String> datos = new ArrayList<>();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int a = fileChooser.showOpenDialog(new JFrame());

        archivo = fileChooser.getSelectedFile();

        Scanner buffer = null;
        try {
            buffer = new Scanner(archivo);
            while (buffer.hasNextLine()) {
                for (int i = 0; i < 7; i++) {
                    datos.add(buffer.nextLine());
                }
            }
            return datos;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            buffer.close();
        }

        return null;
    }

    public void guardarInformes(ArrayList<InformeExamen> informes) {
        archivo = new File("Informes.txt");
        try {
            guardar = new FileWriter(archivo);
            pWriter = new PrintWriter(guardar);
            for (InformeExamen i : informes) {
                StringTokenizer token = new StringTokenizer(i.getInforme(), "\n");
                while (token.hasMoreTokens()) {
                    pWriter.println(token.nextToken());
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo");
        } finally {
            try {
                if (guardar != null) {
                    guardar.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public ArrayList<InformeExamen> cargarInformes() {
        archivo = new File("Informes.txt");
        Scanner buffer = null;
        ArrayList<InformeExamen> informes = new ArrayList<>();
        try {
            buffer = new Scanner(archivo);
            while (buffer.hasNextLine()) {
                InformeExamen informeExamen = new InformeExamen();
                String nombre = buffer.nextLine();
                //System.out.println(nombre);
                String informe = "";
                int totalPreguntas = 0;
                String dato = buffer.nextLine();
                //System.out.println(dato);
                while (!dato.contains("Respuestas")) {
                    informe += dato + "\n";
                    totalPreguntas++;
                    dato = buffer.nextLine();
                    //System.out.println(dato);
                };
                String[] split = dato.split(":");
                double respuestasCorrectas = Double.parseDouble(split[1].trim());
                //System.out.println(respuestasCorrectas);
                dato = buffer.nextLine();
                informeExamen.setInforme(nombre, informe, totalPreguntas, respuestasCorrectas);
                informes.add(informeExamen);
            }
            return informes;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No se ha cargado correctamente los informes");
            return new ArrayList<>();
        }
    }
}
