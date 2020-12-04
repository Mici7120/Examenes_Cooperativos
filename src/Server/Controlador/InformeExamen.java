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
    ArrayList<Pregunta> preguntas;
    ArrayList<Pregunta> preguntasCorrectas;
    ArrayList<Pregunta> preguntasIncorrectas;

    /**
     * constructor
     */
    public InformeExamen() {
        preguntasCorrectas = new ArrayList<>();
        preguntasIncorrectas = new ArrayList<>();
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
    
    public void setTotalPreguntas(int totalPreguntas){
        this.totalPreguntas = totalPreguntas;
    }

    /**
     * registra la respuesta del estudiante cual es, si es correcta o incorrecta y quien la respondio
     * @param nombreEstudiante
     * @param enunciado
     * @param cuerpo
     * @param respuestaEstudiante
     * @param correcta 
     */
    public synchronized void registrarRespuesta(String nombreEstudiante, String enunciado, String cuerpo, String respuestaEstudiante, boolean correcta) {
        informeExamen += enunciado + " respondida por " + nombreEstudiante + ", " + respuestaEstudiante;
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
        String informe = "\n" + nombre + "\n" + informeExamen + "Respuestas Correctas: " + respuestasCorrectas + "\nCalificacion final: " + nota;
        return informe;
    }
    
    public double getNota(){
        double nota = (respuestasCorrectas / totalPreguntas) * 5;
        return nota;
    }
}
