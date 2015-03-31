/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.event.ActionListener;
import java.beans.EventHandler;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Luca Morreale
 *
 */
public final class MainWindowMenu extends JMenuBar{
    private static final long serialVersionUID = -4760379938611375664L;

    private ActionListener actionToCall;

    public MainWindowMenu(ActionListener listenerToCall){
        super();
        this.actionToCall = listenerToCall;

        buildFile();
    }

    private void buildFile(){

        JMenu mFile = new JMenu("File");
        JMenuItem itemExit = new JMenuItem("Exit");

        itemExit.addActionListener(EventHandler.create(ActionListener.class, this.actionToCall, "exit"));    // at row
        mFile.addSeparator();
        mFile.add(itemExit);

        this.add(mFile);

    }


}
