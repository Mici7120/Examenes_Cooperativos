/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Vista.GUIServer;

/**
 *
 * @author camil
 */
public class Validaciones {
    public static boolean validarBotonAgregarExamen(GUIServer interfaz){
        if(interfaz.getNombreExamen().equals("") 
                || interfaz.getDuracion().equals("")){
            return false;
        }else {
            return true;
        }
    }
}
