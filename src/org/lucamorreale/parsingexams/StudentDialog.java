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
public final class StudentDialog extends JDialog {
    private static final long serialVersionUID = -8916667523732132664L;

    private final transient SQLiteManager db;
    private final JButton saveButton;
    private final JButton cancelButton;
    private final JLetterField nomeTextField;
    private final JIntField matricolaTextField;
    private final JLetterField cognomeTextField;

    private final String dbTable;

    public StudentDialog(String db_table) {
        super();
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Aggiungi Matricola");
        this.setLayout(new BorderLayout(0, 0));

        dbTable = db_table;

        saveButton = new JButton("Salva");
        cancelButton = new JButton("Cancel");
        generateButtonPane();


        nomeTextField = new JLetterField(10);
        cognomeTextField = new JLetterField(10);
        matricolaTextField = new JIntField(10);
        generateCenterPane();


        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        db = new SQLiteManager("jdbc:sqlite:data/source.sqlite");
    }

    private void generateButtonPane(){

        saveButton.addActionListener(EventHandler.create(ActionListener.class, this, "saveStudent"));
        saveButton.setEnabled(false);
        cancelButton.addActionListener(EventHandler.create(ActionListener.class, this, "dispose"));

        JPanel panelBottom = new JPanel();
        panelBottom.add(saveButton);
        panelBottom.add(cancelButton);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);

    }

    private void generateCenterPane(){
        JPanel centerPane = new JPanel();


        FormLayout formLayout =
                new FormLayout("80px, 120px", "50px, 50px, 50px");
        centerPane.setLayout(formLayout);
        CellConstraints c = new CellConstraints();

        JLabel lblNome = new JLabel("Nome");
        lblNome.setLabelFor(nomeTextField);
        centerPane.add(lblNome, c.xy(1, 1, CellConstraints.LEFT, CellConstraints.CENTER));
        centerPane.add(nomeTextField, c.xy(2, 1, CellConstraints.RIGHT, CellConstraints.CENTER));


        JLabel lblCognome = new JLabel("Cognome");
        lblCognome.setLabelFor(cognomeTextField);
        centerPane.add(lblCognome, c.xy(1, 2, CellConstraints.LEFT, CellConstraints.CENTER));
        centerPane.add(cognomeTextField, c.xy(2, 2, CellConstraints.RIGHT, CellConstraints.CENTER));

        JLabel lblMatricola = new JLabel("Matricola");
        lblMatricola.setLabelFor(lblMatricola);
        centerPane.add(lblMatricola, c.xy(1, 3, CellConstraints.LEFT, CellConstraints.CENTER));
        centerPane.add(matricolaTextField, c.xy(2, 3, CellConstraints.RIGHT, CellConstraints.CENTER));



        nomeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkField();
            }
        });
        cognomeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkField();
            }
        });
        matricolaTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkField();
            }
        });

        this.add(centerPane, BorderLayout.CENTER);

    }

    private void checkField(){

        if (nomeTextField.getText().trim().length() > 0 && cognomeTextField.getText().trim().length() > 0
                && matricolaTextField.getText().trim().length() > 0){
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    public void saveStudent() {
        db.insertQuery(dbTable, new String[][]{
                        {"matricola", matricolaTextField.getText()},
                        {"nome", nomeTextField.getText()},
                        {"cognome", cognomeTextField.getText()},
                });
        dispose();
    }


}
