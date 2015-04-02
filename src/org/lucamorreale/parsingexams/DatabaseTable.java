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
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * @author Luca Morreale
 *
 */
public abstract class DatabaseTable extends JTable implements MouseListener, ActionListener{
    private static final long serialVersionUID = -6102001466807223180L;

    private SQLiteManager db;
    protected KeyTableModel model;
    protected TablePopupMenu popupMenu;

    private final String DB_TABLE;
    private final List<String> Columns;

    public static enum ACTION {UPDATE, DELETE, ADD, LOAD};

    public DatabaseTable(List<String> columns, String db_table){
        super();

        DB_TABLE = db_table;
        Columns = columns;

        this.model = new KeyTableModel();
        for (String c : Columns) {
            model.addColumn(c);
        }

        popupMenu = new TablePopupMenu(null);
        init();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                db = new SQLiteManager("jdbc:sqlite:data/source.sqlite");
            }
        });

    }

    private void init(){

        model.addTableModelListener(this);
        this.addMouseListener(this);

        this.setComponentPopupMenu(popupMenu);
        this.setInheritsPopupMenu(true);

        this.setModel(this.model);
        this.setBackground(Color.WHITE);
    }

    protected int getSelectedId(){
        int row = getSelectedModelRow();
        int id = (Integer) model.getValueAt(row, KeyTableModel.COLUMN_KEY);
        return id;
    }

    protected int getSelectedModelRow(){
        return this.convertRowIndexToModel(this.getSelectedRow());
    }

    protected void updateTable(String[] fields){

        Object[] values = new Object[fields.length];
        for(int i = 0;i < values.length; i++) {
            values[i] = db.getField(fields[i]);
        }

        model.addRow(values, db.getField("id"));
    }

    protected String[] getUpdateFields(){

        String fields[] = new String[4];
        int i = 0;
        for (String c : Columns) {
            fields[i++] = c.toLowerCase();
        }
        return fields;
    }

    public synchronized void refershTable(){
        if(!db.isConnected()) {
            return;
        }

        db.selectQuery(DB_TABLE, null, "*", "ORDER BY nome");

        model.removeTableModelListener(this);

        String fields[] = getUpdateFields();
        while(db.hasNext()){
            if(!model.existsKey(db.getField("id"))){
                updateTable(fields);
            }
        }
        model.addTableModelListener(this);
        notifyAll();
    }



    protected void deleteRow(){

        if(this.getRowCount() == 0){
            emptyTableError();
        }
        int id = getSelectedId();
        int row = getSelectedModelRow();

        synchronized(this){
            model.removeTableModelListener(this);

            model.removeRow(row);
            db.deleteQuery(DB_TABLE, new String[][]{
                    {"id", id+""}
            });

            model.addTableModelListener(this);
        }
    }

    abstract void emptyTableError();



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ACTION.DELETE){
            deleteRow();
        } else if(e.getSource() == ACTION.LOAD) {
            refershTable();
        } else if(e.getSource() == ACTION.UPDATE) {

        } else if(e.getSource() == ACTION.ADD) {

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
    public void mouseClicked(MouseEvent evt) { } // TODO Auto-generated method stub

    @Override
    public void mouseEntered(MouseEvent evt) { } // TODO Auto-generated method stub

    @Override
    public void mouseExited(MouseEvent evt) { } // TODO Auto-generated method stub

    @Override
    public void mousePressed(MouseEvent evt) { } // TODO Auto-generated method stub




}
