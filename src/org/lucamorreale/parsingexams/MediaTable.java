/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * @author Luca Morreale
 *
 */
public final class MediaTable extends DatabaseTable {
    private static final long serialVersionUID = 3012089127775134645L;

    public static final String DB_TABLE = "media";

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


    public synchronized String getMedia(){

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

        notifyAll();
        return String.valueOf(result.toString());
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

    @Override
    protected void saveTable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);

    }


}
