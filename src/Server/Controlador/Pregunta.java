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
    ArrayList<String> opciones;
    String opcCorrecta;
    String estado; // los valores posibles son: "LIBRE", "OCUPADA", "RESPONDIDA"

    public Pregunta(String enunciado, String cuerpo, ArrayList<String> opciones, String opcCorrecta) {
        this.enunciado = enunciado;
        this.cuerpo = cuerpo;
        this.opciones = opciones;
        this.opcCorrecta = opcCorrecta;
        this.estado = "LIBRE";
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String nuevoEstado) {
        estado = nuevoEstado;
    }

    public boolean getDisponible() {
        if (estado.equals("LIBRE")) {
            return true;
        } else {
            return false;
        }
    }

}
