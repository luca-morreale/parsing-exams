/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Luca Morreale
 *
 */
public final class ParsePane extends JPanel {
    private static final long serialVersionUID = -3974194640480696898L;


    private ParseTable tParse;
    private PlainButton addBtn;
    private PlainButton openBtn;
    private PlainButton clearBtn;
    private PlainButton exportBtn;


    public ParsePane(){
        super();


        this.setLayout(new BorderLayout());

        tParse = new ParseTable();
        generateCenterPane();

        addBtn = new PlainButton("Aggiungi Matricola");
        openBtn = new PlainButton("Esamina File");
        clearBtn = new PlainButton("Pulisci Tabella");
        exportBtn = new PlainButton("Esporta Tabella");
        generateTopPane();

    }

    private void generateTopPane(){

        JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEFT));

        try{
            addBtn.setIcon(new ImageIcon(Main.class.getResource("/resources/images/add.png")));
            openBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/open.png")));
            clearBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/delete_all.png")));
            exportBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/save.png")));
        } catch(NullPointerException exc) {
            LOG.severe(PlainButton.class +" resources not found: "+ exc.getMessage());
        }

        addBtn.addActionListener(EventHandler.create(ActionListener.class, this, "addStudent"));
        clearBtn.addActionListener(EventHandler.create(ActionListener.class, tParse, "clearResults"));
        openBtn.addActionListener(EventHandler.create(ActionListener.class, tParse, "parseFile"));
        exportBtn.addActionListener(EventHandler.create(ActionListener.class, tParse, "esporta"));

        topPane.add(addBtn);
        topPane.add(openBtn);
        topPane.add(clearBtn);
        topPane.add(exportBtn);

        this.add(topPane, BorderLayout.NORTH);
    }

    private void generateCenterPane(){

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(tParse);
        this.add(scroll, BorderLayout.CENTER);
    }

    public void addStudent(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentDialog student = new StudentDialog(ParseTable.DB_TABLE);
                student.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        tParse.refresh();
                    }
                });
            }
        });
    }


    private static final Logger LOG = Logger.getLogger(ParsePane.class.getName());
}
