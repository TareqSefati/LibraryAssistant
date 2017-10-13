package library.asistant.ui.addMember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author TAREQ SEFATI
 */
public class AddMemberController implements Initializable {

    DatabaseHandler handler;
    
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField memberID;
    @FXML
    private JFXTextField contact;
    @FXML
    private JFXTextField email;
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
    }    

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID = memberID.getText();
        String mContact = contact.getText();
        String mEmail = email.getText();
        
        if(mName.isEmpty() || mID.isEmpty() || mContact.isEmpty() || mEmail.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill all of the fields.");
            alert.showAndWait();
            return;
        }
        
        String query = "INSERT INTO MEMBER VALUES (" + 
                "'"+ mID +"',"+
                "'"+ mName +"',"+
                "'"+ mContact +"',"+
                "'"+ mEmail +"'"+
                ")";
        //System.out.println(query);
        
        if(handler.execAction(query)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Member added Successfully");
            alert.showAndWait();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }else{
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText("Error occured when try to add a Member");
//            alert.showAndWait();
        }
        
    }

    @FXML
    private void cancelMember(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
}
