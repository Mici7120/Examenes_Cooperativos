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
    double respuestasCorrectas = 0;
    double totalPreguntas = 0;

    /**
     * constructor
     */
    public InformeExamen() {
        nombre = "";
        informeExamen = "";
    }

    /**
     * guarda el nombre del informe
     * @param nombre 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * obtiene el nombre del informe
     * @return 
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * registra la respuesta del estudiante cual es, si es correcta o incorrecta y quien la respondio
     * @param nombreEstudiante
     * @param pregunta
     * @param respuestaEstudiante
     * @param correcta 
     */
    public void registrarRespuesta(String nombreEstudiante, String pregunta, String respuestaEstudiante, boolean correcta) {
        totalPreguntas++;
        informeExamen += pregunta + " respondida por " + nombreEstudiante + ", " + respuestaEstudiante;
        if (correcta) {
            respuestasCorrectas++;
            informeExamen += " - Respuesta Correcta\n";
        } else {
            informeExamen += " - Respuesta Incorrecta\n";
        }
    }
    
    /**
     * asigna los datos al informe
     * @param nombre
     * @param informeExamen
     * @param totalPreguntas
     * @param respuestasCorrectas 
     */
    public void setInforme(String nombre, String informeExamen, double totalPreguntas, double respuestasCorrectas){
        this.nombre = nombre;
        this.informeExamen = informeExamen;
        this.totalPreguntas = totalPreguntas;
        this.respuestasCorrectas = respuestasCorrectas;
    }

    /**
     * obtiene el informe con la nota
     * @return String con los datos del informe
     */
    public String getInforme() {
        double nota = (respuestasCorrectas / totalPreguntas) * 5;
        String informe = nombre + "\n" + informeExamen + "Respuestas Correctas: " + respuestasCorrectas + "\nCalificacion final: " + nota;
        return informe;
    }
}
