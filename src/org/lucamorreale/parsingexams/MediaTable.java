/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

/**
 * @author Luca Morreale
 *
 */
public final class MediaTable extends JTable implements MouseListener, ActionListener{
    private static final long serialVersionUID = 3012089127775134645L;

    private SQLiteManager db;
    private KeyTableModel model;
    private TablePopupMenu popupMenu;

    private static final String DB_TABLE = "media";

    public MediaTable(){
        super();

        this.model = new KeyTableModel();
        for (Column c : Column.values()) {
            model.addColumn(c);
        }
        model.setColumnsClass(Column.TYPE);
        model.addTableModelListener(this);

        popupMenu = new TablePopupMenu(this);
        this.setComponentPopupMenu(popupMenu);
        this.setInheritsPopupMenu(true);

        this.setModel(this.model);
        this.setColumnWidth();
        this.setBackground(Color.WHITE);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                db = new SQLiteManager("jdbc:sqlite:data/source.sqlite");
            }
        });

        refresh();

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
            emptyTableMessage();
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

        db.selectQuery(DB_TABLE, null, "*", "ORDER BY corso");

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

    private void emptyTableMessage() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmptyTableDialog();
            }
        });
    }

    public void tableModelChanged(TableModelEvent evt){

        String[] fields = new String[3];
        for(int i = 0; i < 3;i++) {
            fields[i] = this.getColumnName(i).toLowerCase();
        }
        int row = getSelectedModelRow();
        int id = getSelectedId();
        String[][] set = new String[3][2];
        for(int i = 0; i < 3; i++){
            set[i][0] = fields[i];
            set[i][1] = this.getValueAt(row, i).toString();
        }
        db.updateQuery(DB_TABLE, set, "id = " + id);

    }

    private int getSelectedId(){
        int row = getSelectedModelRow();
        int id = (Integer) model.getValueAt(row, KeyTableModel.COLUMN_KEY);
        return id;
    }

    private int getSelectedModelRow(){
        return this.convertRowIndexToModel(this.getSelectedRow());
    }


    @Override
    public void actionPerformed(ActionEvent evt) {

        int row = this.convertRowIndexToModel(this.getSelectedRow());
        if(row < 0){
            emptyTableMessage();
            return;
        }

        int id = (Integer) model.getValueAt(row, KeyTableModel.COLUMN_KEY);
        synchronized(this) {
            model.removeTableModelListener(this);
            model.removeRow(row);
            model.addTableModelListener(this);
            db.deleteQuery(DB_TABLE, new String[][]{
                    {"id", id+""}
            });
        }

    }

    @Override
    public void mouseReleased(MouseEvent evt) {

        if (SwingUtilities.isRightMouseButton(evt)) {
            int r = this.rowAtPoint(evt.getPoint());

            if (r >= 0 && r < this.getRowCount()) {
                this.setRowSelectionInterval(r, r);
            } else {
                this.clearSelection();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent evt) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        // TODO Auto-generated method stub
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
