/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.Color;
import java.awt.EventQueue;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;

/**
 * @author Luca Morreale
 *
 */
public final class MediaTable extends JTable implements TableModelListener{
    private static final long serialVersionUID = 3012089127775134645L;

    private SQLiteManager db;
    private KeyTableModel model;

    public MediaTable(){
        super();

        this.model = new KeyTableModel();
        for (Column c : Column.values()) {
            model.addColumn(c);
        }
        model.setColumnsClass(Column.TYPE);
        model.addTableModelListener(this);

        this.setModel(this.model);
        this.setColumnWidth();
        this.setBackground(Color.WHITE);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                db = new SQLiteManager("jdbc:sqlite:data/source.sqlite");
            }
        });

    }

    private void setColumnWidth(){

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


    public synchronized void refresh(){
        if(!db.isConnected()) {
            return;
        }

        db.selectQuery("media", null, "*", "ORDER BY corso");

        model.removeTableModelListener(this);

        String fields[] = new String[3];
        int i = 0;
        for (Column c : Column.values()) {
            fields[i++] = c.toString().toLowerCase();
        }

        Object[] values = new Object[fields.length];
        while(db.hasNext()){
            if(!model.existsKey(db.getField("id"))){
                for(i = 0;i < fields.length; i++) {
                    values[i] = db.getField(fields[i]);
                }
                model.addRow(values, db.getField("id"));
            }
        }

        model.addTableModelListener(this);
        notifyAll();
    }

    public void addCourse(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CourseDialog result = new CourseDialog();
                result.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        refresh();
                    }

                });
            }
        });
    }



    /**
     * Enum containing the base information of the table columns
     * @author Luca Morreale
     *
     */
    private enum Column{
        CORSO("Corso"), ESITO("Esito"), CREDITI("Crediti");

        @SuppressWarnings("rawtypes")
        public static Class[] TYPE = new Class[] { String.class, Short.class, Short.class };

        String msg;
        Column(String msg){
            this.msg = msg;
        }

        @Override
        public String toString(){
            return msg;
        }
    }

}
