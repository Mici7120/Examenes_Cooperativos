/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Vista.GUIServer;
import javax.swing.JOptionPane;

/**
 *
 * @author camil
 */
public class Validaciones {
    public static boolean validarBotonAgregarExamen(GUIServer interfaz){
        try{
        int numero = Integer.parseInt(interfaz.getDuracion());
        if(interfaz.getNombreExamen().equals("") 
                || interfaz.getDuracion().equals("")
                || numero < 1){
            return false;
        }else {
            return true;
        }
        }catch(Exception e){
            return false;
        }
    }
}
