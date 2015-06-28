/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Luca Morreale
 *
 */
public final class MediaTable extends DatabaseTable {
    private static final long serialVersionUID = 3012089127775134645L;

    private static final Logger LOGGER = Logger.getLogger(MediaTable.class.getName());

    private static final String DB_TABLE = "media";

    public MediaTable(){
        super(Arrays.asList("Corso", "Esito", "Crediti"), DB_TABLE);

        this.getColumnModel().getColumn(0).setPreferredWidth(200);
        this.getColumnModel().getColumn(0).setMinWidth(75);
        this.getColumnModel().getColumn(0).setMaxWidth(500);
        this.getColumnModel().getColumn(1).setMinWidth(50);
        this.getColumnModel().getColumn(1).setMaxWidth(150);
        this.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.getColumnModel().getColumn(2).setMinWidth(50);
        this.getColumnModel().getColumn(2).setMaxWidth(200);

        this.getTableHeader().setReorderingAllowed(false);
    }

    protected synchronized String getMedia(){

        if(this.getRowCount() == 0){
            emptyTableError();
            return "";
        }

        BigDecimal result = new BigDecimal("0.00");
        BigDecimal denominatore = new BigDecimal("0.00");
        BigDecimal crediti, esito;
        for(int i = 0; i < this.getRowCount(); i++){
            crediti = new BigDecimal(this.getValueAt(i, 2).toString());
            esito = new BigDecimal(this.getValueAt(i, 1).toString());

            denominatore = denominatore.add(crediti);
            result = result.add(crediti.multiply(esito));
        }

        result = result.divide(denominatore, 2, RoundingMode.HALF_UP);

        return String.valueOf(result.toString());
    }


    @Override
    protected void emptyTableError() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmptyTableDialog().setVisible(true);
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
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showSaveDialog(this);

        if (returnVal != JFileChooser.APPROVE_OPTION){
            return;
        }
        writeData(getFileName(chooser));
    }

    private void writeData(String fileName) {
        try {

            PrintStream outStream = new PrintStream(fileName);
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
            LOGGER.severe("An error occured while trying to export data " + e);
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

    @Override
    protected void addRow() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CourseDialog result = new CourseDialog(MediaTable.DB_TABLE);
                result.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        refreshTable();
                    }
                });
            }
        });
    }

}
