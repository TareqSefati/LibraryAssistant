
package library.asistant.ui.addBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;


public class AddBookController implements Initializable {

    @FXML
    private JFXTextField bookTitle;
    @FXML
    private JFXTextField bookID;
    @FXML
    private JFXTextField bookAuthor;
    @FXML
    private JFXTextField bookPublisher;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;
    @FXML
    private AnchorPane rootPane;
    
    DatabaseHandler databaseHandler;

   @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        checkData();
    }    

    @FXML
    private void addBook(ActionEvent event) {
        String id = bookID.getText();
        String title = bookTitle.getText();
        String author = bookAuthor.getText();
        String publisher = bookPublisher.getText();
        
        if(id.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill all of the fields.");
            alert.showAndWait();
            return;
        }
        
        String query = "INSERT INTO BOOK VALUES (" +
                    "'" + id + "'," +
                    "'" + title + "'," +
                    "'" + author + "'," +
                    "'" + publisher + "'," +
                    "" + true + "" +
                ")";
        //System.out.println(query);
            
        if(databaseHandler.execAction(query)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }else{
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText("Failed");
//            alert.showAndWait();
        }
        
        
        
    }

    @FXML
    private void cancelBook(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
        String query = "SELECT title FROM BOOK";
        ResultSet results = databaseHandler.fetchDataByExecQuery(query);
        
        try {
            while(results.next()){
                String title = results.getString("title");
                System.out.println(title);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
