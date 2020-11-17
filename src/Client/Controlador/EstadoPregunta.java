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
    
    public EstadoPregunta(int num, boolean vis) {
        this.numero = num;
        this.visible = vis;
    }
    
    public void setVisible(boolean estado){
        visible = estado;
    }

    public boolean esVisible() {
        return visible;
    }

    public int getNumero() {
        return numero;
    }

}
