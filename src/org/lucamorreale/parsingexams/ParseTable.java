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

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author Luca Morreale
 *
 */
public final class ParseTable extends JTable implements MouseListener, ActionListener, TableModelListener{
    private static final long serialVersionUID = -9029444808468680306L;

    private SQLiteManager db;
    private KeyTableModel model;
    private TablePopupMenu popupMenu;

    public static final String DB_TABLE = "parse";

    public ParseTable(){
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
                refresh();
            }
        });



    }

    private void setColumnWidth(){

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

    public synchronized void refresh(){
        if(!db.isConnected()) {
            return;
        }

        db.selectQuery(DB_TABLE, null, "*", "ORDER BY nome");

        model.removeTableModelListener(this);

        String fields[] = new String[4];
        int i = 0;
        for (Column c : Column.values()) {
            fields[i++] = c.toString().toLowerCase();
        }

        Object[] values = new Object[fields.length-1];
        while(db.hasNext()){
            if(!model.existsKey(db.getField("id"))){
                for(i = 0;i < fields.length-1; i++) {
                    values[i] = db.getField(fields[i]);
                }
                model.addRow(values, db.getField("id"));
            }
        }

        model.addTableModelListener(this);
        notifyAll();
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


    public void parseFile(){

        if(model.getRowCount() == 0) {
            emptyTableMessage();
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

    private void emptyTableMessage() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EmptyTableDialog();
            }
        });
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
    public void actionPerformed(ActionEvent e) {

        int id = getSelectedId();
        int row = getSelectedModelRow();

        model.removeTableModelListener(this);
        model.removeRow(row);
        db.deleteQuery(DB_TABLE, new String[][]{
                {"id", id+""}
        });

        model.addTableModelListener(this);
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
     */
    private enum Column{
        MATRICOLA("Matricola"), NOME("Nome"), COGNOME("Cognome"), ESITO("Esito");

        @SuppressWarnings("rawtypes")
        public static Class[] TYPE = new Class[] { String.class, String.class, String.class, String.class };

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
