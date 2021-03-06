/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private static final Logger LOGGER = Logger.getLogger(MediaPane.class.getName());

    private final MediaTable tMedia;
    private final PlainButton addBtn;
    private final PlainButton removeBtn;
    private final PlainButton exportBtn;
    private final JButton calculateBtn;
    private final JLabel mediaLabel;


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
            LOGGER.severe(PlainButton.class +" resources not found: "+ exc);
        }

        addBtn.addActionListener(this);
        removeBtn.addActionListener(this);
        exportBtn.addActionListener(this);

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

    private void calculateAverage(){
        String out = tMedia.getMedia();
        if(tMedia.getRowCount() > 0){
            mediaLabel.setText("Media Pesata: " + out);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == addBtn){
            e.setSource(MediaTable.ACTION.ADD);
        } else if(e.getSource() == exportBtn) {
            e.setSource(ParseTable.ACTION.SAVE);
        } else if(e.getSource() == removeBtn){
            e.setSource(ParseTable.ACTION.DELETE);
        } else if(e.getSource() == calculateBtn){
            calculateAverage();
            return;
        }
        tMedia.actionPerformed(e);
    }

}
