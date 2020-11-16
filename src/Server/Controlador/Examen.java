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

    public Examen() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void addPregunta(Pregunta p) {
        preguntas.add(p);
    }

    public int numeroPreguntas() {
        return preguntas.size();
    }
}
