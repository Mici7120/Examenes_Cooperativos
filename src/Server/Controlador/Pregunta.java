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

    /**
     * constructor
     * @param enunciado
     * @param cuerpo
     * @param opciones
     * @param opcCorrecta 
     */
    public Pregunta(String enunciado, String cuerpo, ArrayList<String> opciones, String opcCorrecta) {
        this.enunciado = enunciado;
        this.cuerpo = cuerpo;
        this.opciones = opciones;
        this.opcCorrecta = opcCorrecta;
        this.estado = "LIBRE";
    }

    /**
     * obtiene el enunciado
     * @return String con el enunciado
     */
    public String getEnunciado() {
        return enunciado;
    }

    /**
     * obtiene el cuerpo
     * @return String con el cuerpo 
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * obtiene la opcion correcta
     * @return string con la opcion correcta
     */
    public String getOpcCorrecta() {
        return opcCorrecta;
    }

    /**
     * obtiene la lista de opciones
     * @return lista de opciones
     */
    public ArrayList<String> getOpciones() {
        return opciones;
    }

    /**
     * obtiene el estado de la pregunta
     * @return String si esta libre, ocupada o respondida
     */
    public String getEstado() {
        return estado;
    }

    /**
     * guarda el nuevo estado de la pregunta
     * @param nuevoEstado (libre, ocupada o respondida)
     */
    public void setEstado(String nuevoEstado) {
        estado = nuevoEstado;
    }

    /**
     * dice si la pregunta esta disponible
     * @return boolean si esta libre o no
     */
    public boolean getDisponible() {
        if (estado.equals("LIBRE")) {
            return true;
        } else {
            return false;
        }
    }

}
