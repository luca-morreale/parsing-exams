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
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 * @author Luca Morreale
 *
 */
public abstract class DatabaseTable extends JTable implements MouseListener, ActionListener{
    private static final long serialVersionUID = -6102001466807223180L;
    private static final Logger LOGGER = Logger.getLogger(DatabaseTable.class.getName());

    public static enum ACTION {UPDATE, DELETE, ADD, LOAD, SAVE}

    private transient SQLiteManager db;
    protected final KeyTableModel model;
    protected final TablePopupMenu popupMenu;

    private final String dbTable;
    private final transient List<String> columns;

    public DatabaseTable(List<String> columns, String dbTable){
        super();

        this.dbTable = dbTable;
        this.columns = columns;

        this.model = new KeyTableModel();
        for (String c : columns) {
            model.addColumn(c);
        }

        popupMenu = new TablePopupMenu(this);
        init();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                db = new SQLiteManager("jdbc:sqlite:data/source.sqlite");
                refreshTable();
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
        return (Integer) model.getValueAt(row, KeyTableModel.COLUMN_KEY);
    }

    protected int getSelectedModelRow(){
        return this.convertRowIndexToModel(this.getSelectedRow());
    }

    protected String[] getFields(){

        String[] fields = new String[columns.size()];
        int i = 0;
        for (String c : columns) {
            fields[i++] = c.toLowerCase();
        }
        return fields;
    }

    public synchronized void refreshTable(){
        if(!db.isConnected()) {
            return;
        }

        db.selectQuery(dbTable, null, "*");

        model.removeTableModelListener(this);

        while(db.hasNext()){
            if(!model.existsKey(db.getField("id"))){
                Object[] values = Arrays.copyOfRange(db.getRowArray(), 1, db.getColumnsCount());
                model.addRow(values, db.getField("id"));
            }
        }
        model.addTableModelListener(this);
        updateGUITable();
    }

    protected void deleteRow(){

        if(this.getRowCount() == 0){
            emptyTableError();
            return;
        }
        int id = getSelectedId();
        int row = getSelectedModelRow();

        synchronized(this){
            model.removeTableModelListener(this);

            model.removeRow(row);
            db.deleteQuery(dbTable, new String[][]{
                    {"id", id+""}
            });

            model.addTableModelListener(this);
        }
    }

    protected synchronized void updateTable(TableModelEvent evt){
        ((DefaultTableModel) evt.getSource()).removeTableModelListener(this);

        super.tableChanged(evt);
        if (evt == null || (evt.getFirstRow() == TableModelEvent.HEADER_ROW)) {
            return;
        }

        String[] fields = getFields();
        int row = getSelectedModelRow();
        int id = getSelectedId();
        String[][] set = new String[fields.length][2];
        for(int i = 0; i < fields.length; i++){
            set[i][0] = fields[i];
            set[i][1] = this.getValueAt(row, i).toString();
        }
        db.updateQuery(dbTable, set, "id = " + id);

        ((DefaultTableModel) evt.getSource()).addTableModelListener(this);
    }

    protected void writeData(String fileName) {
        try {

            PrintStream outStream = new PrintStream(fileName);
            for(int row = 0; row < this.getRowCount(); row++) {
                String data = generateDataFromRow(row);
                outStream.println(data);
            }

            outStream.close();
        } catch (IOException e) {
            LOGGER.severe("An error occured while trying to export data " + e);
        }
    }

    private String generateDataFromRow(int row) {
        String data = "";
        for(int col = 0; col < this.getColumnCount(); col++) {
            Object cell = this.getValueAt(row, col);
            if (cell != null) {
                data += cell.toString() + "\t";
            }
            data += "\t";
        }
        return data;
    }

    protected String getFileName(JFileChooser chooser){
        String file = chooser.getSelectedFile().toString();
        if( file.endsWith(".txt") || file.endsWith(".text")){
            return file;
        } else {
            return file + ".txt";
        }
    }

    abstract void emptyTableError();
    abstract void addRow();
    abstract void saveTable();

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ACTION.DELETE){
            deleteRow();
        } else if(e.getSource() == ACTION.LOAD) {
            refreshTable();
        } else if(e.getSource() == ACTION.UPDATE) {
            updateTable(new TableModelEvent(model));
        } else if(e.getSource() == ACTION.ADD) {
            addRow();
        } else if(e.getSource() == ACTION.SAVE) {
            saveTable();
        }
        updateGUITable();
    }

    @Override
    public void tableChanged(TableModelEvent evt){
        updateTable(evt);
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

    protected void updateGUITable() {
        revalidate();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        // Unused method because there is no need to catch click event
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        // Unused method because there is no need to catch entering mouse event
    }

    @Override
    public void mouseExited(MouseEvent evt) {
        // Unused method because there is no need to catch exiting mouse event
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        // Unused method because there is no need to catch mouse press event
    }




}
