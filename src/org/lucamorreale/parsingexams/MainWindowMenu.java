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

    public MainWindowMenu(ActionListener listenerToCall){
        super();
        buildFile(listenerToCall);
    }

    private void buildFile(ActionListener listenerToCall){

        JMenu mFile = new JMenu("File");
        JMenuItem itemExit = new JMenuItem("Exit");

        itemExit.addActionListener(EventHandler.create(ActionListener.class, listenerToCall, "exit"));
        mFile.addSeparator();
        mFile.add(itemExit);

        this.add(mFile);
    }


}
