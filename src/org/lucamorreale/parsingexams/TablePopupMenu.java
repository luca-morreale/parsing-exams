/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author Luca Morreale
 *
 */
public class TablePopupMenu extends JPopupMenu implements ActionListener{
    private static final long serialVersionUID = -3967665426112157339L;

    private JMenuItem deleteItm;
    private ActionListener actionToCall;

    public TablePopupMenu(ActionListener listenerToCall){
        super();

        this.actionToCall = listenerToCall;

        deleteItm = new JMenuItem("Elimina");
        deleteItm.addActionListener(this);

        this.add(deleteItm);

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        actionToCall.actionPerformed(evt);
    }




}
