/**
 *
 */
package org.lucamorreale.parsingexams;

import java.awt.Color;

import javax.swing.JTable;

/**
 * @author Luca Morreale
 *
 */
public final class ParseTable extends JTable{
    private static final long serialVersionUID = -9029444808468680306L;

    private KeyTableModel model;

    public ParseTable(){
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
    }

    public synchronized void clearResults(){

        model.removeTableModelListener(this);
        for(int i=0; i<model.getRowCount(); i++){
            this.setValueAt("", i, 3);
        }

        model.addTableModelListener(this);
    }




    /**
     * Enum containing the base information of the table columns
     * @author Luca Morreale
     *
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
