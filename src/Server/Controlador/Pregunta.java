/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controlador;

import java.util.ArrayList;

/**
 *
 * @author villa
 */
public class Pregunta {
    String enunciado, cuerpo;
    ArrayList <String> opciones;
    String opcCorrecta;
    boolean disponible;

    public Pregunta(String enunciado, String cuerpo, ArrayList<String> opciones, String opcCorrecta){
        this.enunciado = enunciado;
        this.cuerpo = cuerpo;
        this.opciones = opciones;
        this.opcCorrecta = opcCorrecta;
        disponible = true;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public String getOpcCorrecta() {
        return opcCorrecta;
    }

    public ArrayList<String> getOpciones() {
        return opciones;
    }
    
    public boolean getDisponible(){
        return disponible;
    }
    
    public void setDisponible(boolean estado){
        disponible = estado;
    }
}
