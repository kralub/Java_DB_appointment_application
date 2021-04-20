/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAndViews;

import Models.Address;
import Models.Customers;
import Utilities.JDBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 *
 * @author calebbayles
 */

public class CustomerViewController implements Initializable{
    //fxml ids
    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private Button backButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private TableColumn<Customers, Integer> IDColumn;
    @FXML
    private TableColumn<Customers, String> nameColumn;
    @FXML
    private TableColumn<Customers, String> addressColumn;
    @FXML
    private TableColumn<Customers, String> postalCodeColumn;
    @FXML
    private TableColumn<Customers, String> phoneColumn;
    @FXML
    private TableColumn<Customers, Integer> countryColumn;
    @FXML
    private TableColumn<Customers, String> firstLevelDivisionColumn;
    @FXML
    private TableColumn<Customers, Integer> divisionIDColumn;

    static public int maxID;
    static public int selectedID;
    static public int thisID;
    static public boolean isUpdate;
    static public Customers addCust;
    
    Parent root;
    Stage stage;
    
    //create ObservableLists
    ObservableList<Customers> customerOL = FXCollections.observableArrayList();

    public static Customers selectedCustomer = new Customers();
    

    
    
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("OPENING CUSTOMERVIEW");
        maxID = -1;
        thisID = -1;
        // POPULATE TABLEVIEW
        //FACTORY USES NAME OF THE STRING TO FIND THE GETTER FUNCTION SO IT 
        //MUST MATCH THE GETTER FUNCTION NAME IN THE CUSTOMER CLASS!!!
        PropertyValueFactory<Customers, Integer> custCustomerIDFactory = new PropertyValueFactory<>("CustID");
        PropertyValueFactory<Customers, String> custNameFactory = new PropertyValueFactory<>("CustName");
        PropertyValueFactory<Customers, String> custCustomerAddressFactory = new PropertyValueFactory<>("CustAddress");
        PropertyValueFactory<Customers, String> custCustomerPostalCodeFactory = new PropertyValueFactory<>("CustPostalCode");
        PropertyValueFactory<Customers, String> custPhoneFactory = new PropertyValueFactory<>("CustPhone"); 
        PropertyValueFactory<Customers, Integer> custCountryFactory = new PropertyValueFactory<>("CustCountry");
        PropertyValueFactory<Customers, Integer> custDivisionIDFactory = new PropertyValueFactory<>("CustDivisionID");
        PropertyValueFactory<Customers, String> custFirstLevelDivisionFactory = new PropertyValueFactory<>("CustFirstLevelDiv");

        
        IDColumn.setCellValueFactory(custCustomerIDFactory);
        nameColumn.setCellValueFactory(custNameFactory);
        addressColumn.setCellValueFactory(custCustomerAddressFactory);
        phoneColumn.setCellValueFactory(custPhoneFactory);
        postalCodeColumn.setCellValueFactory(custCustomerPostalCodeFactory);
        countryColumn.setCellValueFactory(custCountryFactory);
        divisionIDColumn.setCellValueFactory(custDivisionIDFactory);
        firstLevelDivisionColumn.setCellValueFactory(custFirstLevelDivisionFactory);
        
        try {
            updateCustomerTable();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Listen for mouse click on item in Customer Table
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        customerListener(newValue);
                    } catch (Exception ex) {
                        System.out.println("Customer Listener had an error!");
                    }
                });
    }


    
    
    //fxml funtions for buttons
    @FXML
    public void handleBackButton() throws IOException{
        root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleAddButton() throws IOException{
        thisID = maxID + 1;
        isUpdate = false;
        selectedCustomer = addCust;

        // get stage
        root = FXMLLoader.load(getClass().getResource("CustomerAddUpdView.fxml"));
        stage = (Stage) addButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleUpdateButton() throws IOException{
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
        
        thisID = selectedID;
        isUpdate = true;
        

        // get stage
        root = FXMLLoader.load(getClass().getResource("CustomerAddUpdView.fxml"));
        stage = (Stage) updateButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("None Selected");
            alert.setContentText("You must select a customer you want to update.");
            alert.showAndWait();
        }
    }

    @FXML
    private void CustomerBackButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    //populate table
    public void updateCustomerTable() throws SQLException {
        System.out.println("BEGIN TABLE UPDATE");
        customerOL.clear();
        Statement stmt = JDBConnection.conn.createStatement();

        String sqlStatement = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM WJ0767M.customers;";

        ResultSet result = stmt.executeQuery(sqlStatement);
        
        // get maxID to know what the next id will be
        maxID = -3;
        //get all records from resultset object
        while (result.next()) {
            Customers cust = new Customers();
            cust.setCustomerID(result.getInt("Customer_ID"));
            if(result.getInt("Customer_ID") > maxID) maxID = result.getInt("Customer_ID");
            cust.setCustomerName(result.getString("Customer_Name"));
            cust.setCustomerAddress(result.getString("Address"));
            cust.setCustomerPostalCode(result.getString("Postal_Code"));
            cust.setCustomerPhone(result.getString("Phone")); 
            cust.setCustomerDivisionID(result.getInt("Division_ID"));
            //get FLDivision
            int thisDivID = result.getInt("Division_ID");
            String theDivision = Address.findDivision(thisDivID);
            cust.setCustomerFirstLevelDiv(theDivision);
            //get and set country
            String theCountry = Address.findCountry(thisDivID);
            cust.setCustomerCountry(theCountry);
            customerOL.addAll(cust);
        }
        customerTable.setItems(customerOL);
        System.out.println("END TABLE UPDATE");
    }
    @FXML
    private void handleDeleteButton(ActionEvent event) throws Exception {


        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            Customers cust = customerTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Required");
            alert.setHeaderText("Confirm deletion...");
            alert.setContentText("Are you sure you want to delete this customer with the ID: " + cust.getCustID() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deleteCustomer(cust);
                System.out.println("CustomerID: " + cust.getCustID() + " has been deleted!");
                updateCustomerTable();
            } else {
                System.out.println("delete was canceled.");
            }
        } else {
            System.out.println("No customer was selected...");
        }
    }
    private void deleteCustomer(Customers customer) throws Exception {
        if(!custHasAppointments(customer.getCustID())) {
            int theID = customer.getCustID();
            try {
                PreparedStatement ps = JDBConnection.startConnection().prepareStatement("DELETE FROM WJ0767M.customers WHERE Customer_ID = " + theID + ";");
                int result = ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL ERROR");
            }
        }
    }

    public boolean custHasAppointments(int custID) throws SQLException {
            boolean custHasAppointments = true;
            try {
                PreparedStatement pst = JDBConnection.startConnection().prepareStatement(
                        "SELECT * FROM WJ0767M.appointments WHERE Customer_ID = " + custID + ";");
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Customer Conflict");
                    alert.setHeaderText("There is a conflict with this deletion.");
                    alert.setContentText("All Customer appointments must be deleted before this customer can be deleted.");
                    alert.showAndWait();
                    custHasAppointments = true;
                }
                else {
                    custHasAppointments =  false;
                }

            } catch (SQLException sqe) {
                System.out.println("SQL error 'hasConflict'.");
                sqe.printStackTrace();
            } catch (Exception e) {
                System.out.println("non SQL error.");
                e.printStackTrace();
            }
            return custHasAppointments;
    }


    public void customerListener(Customers customer) throws SQLException, Exception {
        
        
        System.out.println("LISTENING TO THIS SELECTION");
        Customers cust;
        cust = customer;
        selectedCustomer = cust;
        String custName = cust.getCustName();
        System.out.println(custName);
        int custId = cust.getCustID();
        ObservableList<Customers> customerOL = FXCollections.observableArrayList();
        
        selectedID = custId;
    }
    
}


