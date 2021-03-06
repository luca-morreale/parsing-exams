/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Luca Morreale
 *
 */
public final class ParsePane extends JPanel implements ActionListener{
    private static final long serialVersionUID = -3974194640480696898L;

    private static final Logger LOGGER = Logger.getLogger(ParsePane.class.getName());

    private final ParseTable tParse;
    private final PlainButton addBtn;
    private final PlainButton openBtn;
    private final PlainButton clearBtn;
    private final PlainButton exportBtn;


    public ParsePane(){
        super();
        super.setLayout(new BorderLayout());

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
            LOGGER.severe(PlainButton.class +" resources not found: "+ exc);
        }

        addBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        openBtn.addActionListener(this);
        exportBtn.addActionListener(this);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addBtn){
            e.setSource(ParseTable.ACTION.ADD);
        } else if(e.getSource() == exportBtn) {
            e.setSource(ParseTable.ACTION.SAVE);
        } else if(e.getSource() == clearBtn){
            e.setSource(ParseTable.OPERATION.CLEAR);
        } else if(e.getSource() == openBtn){
            e.setSource(ParseTable.OPERATION.OPEN);
        }

        tParse.actionPerformed(e);
    }

}
