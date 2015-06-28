/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * @author Luca Morreale
 *
 */
public final class GUI extends JFrame implements ActionListener{
    private static final long serialVersionUID = 3194012730957326358L;

    private MainWindowMenu menuBar;
    private JTabbedPane tabbedPane;
    private MediaPane mediaTab;
    private ParsePane parseTab;

    public GUI(){
        super("Academic Exams Parser");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUI.setDefaultLookAndFeelDecorated(true);

        this.setLayout(new BorderLayout(0, 0));

        menuBar = new MainWindowMenu(this);
        this.setJMenuBar(menuBar);

        mediaTab = new MediaPane();
        parseTab = new ParsePane();

        tabbedPane = new JTabbedPane(SwingConstants.TOP);
        populateTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);

    }

    private void populateTabbedPane(){

        tabbedPane.addTab("Media Esami",mediaTab);
        tabbedPane.addTab("Parsing Risultati", parseTab);

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Unused because JMenuBar call itself methods
    }

    public void exit(){
        System.exit(0);
    }

}
