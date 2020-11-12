/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Vista;

import Server.Controlador.ConexionesServidor;

/**
 *
 * @author villa
 */
public class Principal {

    public static void main(String[] args) {
        GUIServer gui = new GUIServer();
        ConexionesServidor handler = new ConexionesServidor(gui);
    }
}
