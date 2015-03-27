/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Luca Morreale
 *
 */
public final class MediaPane extends JPanel implements ActionListener{
    private static final long serialVersionUID = 4619821091873343199L;

    private MediaTable tMedia;
    private PlainButton addBtn;
    private PlainButton removeBtn;
    private PlainButton exportBtn;
    private JButton calculateBtn;
    private JLabel mediaLabel;



    public MediaPane(){
        super();

        this.setLayout(new BorderLayout());

        tMedia = new MediaTable();
        generateCenterPane();

        addBtn = new PlainButton("Aggiungi Corso");
        removeBtn = new PlainButton("Elimina Corso");
        exportBtn = new PlainButton("Esporta Tabella");
        generateTopPane();


        calculateBtn = new JButton("Calcola Media");
        mediaLabel = new JLabel("");
        generateBottomPane();


    }

    private void generateTopPane(){

        JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEFT));

        try{
            addBtn.setIcon(new ImageIcon(Main.class.getResource("/resources/images/add.png")));
            removeBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/delete.png")));
            exportBtn.setIcon(new ImageIcon(GUI.class.getResource("/resources/images/save.png")));
        } catch(NullPointerException exc) {
            LOG.severe(PlainButton.class +" resources not found: "+ exc.getMessage());
        }

        addBtn.addActionListener(EventHandler.create(ActionListener.class, tMedia, "addCourse"));

        topPane.add(addBtn);
        topPane.add(removeBtn);
        topPane.add(exportBtn);

        this.add(topPane, BorderLayout.NORTH);
    }

    private void generateCenterPane(){

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(tMedia);
        this.add(scroll, BorderLayout.CENTER);
    }

    private void generateBottomPane(){

        JPanel panel = new JPanel(new BorderLayout(0, 0));

        calculateBtn.addActionListener(this);
        calculateBtn.setBackground(Color.ORANGE);
        panel.add(mediaLabel, BorderLayout.WEST);
        panel.add(calculateBtn, BorderLayout.EAST);

        this.add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if(tMedia.getRowCount() == 0){
            mediaLabel.setText("Media Pesata: " + tMedia.getMedia());
            return;
        } else {
            mediaLabel.setText("Media Pesata: " + tMedia.getMedia());
        }

    }


    private static final Logger LOG = Logger.getLogger(MediaPane.class.getName());
}
