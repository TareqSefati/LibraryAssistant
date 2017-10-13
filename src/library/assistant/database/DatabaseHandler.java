package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author TAREQ SEFATI
 */
public final class DatabaseHandler {
     private static DatabaseHandler handler = null;
     
     private static final String DB_URL = "jdbc:derby:database;create=true";
     private static Connection conn = null;
     private static Statement stmt = null;
     
     private DatabaseHandler(){
         createConnection();
         setupBookTable();
         setupMemberTable();
         setupIssueTable();
     }
     
     public static DatabaseHandler getInstance(){
         if(handler == null){
             handler = new DatabaseHandler();
         }
         return handler;
     }
     
     void createConnection(){
         try {
             Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
             conn = DriverManager.getConnection(DB_URL);
         } catch (Exception e) {
         }
     }
     
     void setupBookTable(){
         String TABLE_NAME = "BOOK";
         try {
             stmt = conn.createStatement();
             
             DatabaseMetaData dbm = conn.getMetaData();
             ResultSet table = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
             
             if(table.next()){
                 System.out.println("Table " + TABLE_NAME + " already exist. Good to Go.");
             }else{
                 //create a  new table
                 stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                            + " id varchar(200) primary key,\n"
                            + " title varchar(200),\n"
                            + " author varchar(200),\n"
                            + " publisher varchar(200),\n"
                            + " isAvail boolean default true"
                            + ")");
             }
                 
         } catch (Exception e) {
             System.err.println(e.getMessage() + "----In Time of setup database");
         }
     }
     
     void setupMemberTable(){
         String TABLE_NAME = "MEMBER";
         try {
             stmt = conn.createStatement();
             
             DatabaseMetaData dbm = conn.getMetaData();
             ResultSet table = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
             
             if(table.next()){
                 System.out.println("Table " + TABLE_NAME + " already exist. Good to Go.");
             }else{
                 //create a  new table
                 stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                            + " id varchar(200) primary key,\n"
                            + " name varchar(200),\n"
                            + " mobile varchar(20),\n"
                            + " email varchar(100)\n"
                            + ")");
             }
                 
         } catch (Exception e) {
             System.err.println(e.getMessage() + "----In Time of setup database");
         }
     }
     
     void setupIssueTable(){
         String TABLE_NAME = "ISSUE";
         try {
             stmt = conn.createStatement();
             
             DatabaseMetaData dbm = conn.getMetaData();
             ResultSet table = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
             
             if(table.next()){
                 System.out.println("Table " + TABLE_NAME + " already exist. Good to Go.");
             }else{
                 //create a  new table
                 stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                            + " bookID varchar(200) primary key,\n"
                            + " memberID varchar(200),\n"
                            + " issueTime timestamp default CURRENT_TIMESTAMP,\n"
                            + " renewCount integer default 0,\n"
                            + " FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                            + " FOREIGN KEY (memberID) REFERENCES MEMBER(id)\n"
                            + ")");
             }
                 
         } catch (Exception e) {
             System.err.println(e.getMessage() + "----In Time of setup database");
         }
     }
     
     public ResultSet fetchDataByExecQuery(String query){
         ResultSet result;
         try {
             stmt = conn.createStatement();
             result = stmt.executeQuery(query);
         } catch (SQLException ex) {
             System.out.println("Fetch dataerror.\n Exception at Execute query: dataHandler " + ex.getLocalizedMessage());
             return null;
         }finally{
         }
         return result;
     }
     
     public boolean execAction(String query){
         try {
             stmt = conn.createStatement();
             stmt.execute(query);
             return true;
         } catch (SQLException ex) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Unable to Perform Operation. ID may be identical.\n" + ex.getMessage());
            alert.showAndWait();
            return false;
         }finally{
         }
     }
     
     public void closeConnection(){
         try {
             conn.close();
         } catch (Exception e) {
             System.err.println("Unable to Close DB Connection. " + e.getMessage());
         }
     }
}
