/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * @author Luca Morreale
 *
 */
public class DatabaseTable extends JTable implements MouseListener{
    private static final long serialVersionUID = -6102001466807223180L;

    private SQLiteManager db;
    protected KeyTableModel model;
    protected TablePopupMenu popupMenu;

    private final String DB_TABLE;
    private final List<String> Columns;

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
