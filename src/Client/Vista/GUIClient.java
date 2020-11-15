/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Vista;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author villa
 */
public class GUIClient extends JFrame{

    Container contenPPal;
    JPanel pExamColab, pDerecho, pIzquierdo, pCentro, pCenArri, pCenAbaj;
    JLabel lPreguntas, lTimeRest; 
    JTextField tRestTime;
    JComboBox<String> selectPregunta;
    JButton bObtener, bEnviar, pCancelar;
    public JTextArea tAreaMensajes;
    JScrollPane barras;
    JCheckBox cPreguntaA, cPreguntaB, cPreguntaC, cPreguntaD;
    
    
    public GUIClient() 
    {
        
        //super("Cliente - Ejemplo");
        
        pExamColab = new JPanel();
        pDerecho = new JPanel();
        pIzquierdo = new JPanel();
        pCentro = new JPanel();
        pCenArri = new JPanel();
        pCenAbaj = new JPanel();
                
        lPreguntas = new JLabel("Preguntas: ");
        lTimeRest = new JLabel("Tiempo restante: ");
        
        selectPregunta = new JComboBox<>();
        
        tRestTime = new JTextField();
        
        bObtener = new JButton("Obtener");
        bEnviar = new JButton("Enviar");
        pCancelar = new JButton("Cancelar");
        
        tAreaMensajes = new JTextArea("Mensaje: ");
        barras = new JScrollPane(tAreaMensajes);
        
        cPreguntaA = new JCheckBox("A. ");
        cPreguntaB = new JCheckBox("B. ");
        cPreguntaC = new JCheckBox("C. ");
        cPreguntaD = new JCheckBox("D. ");
        
        selectPregunta.addItem("Seleccione la pregunta");
        
        lPreguntas.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bObtener.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pIzquierdo.setLayout(new BoxLayout(pIzquierdo, 1));
        pIzquierdo.add(lPreguntas);
        pIzquierdo.add(Box.createRigidArea(new Dimension(0,30)));
        pIzquierdo.add(selectPregunta);
        pIzquierdo.add(Box.createRigidArea(new Dimension(0,290)));
        pIzquierdo.add(bObtener);
        
        pCenArri.setLayout(new BoxLayout(pCenArri, 1));
        pCenArri.add(barras);
        pCenArri.add(Box.createRigidArea(new Dimension(0,5)));
        pCenArri.add(cPreguntaA);
        pCenArri.add(Box.createRigidArea(new Dimension(0,5)));
        pCenArri.add(cPreguntaB);
        pCenArri.add(Box.createRigidArea(new Dimension(0,5)));
        pCenArri.add(cPreguntaC);
        pCenArri.add(Box.createRigidArea(new Dimension(0,5)));
        pCenArri.add(cPreguntaD);
        
        pCenAbaj.setLayout(new BoxLayout(pCenAbaj, 2));
        pCenAbaj.add(Box.createRigidArea(new Dimension(0,10)));
        pCenAbaj.add(bEnviar);
        pCenAbaj.add(Box.createRigidArea(new Dimension(0,10)));
        pCenAbaj.add(pCancelar);
        
        pCentro.setLayout(new BoxLayout(pCentro, 1));
        pCentro.add(pCenArri);
        pCentro.add(pCenAbaj);
        
        pDerecho.setLayout(new BoxLayout(pDerecho, 1));
        pDerecho.add(lTimeRest);
        pDerecho.add(tRestTime);
        pDerecho.add(Box.createRigidArea(new Dimension(0,1000)));
        
        pExamColab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3),"Examen Colaborativo Univalle"));
        pExamColab.setLayout(new BorderLayout());
        pExamColab.add(pIzquierdo,BorderLayout.WEST);
        pExamColab.add(pCentro,BorderLayout.CENTER);
        pExamColab.add(pDerecho,BorderLayout.EAST);
        
        contenPPal = getContentPane();
        contenPPal.setLayout(new GridLayout());
        contenPPal.add(pExamColab);
        
        setTitle("Cliente");
        setSize(800, 500);
        setResizable(false);
        setVisible(true);
     
        //pack();
        //setVisible(true);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
