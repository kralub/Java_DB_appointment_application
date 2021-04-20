/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAndViews;

import Models.Users;
import Utilities.DateTime;
import Utilities.JDBConnection;
import static Utilities.JDBConnection.isConnected;
import static javafx.fxml.FXMLLoader.load;

import java.io.BufferedWriter;
import java.io.FileWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import javafx.scene.text.Text;
/**
 *
 * @author calebbayles
 */
public class MainViewController implements Initializable{
    //declare buttons
    @FXML
    Button customerButton;
    @FXML
    Button appointmentsButton;
    @FXML
    Button generateReportsButton;
    @FXML
    Button closeSessionButton;
    @FXML
    Text welcomeText;
    
    
    Parent root;
    Stage stage;
    
    @FXML
    public void handleCustomerClick(ActionEvent event) throws IOException{
        //OPEN MAIN VIEW
        root = load(getClass().getResource("CustomerView.fxml"));
        stage = (Stage) customerButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleAppointmentsClick(ActionEvent event) throws IOException{
        //OPEN MAIN VIEW
        root = load(getClass().getResource("AppointmentsView.fxml"));
        stage = (Stage) appointmentsButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleGenerateReportsClick(ActionEvent event) throws IOException{
        //OPEN MAIN VIEW
        root = load(getClass().getResource("ReportsView.fxml"));
        stage = (Stage) generateReportsButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleCloseSessionClick(ActionEvent event) throws SQLException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);            
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm exit:");
            alert.setContentText("Do you really want to close this session?");
            Optional<ButtonType> result = alert.showAndWait();
       
            if (result.get() == ButtonType.OK) {         
                JDBConnection.closeConnection();
                System.out.println("Program Exit.");
                System.exit(0);
            } 
            else{
                System.out.println("Exit canceled.");
            }        
    }
    // FIXED
    public void showUpcomingAppointments(int user_ID){
        System.out.println("TESTING UPCOMING APPT");
        // getting datetime in utc
        ZonedDateTime currTime = ZonedDateTime.now(ZoneId.of("UTC"));
//        System.out.println("1 : " + currTime);
        ZonedDateTime currTimePlusFifteen = currTime.plusMinutes(15);
//        System.out.println("2 : " + currTimePlusFifteen);
        // converting to timestamp
        Timestamp sqlNowTime = Timestamp.valueOf(currTime.toLocalDateTime());
//        System.out.println("3 : " + sqlNowTime);
        Timestamp plusTime = Timestamp.valueOf(currTimePlusFifteen.toLocalDateTime());
//        System.out.println("4: " + plusTime);

//        System.out.println("user id is : " + user_ID);

        try {

            PreparedStatement pst = JDBConnection.startConnection().prepareStatement(
                    "SELECT * FROM WJ0767M.appointments \n" +
                            "WHERE (Start BETWEEN  \"" + sqlNowTime + "\" AND \"" + plusTime + "\") AND User_ID = " + user_ID +";");
//            System.out.println("Your sql statement: " + pst);
            ResultSet rs = pst.executeQuery();
            System.out.println("SQL STATEMENT: " + pst);
            if (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String date = rs.getString("Start");
                DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ZoneId utcZoneID = ZoneId.of("UTC");
                ZoneId localZoneID = ZoneId.systemDefault();
                LocalDateTime utcStartDT = LocalDateTime.parse(date, datetimeDTF);
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                String localStartDT = localZoneStart.format(datetimeDTF);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ALERT!");
                alert.setHeaderText("Appointment incoming.");
                alert.setContentText("You have an appointment of ID: " + appointmentID + " within the next 15 minutes at " + localStartDT + " (in your local time)!");
                alert.showAndWait();

            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Appointments");
                alert.setHeaderText("Message: ");
                alert.setContentText("You DO NOT have any appointments within the next 15 minutes.");
                alert.showAndWait();
                return;
            }

        } catch (SQLException sqe) {
            System.out.println("SQL ERROR.");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("non SQL error.");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Main Window opened!");
        Users currUser = LoginController.currUser;
        String theUsername = currUser.getUserName();
        int theCurrUserID = currUser.getUserID();
        welcomeText.setText("Hello " + theUsername + "!");
//        System.out.println("THE CURR USERS ID: " + theCurrUserID);
        showUpcomingAppointments(theCurrUserID);
    }
}
