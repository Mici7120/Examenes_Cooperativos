/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author villa
 */
public class Fachada {

    JFileChooser fileChooser;
    File archivo;

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
}
