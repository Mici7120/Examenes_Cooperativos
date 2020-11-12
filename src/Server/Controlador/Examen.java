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
    ArrayList<Pregunta> preguntas;
    
    public Examen(){
        
    }
    
    public void addPregunta(Pregunta p){
        preguntas.add(p);
    }
}
