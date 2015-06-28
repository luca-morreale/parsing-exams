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

    @Override
    public void mousePressed(MouseEvent e) {
        this.setBorderPainted(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.setBorderPainted(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Unused method because there is no need to catch click event
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Unused method because there is no need to catch entering mouse event
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Unused method because there is no need to catch exiting mouse event
    }


}
