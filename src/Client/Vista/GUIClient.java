/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Vista;

import javax.swing.JFrame;

/**
 *
 * @author villa
 */
public class GUIClient extends JFrame{
    
    
    
    public GUIClient(){
        setTitle("Cliente");
        setVisible(true);
        pack();
    }
    
    public static void main(String[] args){
        GUIClient client = new GUIClient();
        
        
    }
}
