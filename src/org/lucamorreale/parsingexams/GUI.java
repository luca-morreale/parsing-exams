/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * @author Luca Morreale
 *
 */
public final class GUI extends JFrame implements ActionListener{
    private static final long serialVersionUID = 3194012730957326358L;

    private MainWindowMenu menuBar;
    private MediaTable mediaTable;

    public GUI(){

        this.setTitle("Academic Exams Parser");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUI.setDefaultLookAndFeelDecorated(true);

        this.setLayout(new BorderLayout(0, 0));

        menuBar = new MainWindowMenu(this);
        setJMenuBar(menuBar);

        this.pack();
        this.setLocationRelativeTo(null);


        //JMenuBar menuBar = new JMenuBar();
        //getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent evt) {
        // TODO Auto-generated method stub

    }

    public void exit(){
        System.exit(0);
    }

}
