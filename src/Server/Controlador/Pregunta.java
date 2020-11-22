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
    String estado; // los valores posibles son: "Libre", "Ocupada", "Respondida"
    String respondidaPor;
    boolean calificacion; // true si es correcta; false si es incorrecta

    public Pregunta(String enunciado, String cuerpo, ArrayList<String> opciones, String opcCorrecta){
        this.enunciado = enunciado;
        this.cuerpo = cuerpo;
        this.opciones = opciones;
        this.opcCorrecta = opcCorrecta;
        this.calificacion = false;
        this.estado = "Libre";
        this.respondidaPor = "NO RESPONDIDA";
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

    public void setRespondida(boolean nota, String nombreEstudiante) {
        estado = "Respondida";
        calificacion = nota;
        respondidaPor = nombreEstudiante;
    }
    
    public boolean getDisponible(){
        if (estado.equals("Libre")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getCalificacion() {
        return calificacion;
    }

    public String getRespondidaPor() {
        return respondidaPor;
    }
}
