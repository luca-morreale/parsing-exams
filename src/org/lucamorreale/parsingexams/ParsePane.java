/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
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

        addBtn = new PlainButton("Aggiungi Matricola");
        openBtn = new PlainButton("Esamina File");
        clearBtn = new PlainButton("Pulisci Tabella");
        exportBtn = new PlainButton("Esporta Tabella");
        generateTopPane();


        tParse = new ParseTable();
        generateCenterPane();

    }

    private void generateTopPane(){

        JPanel topPane = new JPanel();

        try{
            addBtn.setIcon(new ImageIcon(Main.class.getResource("/resources/images/add.png")));
            openBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/open.png")));
            clearBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/delete_all.png")));
            exportBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/save.png")));
        } catch(NullPointerException exc) {
            LOG.severe(PlainButton.class +" resources not found: "+ exc.getMessage());
        }

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

    private static final Logger LOG = Logger.getLogger(ParsePane.class.getName());
}
