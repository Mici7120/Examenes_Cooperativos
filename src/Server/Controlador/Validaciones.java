/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import Server.Vista.GUIServer;
import java.util.ArrayList;

/**
 *
 * @author villa
 */
public class Validaciones {
    public static boolean validarCampoOpcionesPregunta(GUIServer interfaz){
        ArrayList<String> opciones = interfaz.getOpciones();
        for(String x: opciones){
            if(x.isEmpty()){
                return false;
            }
        }
        if(interfaz.getCuerpoPregunta().isEmpty()
                || interfaz.getEnunciadoPregunta().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
