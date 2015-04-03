/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Luca Morreale
 *
 */
public final class ParseTable extends DatabaseTable {
    private static final long serialVersionUID = -9029444808468680306L;


    public static final String DB_TABLE = "parse";

    public ParseTable(){
        super(Arrays.asList("Matricola", "Nome", "Cognome", "Esito"), DB_TABLE);

        this.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.getColumnModel().getColumn(0).setMinWidth(50);
        this.getColumnModel().getColumn(0).setMaxWidth(200);
        this.getColumnModel().getColumn(1).setPreferredWidth(119);
        this.getColumnModel().getColumn(1).setMinWidth(100);
        this.getColumnModel().getColumn(1).setMaxWidth(500);
        this.getColumnModel().getColumn(2).setPreferredWidth(178);
        this.getColumnModel().getColumn(3).setPreferredWidth(232);
        this.getColumnModel().getColumn(3).setMinWidth(50);
        this.getColumnModel().getColumn(3).setMaxWidth(500);

        this.getTableHeader().setReorderingAllowed(false);


    }

    public synchronized void clearResults(){

        model.removeTableModelListener(this);
        for(int i=0; i<model.getRowCount(); i++){
            this.setValueAt("", i, 3);
        }

        model.addTableModelListener(this);

        notifyAll();
    }

    public void parseFile(){

        if(model.getRowCount() == 0) {
            emptyTableError();
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new CustomFileFilter());
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String[] pattern = new String[model.getRowCount()];
        for(int i=0; i<model.getRowCount(); i++) {
            pattern[i] = (String) model.getValueAt(i, 0);
        }
        FileParser parser = new FileParser(fileChooser.getSelectedFile(), pattern);
        String[] results = parser.parseFile();


        synchronized(this) {
            model.removeTableModelListener(this);
            for(int i=0; i<model.getRowCount(); i++){
                model.setValueAt(results[i] ,i, 3);
            }
            model.addTableModelListener(this);
        }
    }

    @Override
    protected void emptyTableError() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmptyTableDialog();
            }
        });

    }

    @Override
    protected void addRow() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentDialog student = new StudentDialog(ParseTable.DB_TABLE);
                student.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        refershTable();
                    }
                });
            }
        });
    }

    @Override
    protected void saveTable() {

        if(model.getRowCount() == 0) {
            emptyTableError();
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
        int returnVal = chooser.showSaveDialog(this);

        if (returnVal != JFileChooser.APPROVE_OPTION){
            return;
        }
        synchronized(this){
            try {

                PrintStream outStream = new PrintStream(getFileName(chooser));
                for(int row = 0; row < this.getRowCount(); row++) {
                    String data = "";
                    for(int col = 0; col < this.getColumnCount(); col++) {
                        Object cell = this.getValueAt(row, col);
                        if (cell != null) {
                            data += cell.toString() + "\t";
                        }
                        data += "\t";
                    }
                    outStream.println(data);
                }

                outStream.close();
            } catch (IOException e) {
                LOG.severe(e.getMessage() + " esporta()");
            }
        }

    }

    private String getFileName(JFileChooser chooser){
        String file = chooser.getSelectedFile().toString();
        if( file.endsWith(".txt") || file.endsWith(".text")){
            return file;
        } else {
            return file + ".txt";
        }
    }

    private static final Logger LOG = Logger.getLogger(ParseTable.class.getName());

}
