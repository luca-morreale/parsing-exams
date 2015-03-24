/**
 * A class to store istances of Object, like array,
 * used to be displayed by a JTable object.
 *
 * @author  Luca Morreale
 */

package org.lucamorreale.parsingexams;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;



public class KeyTableModel extends DefaultTableModel{
    private static final long serialVersionUID = 2164354135898159481L;

    /**
     * Value that identifies the column of the keys.
     */
    public static final int COLUMN_KEY = -100;

    /**
     * Vector stores the keys.
     *      This keys wantn't be displayed and they can be any object.
     */
    protected Vector keys;

    /**
     * Array containing the class type for the model columns
     */
    protected Class[] columnTypes;

    /**
     * Creates an empty model for the table with zero rows and zero columns, and also zero keys.
     */
    public KeyTableModel() {
        this(0, 0);
    }

    /**
     * Creates a new table with the specified number of rows and columns.
     * All cells in the table are initially empty (set to null).
     *
     * @param numRows       the number of rows.
     * @param numColumns    the number of columns.
     */
    public KeyTableModel(int numRows, int numColumns) {
        Vector defaultNames = new Vector(numColumns);
        Vector data = new Vector(numRows);
        Vector keys = new Vector(numRows);

        for (int i = 0; i < numColumns; i++) {
            defaultNames.add(super.getColumnName(i));
        }

        for (int r = 0; r < numRows; r++) {
            Vector tmp = new Vector(numColumns);
            tmp.setSize(numColumns);
            data.add(tmp);
        }
        setDataVector(data, defaultNames, keys);
    }

    /**
     * Creates a new table with the specified column names and number of
     * rows.  The number of columns is determined by the number of column
     * names supplied.
     *
     * @param columnNames    column names.
     * @param numRows        number of rows.
     */
    public KeyTableModel(Vector columnNames, int numRows) {
        if (numRows < 0) {
            throw new IllegalArgumentException("numRows < 0");
        }
        Vector data = new Vector();
        int numColumns = 0;

        if (columnNames != null) {
            numColumns = columnNames.size();
        }

        while (0 < numRows--) {

            Vector rowData = new Vector();
            rowData.setSize(numColumns);
            data.add(rowData);
        }
        setDataVector(data, columnNames);
    }

    /**
     * Creates a new table with the specified column names and row count.
     *
     * @param columnNames    column names.
     * @param numRows        number of rows.
     */
    public KeyTableModel(Object[] columnNames, int numRows) {
        this(convertToVector(columnNames), numRows);
    }

    /**
     * Creates a new table with the specified data values and column names.
     *
     * @param data           data values.
     * @param columnNames    column names.
     * @param keys           keys to store and not to show.
     */
    public KeyTableModel(Vector data, Vector columnNames, Vector keys) {
        setDataVector(data, columnNames, keys);
    }

    /**
     * Creates a new table with the specified data values, column names and keys.
     *
     * @param data           data values.
     * @param columnNames    column names.
     * @param keys           keus to store.
     */
    public KeyTableModel(Object[][] data, Object[] columnNames, Object[] keys) {
        this(convertToVector(data), convertToVector(columnNames), convertToVector(keys));
    }

    /**
     * Sets the data, column identifiers and keys for the table.
     * The keys vector contains a Vector for each row in the table.
     *
     * @param data the data for the table (a vector of row vectors).
     * @param columnNames the column names.
     *
     * @throws NullPointerException if data and columnNames are null.
     */
    public void setDataVector(Vector data, Vector columnNames, Vector keys) {

        if (keys == null){
            this.keys = new Vector();
        } else {
            this.keys = keys;
        }
        super.setDataVector(data, columnNames);
    }

    /**
     * Sets the data and column identifiers for the table.
     *
     * @param data the data for the table.
     * @param columnNames the column names.
     *
     * @throws NullPointerException if data and columnNames are null.
     */
    public void setDataVector(Object[][] data, Object[] columnNames, Object[] keys) {
        setDataVector(convertToVector(data), convertToVector(columnNames), convertToVector(keys));
    }

    /**
     * Returns the vector containing the row data for the table converted in a matrix.
     *
     * @return The data in a matrix.
     */
    public Object[][] getDataMatrix() {

        if(this.dataVector == null){
            return null;
        }

        Object[][] matrix = new Object[this.getRowCount()][this.getColumnCount()];
        for(int i = 0; i < this.dataVector.size(); i++){
            matrix[i] = ((Vector) this.dataVector.get(i)).toArray();
        }

        return matrix;
    }

    /**
     * Returns the vector of the keys.
     *
     * @return Keys Vector.
     */
    public Vector getKeyVector(){
        return this.keys;
    }

    /**
     * Returns the vector keys converted in an array.
     *
     * @return Keys array.
     */
    public Object[] getKeyArray(){

        if(this.keys == null){
            return null;
        }

        return this.keys.toArray();
    }

    /**
     * Sets the number of rows in the table.  If rowCount is less
     * than the current number of rows in the table, rows are discarded.
     * If rowCount is greater than the current number of rows in
     * the table, new (empty) rows are added.
     *
     * @param rowCount the row count.
     */
    @Override
    public void setRowCount(int rowCount) {
        int existingRowCount = this.keys.size();
        if (rowCount < existingRowCount) {
            this.keys.setSize(rowCount);
        } else {
            addExtraRows(rowCount - existingRowCount);
        }

        super.setRowCount(rowCount);
    }

    /**
     * Adds some rows to @keys.
     *
     * @param rowsToAdd    number of rows to add.
     */
    private void addExtraRows(int rowsToAdd) {
        for (int i = 0; i < rowsToAdd; i++) {
            Vector tmp = new Vector();
            this.keys.add(tmp);
        }
    }

    /**
     * Adds a new row containing the specified data and key to the table and sends a
     * {@link TableModelEvent} to all registered listeners.
     *
     * @param rowData    row data (null is permitted).
     * @param key        key to add.
     */
    public void addRow(Vector rowData, Object key) {

        this.keys.add(key);
        super.addRow(rowData);
    }

    /**
     * Adds a new row containing the specified data and key to the table and sends a
     * {@link TableModelEvent} to all registered listeners.
     *
     * @param rowData    row data (null is permitted).
     */
    public void addRow(Object[] rowData, Object key) {
        addRow(convertToVector(rowData), key);
    }

    /**
     * Inserts a new row into the table in the specified position.
     *
     * @param row        row index.
     * @param rowData    row data.
     * @param key        key to insert.
     */
    public void insertRow(int row, Vector rowData, Vector key) {

        this.keys.add(row, key);
        super.insertRow(row, rowData);
    }

    /**
     * Inserts a new row into the table n the specified position.
     *
     * @param row        row index.
     * @param rowData    row data.
     * @param key        key to store.
     */
    public void insertRow(int row, Object[] rowData, Object key) {
        insertRow(row, convertToVector(rowData));
    }

    /**
     * Moves the rows from startIndex to endIndex
     * (inclusive) to the specified row.
     *
     * @param startIndex    the start row.
     * @param endIndex      the end row.
     * @param toIndex       the row to move to.
     */
    @Override
    public void moveRow(int startIndex, int endIndex, int toIndex) {

        Vector removedKey = new Vector();
        for (int i = endIndex; i >= startIndex; i--) {
            removedKey.add( this.keys.remove(i) );
        }
        for (int i = 0; i <= endIndex - startIndex; i++)  {
            keys.insertElementAt(removedKey.get(i), toIndex);
        }
        super.moveRow(startIndex, endIndex, toIndex);
    }

    /**
     * Removes a row from the table and sends a {@link TableModelEvent} to
     * all registered listeners.
     *
     * @param row    the row index.
     */
    @Override
    public void removeRow(int row) {
        keys.remove(row);
        super.removeRow(row);
    }

    /**
     * Returns the value at the specified cell in the table.
     * Using the COLUMN_KEY as column the key will be returned.
     *
     * @param row       row index.
     * @param column    column index.
     *
     * @return The value (Object, possibly null) at
     *         the specified cell in the table (or key).
     */
    @Override
    public Object getValueAt(int row, int column) {
        if(column == KeyTableModel.COLUMN_KEY){
            return keys.get(row);
        } else {
            return ((Vector) dataVector.get(row)).get(column);
        }
    }

    /**
     * Returns the key contained of the specfied row.
     *
     * @param row    row index.
     *
     * @return The key object requested.
     */
    public Object getKeyAt(int row){
        return this.keys.get(row);
    }

    /**
     * Sets the value for the specified cell in the table and sends a
     * {@link TableModelEvent} to all registered listeners.
     *
     * @param value     the value (Object, null is allowed).
     * @param row       the row index.
     * @param column    the column index.
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
        if(column == KeyTableModel.COLUMN_KEY) {
            this.keys.set(row, value);
        } else {
            super.setValueAt(value, row, column);
        }
    }

    /**
     * Sets the value for the specified key.
     *
     * @param value    value to set.
     * @param row      row index.
     */
    public void setKeyAt(Object value, int row){
        this.keys.set(row, value);
    }

    public boolean existsKey(Object key){

        if(this.keys.contains(key)) {
            return true;
        }
        return false;
    }

    /**
     * Encapsulates the object in a vector.
     *
     * @param obj    object to convert.
     *
     * @return The object converted in a vector.
     */
    public static Vector convertToVector(Object obj){

        Vector v = new Vector(1);
        v.add(obj);
        return v;
    }

    /**
     * Set the column type
     * @param type      array of Class
     */
    public void setColumnsClass(Class[] type){
        this.columnTypes = type;
    }


    /**
     *
     */
    private static final Logger LOG = Logger.getLogger(KeyTableModel.class.getName());

}