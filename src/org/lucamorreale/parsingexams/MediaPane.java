/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Luca Morreale
 *
 */
public final class MediaPane extends JPanel {
    private static final long serialVersionUID = 4619821091873343199L;

    private MediaTable tMedia;

    public MediaPane(){
        super();

        this.setLayout(new BorderLayout());

        tMedia = new MediaTable();
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(tMedia);


        this.add(scroll, BorderLayout.CENTER);
    }

}
