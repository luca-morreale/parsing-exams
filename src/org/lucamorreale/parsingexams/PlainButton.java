/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * A Simple JButton that shows border only on click
 * @author Luca Morreale
 *
 */

public class PlainButton extends JButton implements MouseListener {
    private static final long serialVersionUID = 5244190816256523933L;

    public PlainButton(){
        super();
        setPlainStyle();
    }

    public PlainButton(String title){
        super(title);
        setPlainStyle();
    }

    private void setPlainStyle(){
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(this);
    }

    public void mousePressed(MouseEvent e) {
        this.setBorderPainted(true);
    }

    public void mouseReleased(MouseEvent e) {
        this.setBorderPainted(false);
    }

    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }


}
