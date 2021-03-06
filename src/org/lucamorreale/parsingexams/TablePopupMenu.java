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

    private final transient JMenuItem deleteItm;
    private final transient ActionListener actionToCall;

    public TablePopupMenu(ActionListener listenerToCall){
        super();

        this.actionToCall = listenerToCall;

        deleteItm = new JMenuItem("Elimina");
        deleteItm.addActionListener(this);

        this.add(deleteItm);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == deleteItm){
            e.setSource(DatabaseTable.ACTION.DELETE);
        }
        actionToCall.actionPerformed(e);
    }




}
