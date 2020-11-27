/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Vista;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author villa
 */
public class GUIClient extends JFrame {

    Container contenPPal;
    JPanel pExamColab, pDerecho, pIzquierdo, pCentro, pCenArri, pCenAbaj;
    JLabel lPreguntas, lTimeRest;
    JTextField tRestTime;
    JComboBox<String> selectPregunta;
    JButton bObtener, bEnviar, bCancelar;
    JTextArea tAreaMensajes;
    JScrollPane barras;
    JRadioButton rBPreguntaA, rBPreguntaB, rBPreguntaC, rBPreguntaD;

    public GUIClient() {

        //super("Cliente - Ejemplo");
        pExamColab = new JPanel();
        pDerecho = new JPanel();
        pIzquierdo = new JPanel();
        pCentro = new JPanel();
        pCenArri = new JPanel();
        pCenAbaj = new JPanel();

        lPreguntas = new JLabel("Preguntas Disponibles:");
        lTimeRest = new JLabel("Tiempo restante: ");

        selectPregunta = new JComboBox<>();

        tRestTime = new JTextField();

        bObtener = new JButton("Obtener");
        bEnviar = new JButton("Enviar");
        bCancelar = new JButton("Cancelar");

        tAreaMensajes = new JTextArea("Mensaje: ");
        barras = new JScrollPane(tAreaMensajes);

        rBPreguntaA = new JRadioButton("");
        rBPreguntaB = new JRadioButton("");
        rBPreguntaC = new JRadioButton("");
        rBPreguntaD = new JRadioButton("");

        ButtonGroup rBgrupo = new ButtonGroup();
        rBgrupo.add(rBPreguntaA);
        rBgrupo.add(rBPreguntaB);
        rBgrupo.add(rBPreguntaC);
        rBgrupo.add(rBPreguntaD);

        lPreguntas.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bObtener.setAlignmentX(Component.CENTER_ALIGNMENT);

        pIzquierdo.setLayout(new BoxLayout(pIzquierdo, 1));
        pIzquierdo.add(lPreguntas);
        pIzquierdo.add(Box.createRigidArea(new Dimension(0, 30)));
        pIzquierdo.add(selectPregunta);
        pIzquierdo.add(Box.createRigidArea(new Dimension(0, 290)));
        pIzquierdo.add(bObtener);

        pCenArri.setLayout(new BoxLayout(pCenArri, 1));
        pCenArri.add(barras);
        pCenArri.add(Box.createRigidArea(new Dimension(0, 5)));
        pCenArri.add(rBPreguntaA);
        pCenArri.add(Box.createRigidArea(new Dimension(0, 5)));
        pCenArri.add(rBPreguntaB);
        pCenArri.add(Box.createRigidArea(new Dimension(0, 5)));
        pCenArri.add(rBPreguntaC);
        pCenArri.add(Box.createRigidArea(new Dimension(0, 5)));
        pCenArri.add(rBPreguntaD);

        pCenAbaj.setLayout(new BoxLayout(pCenAbaj, 2));
        pCenAbaj.add(Box.createRigidArea(new Dimension(0, 10)));
        pCenAbaj.add(bEnviar);
        pCenAbaj.add(Box.createRigidArea(new Dimension(0, 10)));
        pCenAbaj.add(bCancelar);

        pCentro.setLayout(new BoxLayout(pCentro, 1));
        pCentro.add(pCenArri);
        pCentro.add(pCenAbaj);

        pDerecho.setLayout(new BoxLayout(pDerecho, 1));
        pDerecho.add(lTimeRest);
        pDerecho.add(tRestTime);
        pDerecho.add(Box.createRigidArea(new Dimension(0, 1000)));

        pExamColab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3), "Examen Colaborativo Univalle"));
        pExamColab.setLayout(new BorderLayout());
        pExamColab.add(pIzquierdo, BorderLayout.WEST);
        pExamColab.add(pCentro, BorderLayout.CENTER);
        pExamColab.add(pDerecho, BorderLayout.EAST);

        contenPPal = getContentPane();
        contenPPal.setLayout(new GridLayout());
        contenPPal.add(pExamColab);

        setTitle("Cliente");
        setSize(800, 500);
        setResizable(false);
        setVisible(true);

        //pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void asignarEscuchasBotones(ActionListener escucha) {
        bObtener.addActionListener(escucha);
        bEnviar.addActionListener(escucha);
        bCancelar.addActionListener(escucha);
    }
    public JButton getBObtenerPregunta() {
        return bObtener;
    }
    
    public JButton getBEnviarPregunta() {
        return bEnviar;
    }
    
    public JButton getBCancelarPregunta() {
        return bCancelar;
    }
    
    public JComboBox<String> getComboBoxSelectPregunta() {
        return selectPregunta;
    }
    
    public JTextArea getAreaMensajes() {
        return tAreaMensajes;
    }
    
    public JPanel getPanelIzquierdo() {
        return pIzquierdo;
    }
    

    public void setRBOpciones(String opcionA, String opcionB, String opcionC, String opcionD) {
        rBPreguntaA.setText("A. " + opcionA);
        rBPreguntaB.setText("B. " + opcionB);
        rBPreguntaC.setText("C. " + opcionC);
        rBPreguntaD.setText("D. " + opcionD);
    }

    public void limpiarAreaMensajes() {
        tAreaMensajes.setText("Mensaje: ");
    }

    public void limpiarOpciones() {
        rBPreguntaA.setText("");
        rBPreguntaB.setText("");
        rBPreguntaC.setText("");
        rBPreguntaD.setText("");
    }

    public String getRespuestaSeleccionada() {
        if (rBPreguntaA.isSelected()) {
            return "A";
        } else if (rBPreguntaB.isSelected()) {
            return "B";
        } else if (rBPreguntaC.isSelected()) {
            return "C";
        } else if (rBPreguntaD.isSelected()) {
            return "D";
        }
        return "";
    }

    public void setTimeRest(String tiempo) {
        tRestTime.setText("" + tiempo);
    }

}
