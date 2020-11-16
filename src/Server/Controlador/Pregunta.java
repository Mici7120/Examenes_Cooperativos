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
    int verdadera;

    public Pregunta(String enunciado, String cuerpo, ArrayList<String> opciones, int verdadera){
        this.enunciado = enunciado;
        this.cuerpo = cuerpo;
        this.opciones = opciones;
        this.verdadera = verdadera;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public int esCorrecta() {
        return verdadera;
    }

    public ArrayList<String> getOpciones() {
        return opciones;
    }
    
}
