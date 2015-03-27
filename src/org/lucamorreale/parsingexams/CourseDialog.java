/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Luca Morreale
 *
 */
public class CourseDialog extends JDialog {
    private static final long serialVersionUID = -8178179660938779222L;

    private JButton saveButton;
    private JIntField resultIntField;
    private JIntField creditIntField;
    private JLetterField courseLetterField;

    private SQLiteManager db;

    public CourseDialog(){

        this.setLayout(new BorderLayout(0, 0));

        saveButton = new JButton("Aggiungi Esito");
        generateButtonPane();

        courseLetterField = new JLetterField(10);
        resultIntField = new JIntField(10);
        creditIntField = new JIntField(10);
        generateFormPane();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        db = new SQLiteManager("jdbc:sqlite:data/source.sqlite");

    }

    private void generateButtonPane(){

        JPanel bottomPanel = new JPanel();

        saveButton.setEnabled(false);
        saveButton.addActionListener(EventHandler.create(ActionListener.class, this, "saveResult"));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(EventHandler.create(ActionListener.class, this, "dispose"));

        bottomPanel.add(saveButton);
        bottomPanel.add(cancelButton);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void generateFormPane(){

        JPanel centerPane = new JPanel();

        FormLayout formLayout =
                new FormLayout("80px, 120px", "50px, 50px, 50px");
        centerPane.setLayout(formLayout);
        CellConstraints c = new CellConstraints();

        JLabel lblCorso = new JLabel("Corso");
        lblCorso.setLabelFor(courseLetterField);
        centerPane.add(lblCorso, c.xy(1, 1, CellConstraints.LEFT, CellConstraints.CENTER));
        centerPane.add(courseLetterField, c.xy(2, 1, CellConstraints.RIGHT, CellConstraints.CENTER));

        JLabel lblEsito = new JLabel("Esito");
        lblEsito.setLabelFor(resultIntField);
        centerPane.add(lblEsito, c.xy(1, 2, CellConstraints.LEFT, CellConstraints.CENTER));
        centerPane.add(resultIntField, c.xy(2, 2, CellConstraints.RIGHT, CellConstraints.CENTER));

        JLabel lblCrediti = new JLabel("Crediti");
        lblCrediti.setLabelFor(creditIntField);
        centerPane.add(lblCrediti, c.xy(1, 3, CellConstraints.LEFT, CellConstraints.CENTER));
        centerPane.add(creditIntField, c.xy(2, 3, CellConstraints.RIGHT, CellConstraints.CENTER));


        courseLetterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkField();
            }
        });

        resultIntField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkField();
            }
        });

        creditIntField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkField();
            }
        });

        this.add(centerPane, BorderLayout.CENTER);

    }

    private void checkField() {
        if(courseLetterField.getText().trim().length()>0 && isResultValid() && creditIntField.getText().trim().length()>0){
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    private boolean isResultValid(){

        String resultString = resultIntField.getText().trim();
        if(resultString.length() > 0){
            int result = Integer.parseInt(resultString);
            if(result >= 18 && result <= 30){
                return true;
            }
        }
        return true;
    }

    public void saveResult(){
        db.insertQuery("esami", new String[][]{
                {"corso", courseLetterField.getText()},
                {"esito", resultIntField.getText()},
                {"crediti", creditIntField.getText()},
        });

        dispose();
    }

}
