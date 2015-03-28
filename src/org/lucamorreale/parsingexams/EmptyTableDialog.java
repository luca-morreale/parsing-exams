/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Luca Morreale
 *
 */
public final class EmptyTableDialog extends JDialog {
    private static final long serialVersionUID = 7341082940972275593L;

    private static final String msg = "<html><div style=\"text-align: center;\">"
            + "<center><h2>Errore!</h2></center><br/>"
            + "La tabella selezionata è vuota, <br/>impossibile compiere l'operazione richiesta!<br/>"
            + "</div></html>";

    public EmptyTableDialog(){
        super();

        this.setTitle("Error Empty Table");
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(0, 0));

        generateMessagePane();
        generateButtonPane();

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void generateMessagePane() {

        JPanel pane = new JPanel();
        pane.setBorder(new EmptyBorder(0, 10, 10, 10) );

        JLabel message = new JLabel(msg);
        pane.add(message);

        this.add(pane, BorderLayout.CENTER);
    }

    private void generateButtonPane(){

        JPanel pane = new JPanel();

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(EventHandler.create(ActionListener.class, this, "dispose"));
        pane.add(okButton);

        this.add(pane, BorderLayout.SOUTH);
    }

}
