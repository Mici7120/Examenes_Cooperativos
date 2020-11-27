/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controlador;

/**
 *
 * @author ruzbe
 */
public class EstadoPregunta {
    int numero;
    boolean visible;
    
    /**
     * Constructor de EstadoPregunta
     * @param num numero de la pregunta
     * @param vis si la pregunta es visible
     */
    public EstadoPregunta(int num, boolean vis) {
        this.numero = num;
        this.visible = vis;
    }
    
    /**
     * establece el valor de visible
     * @param estado 
     */
    public void setVisible(boolean estado){
        visible = estado;
    }

    /**
     * retorna el valor de visible
     * @return boolean si es o no visible
     */
    public boolean esVisible() {
        return visible;
    }

    /**
     * obtiene el numero de la pregunta
     * @return int con el numero de la pregunta
     */
    public int getNumero() {
        return numero;
    }

}
