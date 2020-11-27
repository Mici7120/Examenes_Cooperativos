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
public class InformeExamen {

    String nombre;
    String informeExamen;
    int respuestasCorrectas = 0;
    int totalPreguntas = 0; 

    public InformeExamen() {
        nombre = "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void registrarRespuesta(String nombreEstudiante, String pregunta, String respuestaEstudiante, boolean correcta) {
        totalPreguntas++;
        informeExamen += pregunta + " respondida por " + nombreEstudiante + ", " + respuestaEstudiante;
        if (correcta) {
            respuestasCorrectas++;
            informeExamen += " - Respuesta Correcta\n\n";
        } else {
            informeExamen += " - Respuesta Incorrecta\n\n";
        }
    }

    public String getInforme() {
        double nota = (respuestasCorrectas / totalPreguntas) * 5;
        String informe = nombre + "\n" + informeExamen + "Respuestas Correctas: " + respuestasCorrectas + "\nCalificacion final: " + nota;
        return informe;
    }
}
