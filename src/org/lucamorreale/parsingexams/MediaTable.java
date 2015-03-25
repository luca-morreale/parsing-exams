/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;

/**
 * @author Luca Morreale
 *
 */
public final class MediaTable extends JTable implements TableModelListener{
    private static final long serialVersionUID = 3012089127775134645L;

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
    }

    public int getMedia(){

        return 0;
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
