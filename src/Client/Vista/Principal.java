/*
  Autor: Ruzbellit Rossy Romero Ramirez (1925456)
  Email: ruzbellit.romero@correounivalle.edu.co
  Autor: Christian Villanueva Paez (1924546)
  Email: christian.villanueva@correounivalle.edu.co
  Autor: Daniel Rodriguez Sanchez (1927631)
  Email: daniel.rodriguez.sanchez@correounivalle.edu.co
*/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Vista;

import Client.Controlador.LogicaCliente;

/**
 *
 * @author ruzbe
 */
public class Principal {

    public static void main(String[] args) {
        GUIClient client = new GUIClient();
        LogicaCliente logica = new LogicaCliente(client);
        logica.conectar("127.0.0.1");
    }
}
