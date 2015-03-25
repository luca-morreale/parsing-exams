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
public final class MediaPane extends JPanel {
    private static final long serialVersionUID = 4619821091873343199L;

    private MediaTable tMedia;
    private PlainButton addBtn;
    private PlainButton removeBtn;
    private PlainButton exportBtn;



    public MediaPane(){
        super();

        this.setLayout(new BorderLayout());


        addBtn = new PlainButton("Aggiungi Corso");
        removeBtn = new PlainButton("Elimina Corso");
        exportBtn = new PlainButton("Esporta Tabella");
        generateTopPane();


        tMedia = new MediaTable();
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(tMedia);


        this.add(scroll, BorderLayout.CENTER);
    }

    private void generateTopPane(){

        JPanel topPane = new JPanel();

        try{
            addBtn.setIcon(new ImageIcon(Main.class.getResource("/resources/images/add.png")));
            removeBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/delete_all.png")));
            exportBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/save.png")));
        } catch(NullPointerException exc) {
            LOG.severe(PlainButton.class +" resources not found: "+ exc.getMessage());
        }

        topPane.add(addBtn);
        topPane.add(removeBtn);
        topPane.add(exportBtn);

        this.add(topPane, BorderLayout.NORTH);
    }

    private static final Logger LOG = Logger.getLogger(MediaPane.class.getName());

}
