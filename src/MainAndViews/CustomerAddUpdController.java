/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAndViews;

import Models.Address;
import Models.Customers;
import Utilities.JDBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author calebbayles
 */

public class CustomerAddUpdController implements Initializable {

    @FXML
    Text AddUpdTitle;
    @FXML
    Text IDText;
    @FXML
    TextField nameTextField;
    @FXML
    TextField addressTextField;
    @FXML
    TextField postalCodeTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    ComboBox customerCountryComboBox;
    @FXML
    ComboBox customerFirstLevelDivisionComboBox;
    @FXML
    Button cancelButton;
    @FXML
    Button addUpdButton;
    Parent root;
    Stage stage;

    ObservableList<String> FirstLevelDivisionOptions = FXCollections.observableArrayList();
    ObservableList<String> countryOptions = FXCollections.observableArrayList();

    @FXML
    private void handleAddUpdButton() throws SQLException, IOException {
        // declare imp variables
        int theId = CustomerViewController.thisID;
        boolean isUpdate = CustomerViewController.isUpdate;
        Customers theCust = CustomerViewController.selectedCustomer;
        // get values
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneTextField.getText();
        String country = customerCountryComboBox.getValue().toString();
        String firstLevelDivision = customerFirstLevelDivisionComboBox.getValue().toString();
        int theDivID = Address.findDivisionID(firstLevelDivision);
        // test for blank boxes
        if (name.length() < 1 || address.length() < 1 || postalCode.length() < 1
                || phone.length() < 1 || country.length() < 1
                || firstLevelDivision.length() < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty text fields!");
            alert.setHeaderText("Your error: ");
            alert.setContentText("You must fill in each box.");
            alert.showAndWait();

        } // update customer
        else if (isUpdate) {
            Statement stmt = JDBConnection.conn.createStatement();

            String sqlStatement = "UPDATE WJ0767M.customers\n"
                    + "SET Customer_Name = \"" + name + "\", Address = \"" + address + "\", Postal_Code = \"" + postalCode + "\", Phone = \"" + phone + "\", Division_ID = \"" + theDivID + "\" \n"
                    + "WHERE Customer_ID = " + theId + ";";

            int result = stmt.executeUpdate(sqlStatement);
            System.out.println(result + " Rows Effected!");

            root = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
            stage = (Stage) addUpdButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Statement stmt = JDBConnection.conn.createStatement();

            String sqlStatement = "INSERT INTO WJ0767M.customers (Customer_Name, Address, Phone, Postal_Code, Division_ID)\n"
                    + "VALUES (\"" + name + "\", \"" + address + "\", \"" + phone + "\", \"" + postalCode + "\", \"" + theDivID + "\");";

            int result = stmt.executeUpdate(sqlStatement);
            System.out.println(result);
            root = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
            stage = (Stage) addUpdButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    private void handleAddUpdTranslation(boolean isUpdate, Customers cust) {
        // if its an update then translate this stuff

        if (isUpdate) {
            AddUpdTitle.setText("Update Customer: " + cust.getCustID());
            int id = cust.getCustID();
            IDText.setText(String.valueOf(id));
            nameTextField.setText(cust.getCustName());
            addressTextField.setText(cust.getCustAddress());
            postalCodeTextField.setText(cust.getCustPostalCode());
            phoneTextField.setText(cust.getCustPhone());
            customerCountryComboBox.setValue(cust.getCustCountry());
            customerFirstLevelDivisionComboBox.setValue(cust.getCustFirstLevelDiv());
            addUpdButton.setText("Update");
        } else {
            int newID = CustomerViewController.thisID;
            AddUpdTitle.setText("Add New Customer");
            IDText.setText(String.valueOf(newID));

        }
    }

    public void fillCountryComboBox() throws SQLException, Exception {
        Statement stmt = JDBConnection.startConnection().createStatement();
        String sqlStatement = "SELECT country FROM WJ0767M.countries";
        ResultSet result = stmt.executeQuery(sqlStatement);

        while (result.next()) {
            Customers cust = new Customers();
            cust.setCustomerCountry(result.getString("country"));
            countryOptions.add(cust.getCustCountry());
            customerCountryComboBox.setItems(countryOptions);
        }
        stmt.close();
        result.close();
    }

    @FXML
    public void handleEnterButton(ActionEvent event) throws SQLException, Exception {
        FirstLevelDivisionOptions.clear();
        customerFirstLevelDivisionComboBox.setValue("Null or no FLD for Country.");
        System.out.println("FILTERING");
        String theCountryName = customerCountryComboBox.getValue().toString();
        System.out.println("the country name: " + theCountryName);
        int countryID = Address.findCountryID(theCountryName);
        System.out.println("the country ID: " + countryID);
        
        Statement stmt2 = JDBConnection.startConnection().createStatement();

        String sqlStatement2 = "SELECT Division FROM WJ0767M.first_level_divisions WHERE Country_ID = " + countryID + ";";

        ResultSet result2 = stmt2.executeQuery(sqlStatement2);
        result2.next();
        while (result2.next()) {
            Customers cust1 = new Customers();
            cust1.setCustomerFirstLevelDiv(result2.getString("Division"));
            FirstLevelDivisionOptions.add(cust1.getCustFirstLevelDiv());
            customerFirstLevelDivisionComboBox.setItems(FirstLevelDivisionOptions);
        }

        stmt2.close();
        result2.close();
        System.out.println("FILTERING ENDED");
    }

    @FXML
    public void handleCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm cancel:");
        alert.setContentText("Do you really want to cancel this?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            //OPEN MAIN VIEW
            root = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
            stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("cancel canceled.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int theId = CustomerViewController.thisID;
        boolean isUpdate = CustomerViewController.isUpdate;
        Customers theCust = CustomerViewController.selectedCustomer;
        try {
            fillCountryComboBox();
        } catch (Exception ex) {
            Logger.getLogger(CustomerAddUpdController.class.getName()).log(Level.SEVERE, null, ex);
        }

        handleAddUpdTranslation(isUpdate, theCust);
    }
}
