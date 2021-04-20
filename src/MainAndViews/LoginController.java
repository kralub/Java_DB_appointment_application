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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javafx.scene.text.Text;

/**
 * @author calebbayles
 */
public class LoginController implements Initializable {
    @FXML
    private Text welcomeText;
    @FXML
    private Text yourZoneIdText;
    @FXML
    private Text zoneIdText;
    @FXML
    private Text usernameText;
    @FXML
    private Text passwordText;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button enterButton;
    @FXML
    private Text hostText;
    @FXML
    private Text locationText;

    public static Users currUser = new Users();

//    public static Users currentUser = new Users();


    //find out the locale
    public static boolean isFrance(Locale usersLocale) {
        // puts the users display language in a string
        String userCountry = usersLocale.getCountry();

        //test for if it is france
        boolean isItFrance = false;
        if (userCountry.equals("FR")) isItFrance = true;

        //System.out.println(isFrance);
        return isItFrance;
    }

    //transate form if country is france
    public void handleTranslation(boolean isItFrance, String userCountry) {
        if (isItFrance) {
            welcomeText.setText("Bienvenue!");
            yourZoneIdText.setText("Votre identifiant de zone");
            zoneIdText.setText(userCountry);
            usernameText.setText("Nom d'utilisateur: ");
            passwordText.setText("Mot de passe: ");
            enterButton.setText("Entrer");
            hostText.setText("Hôte non connecté");
        }

    }

    public void handleConnectionText(boolean isFrance, boolean isItConnected) {
        if (isItConnected) {
            if (isFrance) {
                hostText.setText("Hôte connecté");
            } else hostText.setText("Host Connected");

        }
    }

    // handle enter button
    @FXML
    public void handleEnterButton(ActionEvent event) throws SQLException, IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        int theUsersID = getThisUserID(username);

        Parent root;
        Stage stage;


        //Translate error messages if needed
        Locale usersLocale = Locale.getDefault();
        String userCountry = usersLocale.getCountry();
        // get bool to translate if needed
        boolean isFrance = isFrance(usersLocale);
        // error messages
        String alertTitle = "Input error";
        String alertHeader = "Your error message: ";
        String noUserMatch = "The username or password you entered does not match "
                + "our user database.";
        String tooLongErrorMsg = "The username or password must be 20 or under Characters long.";
        String blankFields = "One or more text boxes are blank.";

        if (isFrance) {
            alertTitle = "Erreur d'entrée";
            alertHeader = "Votre message d'erreur:";
            //specific errors
            noUserMatch = "Le nom d'utilisateur ou le mot de passe que vous "
                    + "avez entré ne correspond pas à la base de données "
                    + "des utilisateurs.";
            tooLongErrorMsg = "Le nom d'utilisateur ou le mot de passe doit "
                    + "comporter 20 caractères ou moins.";
            blankFields = "Une ou plusieurs zones de texte sont vides.";
        }


        // generate alert messages ----------
        // blank error message:
        if (username.length() < 1 || password.length() < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeader);
            alert.setContentText(blankFields);
            alert.showAndWait();
            loginLog(username, false);
        }
        // text too long
        else if (username.length() > 20 || password.length() > 20) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeader);
            alert.setContentText(tooLongErrorMsg);
            alert.showAndWait();
            loginLog(username, false);
        }

        // THIS IS WHERE LOGIN HAPPENS
        else if (isValidPassword(theUsersID, password)) {
            // get userID again
            currUser.setUserID(theUsersID);
            currUser.setUserName(username);
            currUser.setUserPassword(password);

            // log user
            loginLog(username, true);

            //OPEN MAIN VIEW
            root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
            stage = (Stage) enterButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeader);
            alert.setContentText(noUserMatch);
            alert.showAndWait();
            loginLog(username, false);
        }
    }

    //check to see if password is in db
    private boolean isValidPassword(int userID, String password) throws SQLException {
        //create statement object
        Statement statement = JDBConnection.conn.createStatement();

        //write SQL statement
        String sqlStatement = "SELECT Password FROM WJ0767M.users WHERE User_ID ='" + userID + "'";

        //create resultset object
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            if (result.getString("Password").equals(password)) {
                return true;
            }

        }
        return false;
    }

    // get the user ID from db
    private int getThisUserID(String username) throws SQLException {
        int userID = -1;

        //create statement object
        Statement statement = JDBConnection.conn.createStatement();

        //write SQL statement
        String sqlStatement = "SELECT User_ID FROM WJ0767M.users WHERE User_Name ='" + username + "'";

        //create resultset object
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            userID = result.getInt("User_Id");
        }
        return userID;
    }

    //creates a log // FIXED
    public void loginLog(String userName, boolean wasSucessful) {
        try {
            String wasItSucessfull = "Not Sucessful Login.";
            if (wasSucessful) {
                wasItSucessfull = "Sucessful Login.";
            }
            String fileName = "ProjectForC195/src/MainAndViews/login_activity.txt";
            FileWriter writer = new FileWriter(fileName, true);
            writer.append("Date:" + DateTime.getTimeStamp() + " By: " + userName + "      Successful: " + wasItSucessfull + "\n");
            System.out.println("New login recorded in log file.");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // find out users locale and country
        Locale usersLocale = Locale.getDefault();
        String userCountry = usersLocale.getCountry();
        System.out.println("Users country is: " + userCountry);
        String usersZoneIDString = ZoneId.systemDefault().toString();
        // get bool to translate if needed
        locationText.setText(String.valueOf(usersLocale));

        zoneIdText.setText(ZoneId.systemDefault().toString());
        boolean isFrance = isFrance(usersLocale);

        //translate if needed
        handleTranslation(isFrance, userCountry);
        handleConnectionText(isFrance, isConnected);

    }

}



