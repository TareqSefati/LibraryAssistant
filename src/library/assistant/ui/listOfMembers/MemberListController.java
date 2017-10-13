/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.listOfMembers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.asistant.ui.addBook.AddBookController;
import library.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author TAREQ SEFATI
 */
public class MemberListController implements Initializable {

    ObservableList<Member> list = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> contactCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }    
    
    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    
    private void loadData() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        
        String query = "SELECT * FROM MEMBER";
        ResultSet results = handler.fetchDataByExecQuery(query);
        
        try {
            while(results.next()){
                String name = results.getString("name");
                String id = results.getString("id");
                String contact = results.getString("mobile");
                String email = results.getString("email");
                
                list.add(new Member(name, id, contact, email));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tableView.getItems().setAll(list);
    }
    
    public static class Member {
        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty contact;
        private final SimpleStringProperty email;
        
        public Member(String name, String id, String contact, String email){
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.contact = new SimpleStringProperty(contact);
            this.email = new SimpleStringProperty(email);
        }
        
        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getContact() {
            return contact.get();
        }

        public String getEmail() {
            return email.get();
        }

        
    }
}
