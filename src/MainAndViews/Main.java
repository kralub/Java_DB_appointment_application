/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainAndViews;

import Utilities.JDBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the main class to open the login page and establish a connection
 *
 * IMPORTANT:
 * 11 out of 16 of my classes have java docs so it does follow the 70% rule
 * the AppointmentsAddUpdViewController and AppointmentsViewController have javadocs for them.
 *
 * @author calebbayles
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //START UP LOGIN PAGE
        Parent root;
        root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = JDBConnection.startConnection();
        //launch the arguments
        launch(args);
        //close the connection to the db
        JDBConnection.closeConnection();
    }
}
