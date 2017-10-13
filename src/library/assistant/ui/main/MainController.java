package library.assistant.ui.main;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author TAREQ SEFATI
 */
public class MainController implements Initializable {

    DatabaseHandler handler;
    Boolean bookIsAvailable = false;
    Boolean readyForSubmission = false;
    
    @FXML
    private HBox bookInfo;
    @FXML
    private HBox memberInfo;
    @FXML
    private Text bookName;
    @FXML
    private Text authorName;
    @FXML
    private Text memberName;
    @FXML
    private Text contact;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookStatus;
    @FXML
    private TextField memberIDInput;
    @FXML
    private JFXTextField bookIDForDetails;
    @FXML
    private ListView<String> issueDataList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(bookInfo, 1);
        JFXDepthManager.setDepth(memberInfo, 1);
        handler = DatabaseHandler.getInstance();
    }    

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/library/asistant/ui/addMember/AddMember.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/library/asistant/ui/addBook/AddBook.fxml", "Add New Book");
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        loadWindow("/library/assistant/ui/listOfMembers/MemberList.fxml", "Member List");
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        loadWindow("/library/assistant/ui/listOfBooks/BookList.fxml", "Book List");
    }
    
    void loadWindow(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookCache();
        bookIsAvailable = false;
        String bookID = bookIDInput.getText();
        String query = "SELECT * FROM BOOK WHERE id = '" + bookID + "'";
        ResultSet result = handler.fetchDataByExecQuery(query);
        
        Boolean flag = false;
        try {
            while(result.next()){
                bookName.setText(result.getString("title"));
                authorName.setText(result.getString("author"));
                bookStatus.setText(result.getBoolean("isAvail")? "Available" : "Not Available");
                bookIsAvailable = result.getBoolean("isAvail");
                flag = true;
            }
            
            if(!flag){
                bookName.setText("No such book is available");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMemberCache();
        String memberID = memberIDInput.getText();
        String query = "SELECT * FROM MEMBER WHERE id = '" + memberID + "'";
        ResultSet result = handler.fetchDataByExecQuery(query);
        
        Boolean flag = false;
        try {
            while(result.next()){
                memberName.setText(result.getString("name"));
                contact.setText(result.getString("mobile"));
                flag = true;
            }
            
            if(!flag){
                memberName.setText("No such member is available");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void clearBookCache(){
        bookName.setText("");
        authorName.setText("");
        bookStatus.setText("");
    }
    
    void clearMemberCache(){
        memberName.setText("");
        contact.setText("");
    }

    @FXML
    private void issueBookOperation(ActionEvent event) {
        String bookID = bookIDInput.getText();
        String memberID = memberIDInput.getText();
        
        if(!bookID.isEmpty() && !memberID.isEmpty() && bookIsAvailable){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Book Issue Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are u sure to issue the book " + bookName.getText() + "\nto " + memberName.getText()+ " ?");

            Optional<ButtonType> response = alert.showAndWait();

            if(response.get() == ButtonType.OK){
                String query1 = "INSERT INTO ISSUE (memberID, bookID) VALUES(" +
                        "'" + memberID + "'," +
                        "'" + bookID + "')";
                String query2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "'";

                System.out.println(query1 + " and \n" + query2);

                if(handler.execAction(query1) && handler.execAction(query2)){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book issue complete");
                    alert1.showAndWait();
                }else{
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Issue Operation Failed");
                    alert1.showAndWait();
                }
            }else{
    //            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
    //            alert1.setTitle("Cancel");
    //            alert1.setHeaderText(null);
    //            alert1.setContentText("Issue Operation Canceled");
    //            alert1.showAndWait();
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Issue Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Unable to Issue this book.");
            alert1.showAndWait();
        }
        
    }

    @FXML
    private void showDetaisAboutThisBook(ActionEvent event) {
        readyForSubmission = false;
        ObservableList<String> issueData = FXCollections.observableArrayList();
        
        String bookID = bookIDForDetails.getText();
        String query = "SELECT * FROM ISSUE WHERE bookID = '" + bookID + "'";
        ResultSet result = handler.fetchDataByExecQuery(query);
        
        try {
            while(result.next()){
                issueData.add("Issue Date and Time: " + result.getTimestamp("issueTime").toGMTString());
                issueData.add("Renew count: " + result.getInt("renewCount"));
                
                issueData.add("Book Information:-------------------------");
                String quForBook = "SELECT * FROM BOOK WHERE id = '" + result.getString("bookID") + "'";
                ResultSet resultForBook = handler.fetchDataByExecQuery(quForBook);
                while(resultForBook.next()){
                    issueData.add("Book Name: " + resultForBook.getString("title"));
                    issueData.add("Book id: " + resultForBook.getString("id"));
                    issueData.add("Book Author: " + resultForBook.getString("author"));
                    issueData.add("Book Publisher: " + resultForBook.getString("publisher"));
                }
                
                issueData.add("Member Information:-------------------------");
                String quForMember = "SELECT * FROM MEMBER WHERE id = '" + result.getString("memberID") +"'";
                ResultSet resultForMember = handler.fetchDataByExecQuery(quForMember);
                while(resultForMember.next()){
                    issueData.add("Member Name: " + resultForMember.getString("name"));
                    issueData.add("Member Mobile: " + resultForMember.getString("mobile"));
                    issueData.add("Member Email: " + resultForMember.getString("email"));
                }
                readyForSubmission = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        issueDataList.getItems().setAll(issueData);
    }

    @FXML
    private void renewOperation(ActionEvent event) {
        if(!readyForSubmission){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book to renew.");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Submission Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are u sure to submit this book?");

        Optional<ButtonType> response = alert.showAndWait();

        if(response.get() == ButtonType.OK){
            String query = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renewCount = renewCount + 1 WHERE bookID = '" + bookIDForDetails.getText() + "'";
            if(handler.execAction(query)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book has been renewed Successfully.");
                alert1.showAndWait();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book renew has been failed.");
                alert1.showAndWait();
            }
        }else{
            // code for cancel operations.....
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancel");
            alert1.setHeaderText(null);
            alert1.setContentText("Book renew is Canceled");
            alert1.showAndWait();
        }
    }

    @FXML
    private void submissionOperation(ActionEvent event) {
        if(!readyForSubmission){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Unable to submit this book");
            alert.showAndWait();
            return;
        }
        
        String bookID = bookIDForDetails.getText();
        String query1 = "DELETE FROM ISSUE WHERE bookID = '" + bookID + "'";
        String query2 = "UPDATE BOOK SET isAvail = TRUE WHERE id = '" + bookID + "'";
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Submission Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are u sure to submit this book?");

        Optional<ButtonType> response = alert.showAndWait();

            if(response.get() == ButtonType.OK){
                if(handler.execAction(query1) && handler.execAction(query2)){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Submission Success");
                    alert1.showAndWait();
                }else{
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Submission Failed");
                    alert1.showAndWait();
                }
            }else{
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Cancel");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Submission is Canceled");
                alert1.showAndWait();
            }
        
        
    }
    
    @FXML
    void close(ActionEvent event) {
        handler.closeConnection();
        Platform.exit();
	System.exit(0);
    }
    
}