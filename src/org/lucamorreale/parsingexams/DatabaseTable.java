/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JTable;

/**
 * @author Luca Morreale
 *
 */
public class DatabaseTable extends JTable {
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
        this.setComponentPopupMenu(popupMenu);
        this.setInheritsPopupMenu(true);

        this.setModel(this.model);
        this.setBackground(Color.WHITE);
    }

}
