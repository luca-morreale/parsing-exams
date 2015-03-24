/**
 *
 */
package org.lucamorreale.parsingexams;

import javax.swing.JFrame;

/**
 * @author Luca Morreale
 *
 */
public class GUI extends JFrame{

    public GUI(){

        setTitle("Academic Exams Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);


        pack();
        setLocationRelativeTo(null);
    }

}
