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
    ArrayList<String> informacionPregunta;
    int respuestasCorrectas = 0;
    
    public InformeExamen(){
        informacionPregunta = new ArrayList<>();
    }
    
    public void respuestaCorrecta(String nombreEstudiante, String pregunta, String respuestaEstudiante, boolean correcta){
        String info = "\n" + pregunta + " " + respuestaEstudiante + " " + nombreEstudiante;
        if(correcta){
            respuestasCorrectas ++;
            info += " Respuesta Correcta";
        }else{
            info += " Respuesta Incorrecta";            
        }
    }
}
