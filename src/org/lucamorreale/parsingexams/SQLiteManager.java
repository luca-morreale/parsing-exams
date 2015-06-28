/**
 *
 */
package org.lucamorreale.parsingexams;

/**
 * @author Luca Morreale
 *
 */

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;


public final class SQLiteManager {

    private static final Logger LOGGER = Logger.getLogger(SQLiteManager.class.getName());

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public SQLiteManager(String pathDb){

        try{
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(pathDb);
            stmt = conn.createStatement();

        } catch(ClassNotFoundException e){
            LOGGER.severe("Class Not Found " + e);
        } catch(SQLException e) {
            LOGGER.severe("A strange error occured "+ e);
        }
    }

    public boolean isConnected(){
        return conn != null;
    }


    /**
     *
     * @param table
     * @param set
     * @param condition
     * @return
     */
    public boolean updateQuery(String table, String[][] set, String condition){

        String query = "UPDATE " + table + " SET ";
        query += buildString(set, ",");
        query += " WHERE " + condition;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, set, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing an update query "+ e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param table
     * @param set
     * @param condition
     * @return
     */
    public boolean updateQuery(String table, String[][] set, String[][] condition){

        String query = "UPDATE " + table + " SET ";
        query += buildString(set, ",");
        query += " WHERE " + buildString(condition, "AND");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, set, 1);
            setField(preparedStatement, condition, set.length+1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing an update query " + e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param table
     * @return
     */
    public boolean selectQuery(String table){
        return query("SELECT * FROM "+table, false);
    }

    /**
     *
     * @param table
     * @param condition
     * @return
     */
    public boolean selectQuery(String table, String[][] condition){

        String query = "SELECT * FROM "+table+" ";
        query += buildString(condition, "AND");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, condition, 1);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing a select query " + e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param table
     * @param condition
     * @param fields
     * @return
     */
    public boolean selectQuery(String table, String[][] condition, String fields){

        String query = "SELECT "+fields+" FROM "+table+" ";
        query += buildString(condition, "AND");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, condition, 1);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing a select query " + e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param table
     * @param condition
     * @param fields
     * @return
     */
    public boolean selectQuery(String table, String[][] condition, String[] fields){

        String query = "SELECT ";
        query += buildString(fields, ",");
        query += " FROM "+table+" ";
        query += buildString(condition, "AND");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, condition, 1);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing a select query " + e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param table
     * @param condition
     * @param fields
     * @param after_where
     * @return
     */
    public boolean selectQuery(String table, String[][] condition, String[] fields, String after_where){

        String query = "SELECT ";
        query += buildString(fields, ",");
        return buildSelectBodyQuery(table, condition, after_where, query);

    }

    /**
     *
     * @param table
     * @param condition
     * @param fields
     * @param after_where
     * @return
     */
    public boolean selectQuery(String table, String[][] condition, String field, String after_where){

        String query = "SELECT "+field;
        return buildSelectBodyQuery(table, condition, after_where, query);

    }

    private boolean buildSelectBodyQuery(String table, String[][] condition, String after_where, String queryHeader) {
        String query = queryHeader + " FROM " + table + " ";
        query += buildString(condition, "AND");
        query += after_where;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, condition, 1);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing a select query " + e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param table
     * @param values
     * @return
     */
    public boolean insertQuery(String table, String[][] values){

        String query = "INSERT INTO "+ table +"(";

        query += buildString(values,",").replaceAll("[=?]", "");
        query += ") VALUES (";
        for(int i=0; i<values.length; i++){
            query += "?, ";
        }
        query = query.substring(0, query.length()-",".length()-1);
        query += ")";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, values, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing an insert query " + e);
            return false;
        }

        return true;
    }

    /**
     *
     * @param table
     * @param where
     */
    public boolean deleteQuery(String table, String[][] where){

        String query = "DELETE FROM "+ table;
        if(where.length >0 && where[0].length >0){
            query += " WHERE " + buildString(where, ",");
        }

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setField(preparedStatement, where, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing a delete query " + e);
            return false;
        }

        return true;
    }

    /**
     *
     * @param query
     * @param securized
     * @return
     */
    public boolean query(String query, boolean securized){

        try {
            if(!securized){
                PreparedStatement prepStmt = conn.prepareStatement(query);
                rs = prepStmt.executeQuery();
            } else {
                rs = stmt.executeQuery(query);
            }

        } catch (SQLException e) {
            LOGGER.severe("An error occured while performing a query " + e);
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean hasNext(){
        try {
            if(rs == null){
                return false;
            } else {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.severe("There isn't any other elemnts left" + e);
            return false;
        }
    }

    public int getColumnsCount(){
        try {
            return rs.getMetaData().getColumnCount();
        } catch (SQLException e) {
            LOGGER.severe("Impossible to count remaining elements " + e);
            return -1;
        }
    }

    /**
     * Iterate over all column selected and return an array of Object
     * containing the data of the current row.
     * @return
     */
    public Object[] getRowArray(){

        int colNumber = getColumnsCount();
        Object[] row = new Object[colNumber];
        for(int i=0; i<colNumber; i++){
            row[i] = getField(i);
        }
        return row;

    }

    /**
     *
     * @param indexField
     * @return
     */
    public BigDecimal getBigDecimal(int indexField){
        try {
            return rs.getBigDecimal(indexField);
        } catch (SQLException e) {
            LOGGER.severe("Impossible to get a BigDecimal for the requested field" + e);
            return null;
        }
    }
    /**
     *
     * @param indexField
     * @return
     */
    public Array getArray(int indexField){
        try {
            return rs.getArray(indexField);
        } catch (SQLException e) {
            LOGGER.severe("Impossible to get an array for the requested field" + e);
            return null;
        }
    }
    /**
     *
     * @param indexField
     * @return
     */
    public float getFloat(int indexField){
        try {
            return rs.getFloat(indexField);
        } catch (SQLException e) {
            LOGGER.severe("Impossible to get a float for the requested field" + e);
            return 0;
        }
    }
    /**
     *
     * @param indexField
     * @return
     */
    public byte getByte(int indexField){
        try {
            return rs.getByte(indexField);
        } catch (SQLException e) {
            LOGGER.severe("Impossible to get a byte for the requested field" + e);
            return 0;
        }
    }
    /**
     *
     * @param indexField
     * @return
     */
    public byte[] getBytes(int indexField){
        try {
            return rs.getBytes(indexField);
        } catch (SQLException e) {
            LOGGER.severe("Impossible to get an array for the requested field" + e);
            return new byte[0];
        }
    }
    /**
     *
     * @param indexField
     * @return
     */
    public Object getField(int indexField){

        int index  = indexField + 1;

        try{
            ResultSetMetaData rm = rs.getMetaData();
            String type = rm.getColumnTypeName(index);

            return factoryTypeData(index, type);
        }catch(SQLException e){
            LOGGER.severe("Impossible to get the requested field" + e);
        }
        return null;
    }

    private Object factoryTypeData(int indexField, String type) throws SQLException {
        switch(type.toLowerCase()){
            case "integer":
                return rs.getInt(indexField);
            case "text":
                return rs.getString(indexField);
            case "string":
                return rs.getString(indexField);
            case "real":
                return rs.getDouble(indexField);
            case "date":
                return rs.getDate(indexField);
            case "boolean":
                return rs.getBoolean(indexField);
            case "blob":
                return rs.getBlob(indexField);
            default:
                throw new SQLException("The type is not recognizable.");
        }
    }

    /**
     *
     * @param field
     * @return
     */
    public Object getField(String field){

        try{
            int indexField = rs.findColumn(field);
            return getField(indexField-1);
        }catch(SQLException e){
            LOGGER.severe("Impossible to get the requested field" + e);
        }
        return null;
    }


    /**
     *
     * @param matrix
     * @param sep
     * @return
     */
    private static String buildString(String[][] matrix, String sep){

        String s = "";
        if(matrix==null){
            return s;
        } else if(matrix.length==0 || matrix[0].length==0){
            return s;
        }

        for(int i = 0; i<matrix.length; i++){
           s += matrix[i][0] + "=" + "?" + sep + " ";
        }

        s = s.substring(0, s.length()-sep.length()-1);
        return s;
    }

    /**
     *
     * @param array
     * @param sep
     * @return
     */
    private static String buildString(String[] array, String sep){

        String s = "";
        if(array==null){
            return s;
        } else if(array.length==0){
            return s;
        }

        for(int i = 0; i<array.length; i++){
            s += array[i]+sep+" ";
        }

        s = s.substring(0, s.length()-sep.length()-1);
        return s;
    }

    /**
     *
     * @param stm
     * @param set
     * @param startIndex
     */
    private static void setField(PreparedStatement stm, String[][] set, int startIndex){

        if(set == null){
            return;
        } else if(set.length==0 || set[0].length==0){
            return;
        }

        for(int i = 0; i<set.length; i++){
            try {
                stm.setString(startIndex+i, set[i][1]);
            } catch (SQLException e) {
                LOGGER.severe("Impossible to set the requested field" + e);
            }
        }

    }

}