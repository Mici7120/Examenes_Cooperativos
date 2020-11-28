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
public class Examen {

    String nombre;
    ArrayList<Pregunta> preguntas = new ArrayList<>();
    int duracion;

    /**
     * constructor
     */
    public Examen() {
    }

    /**
     * guarda el nombre del examen
     * @param nombre 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * obtiene el nombre del examen
     * @return String nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * obtiene la duracion del examen
     * @param duracion numero en minutos
     */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    /**
     * obtiene la duracion del examen
     * @return int minutos 
     */
    public int getDuracion() {
        return duracion;
    }

    /**
     * agrega una pregunta a la lista de preguntas
     * @param p 
     */
    public void addPregunta(Pregunta p) {
        preguntas.add(p);
    }

    /**
     * dice la cantidad de preguntas 
     * @return numero de preguntas
     */
    public int numeroPreguntas() {
        return preguntas.size();
    }

    /**
     * obtiene la pregunta del array de preguntas
     * @param indice de la pregunta
     * @return la pregunta
     */
    public Pregunta getPregunta(int indice) {
        return preguntas.get(indice - 1);
    }

    /**
     * obtiene los enunciados de las preguntas
     * @return los enunciados
     */
    public ArrayList<String> getInfoPreguntas() {
        ArrayList<String> enunciados = new ArrayList<>();
        for (Pregunta x : preguntas) {
            enunciados.add(x.getEnunciado());
            enunciados.add(x.getCuerpo());
        }
        return enunciados;
    }
}
