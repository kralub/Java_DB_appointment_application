// FINISHED!!!!!!!
package MainAndViews;

import Models.Appointment;
import Models.Customers;
import Utilities.JDBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This is the appointments add and update view controller.
 *
 * @author calebbayles
 */

public class AppointmentsAddUpdViewController implements Initializable {
    @FXML
    Button backButton;
    @FXML
    Button submitButton;
    @FXML
    TextField typeTextField;
    @FXML
    Text addUpdApptTitle;
    @FXML
    Text apptIDText;
    @FXML
    ComboBox contactComboBox;
    @FXML
    ComboBox customerIDComboBox;
    @FXML
    ComboBox userIDComboBox;
    @FXML
    TextField titleTextField;
    @FXML
    TextField descriptionTextField;
    @FXML
    TextField locationTextField;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox startComboBox;
    @FXML
    ComboBox endComboBox;

    Parent root;
    Stage stage;

    // SETUP TABLE VALUES
    ObservableList<String> contactOptions = FXCollections.observableArrayList();
    ObservableList<Integer> customerIDOptions = FXCollections.observableArrayList();
    ObservableList<Integer> userIDOptions = FXCollections.observableArrayList();
    ObservableList<String> startTimes = FXCollections.observableArrayList();
    ObservableList<String> endTimes = FXCollections.observableArrayList();
    // SETUP TIME VALUES
    DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    int theID = AppointmentsViewController.thisID;
    Appointment theAppt = AppointmentsViewController.selectedAppointment;
    boolean isUpdate = AppointmentsViewController.isUpdate;
    private final ZoneId localZoneID = ZoneId.systemDefault();
    public int custID;
    /**
     * This handles when the user presses on the back button and brings them back to the appointments view
     *
     * @throws IOException
     */
    @FXML
    private void handleBackButton() throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentsView.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This contact combo box from the DB
     *
     * @throws Exception
     */
    private void fillContactComboBox() throws Exception {
        Statement stmt = JDBConnection.startConnection().createStatement();
        String sqlStatement = "SELECT Contact_Name FROM WJ0767M.contacts";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
            Appointment appt = new Appointment();
            appt.setContact(result.getString("Contact_Name"));
            contactOptions.add(appt.getContact());
            contactComboBox.setItems(contactOptions);
        }
        stmt.close();
        result.close();
    }
    /**
     * This fills the id combo box
     *
     * @throws Exception
     */
    private void fillCustomerIDComboBox() throws Exception {
        Statement stmt = JDBConnection.startConnection().createStatement();
        String sqlStatement = "SELECT Customer_ID FROM WJ0767M.customers";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
            Appointment appt = new Appointment();
            appt.setCustomerID(result.getInt("Customer_ID"));
            customerIDOptions.add(appt.getCustomerID());
            customerIDComboBox.setItems(customerIDOptions);
        }
        stmt.close();
        result.close();
    }
    /**
     * This fills the user ID combo box
     *
     * @throws Exception
     */
    private void fillUserIDComboBox() throws Exception {
        Statement stmt = JDBConnection.startConnection().createStatement();
        String sqlStatement = "SELECT User_ID FROM WJ0767M.users";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
            int thisUserID = result.getInt("User_ID");
            userIDOptions.add(thisUserID);
            userIDComboBox.setItems(userIDOptions);
        }
        stmt.close();
        result.close();
    }
    /**
     * This handles translating the labels upon opening this view so that it auto populates if it is an update and
     * handles the id that is displayed based on if it is add or update
     *
     *
     */
    private void handleIsUpdateTranslation() {
        // will work for both update and add
        apptIDText.setText("Appt. ID: " + theID);
        // if its an update
        if (isUpdate) {
            addUpdApptTitle.setText("Update Appointment");
            updateAppointmentFields();
        } else {
            addUpdApptTitle.setText("Add Appointment");
        }
    }
    /**
     * This fills the start times list
     *
     *
     */
    private void fillStartTimesList() {
        LocalTime time = LocalTime.of(1, 0, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(15);
        } while (!time.equals(LocalTime.of(23, 30, 0)));
        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);
        if (!isUpdate) {
            datePicker.setValue(LocalDate.now());
        }
        startComboBox.setItems(startTimes);
        endComboBox.setItems(endTimes);
        startComboBox.getSelectionModel().select(LocalTime.of(1, 0, 0).format(timeDTF));
        endComboBox.getSelectionModel().select(LocalTime.of(2, 00, 0).format(timeDTF));
    }
    /**
     * this fills the appointment fields if it is an update
     *
     */
    private void updateAppointmentFields() {
        System.out.println("POPULATING APPT UPDATE FIELDS");
        System.out.println("Selected Appointment CustomerID: " + theAppt.getCustomerID());
        theAppt.setCustomerID(theAppt.getCustomerID());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String startLocal = theAppt.getStart();

        String endLocal = theAppt.getEnd();

        LocalDateTime localDateTimeStart = LocalDateTime.parse(startLocal, datetimeDTF);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(endLocal, datetimeDTF);

        LocalDate localDate = localDateTimeStart.toLocalDate();
        System.out.println("localDate: " + localDate);

        LocalTime localTimeStart = localDateTimeStart.toLocalTime();

        System.out.println("localTimeStart: " + localTimeStart);

        userIDComboBox.setValue(theAppt.getUserID());
        customerIDComboBox.setValue(theAppt.getCustomerID());
        titleTextField.setText(theAppt.getTitle());
        descriptionTextField.setText(theAppt.getDescription());
        typeTextField.setText(theAppt.getType());
        contactComboBox.setValue(theAppt.getContact());
        locationTextField.setText(theAppt.getLocation());
        datePicker.setValue(localDate);

        startComboBox.getSelectionModel().select(localDateTimeStart.toLocalTime().format(timeDTF));
        endComboBox.getSelectionModel().select(localDateTimeEnd.toLocalTime().format(timeDTF));
        System.out.println("ENDING POPULATE UPDATE");
    }
    /**
     * This handles when the user presses on the submit button and does error checking to avoid messing up the db
     * it does sql:update when it is an update and sql:insert when it is an add
     *
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void handleSubmitButton() throws SQLException, IOException {
        String contactName = contactComboBox.getValue().toString();

        int contactID = Customers.findContactID(contactName);
        custID = (int) customerIDComboBox.getValue();
        int userID = (int) userIDComboBox.getValue();
        System.out.println("USER ID IS " + userID);
        String title = titleTextField.getText();
        String type = typeTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        LocalDate localDate = datePicker.getValue();
        LocalTime localStartTime = LocalTime.parse((CharSequence) startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime localEndTime = LocalTime.parse((CharSequence) endComboBox.getSelectionModel().getSelectedItem(), timeDTF);

        LocalDateTime startDT = LocalDateTime.of(localDate, localStartTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, localEndTime);

        ZonedDateTime startUTC = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        // used to test if in business hours
        ZonedDateTime startEST = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("US/Eastern"));
        ZonedDateTime endEST = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("US/Eastern"));


        //convert UTC to timestamp for db
        Timestamp sqlStartTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endUTC.toLocalDateTime());
        System.out.println("the start time in utc is: " + sqlStartTS + "\n the end time in utc is: " + sqlEndTS);

        // error phase initiated!
        // checks if description is too long
        boolean isTooLong = description.length() > 50;

        if (isTooLong) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Description Too long");
            alert.setHeaderText("There is a problem in your description.");
            alert.setContentText("Description must be less than 51 Characters");
            alert.showAndWait();
            return;
        } else if ((isValidAppt(isNotNull(), hasConflict(startUTC, endUTC), isWithinBusinessHours(startEST, endEST))) && !isTooLong) {
            if (isUpdate) {
                System.out.println("TRYING UPDATE APPT");
                Statement stmt = JDBConnection.conn.createStatement();

                String sqlStatement = "UPDATE WJ0767M.appointments\n"
                        + "SET Description = \"" + description + "\", Location = \"" + location + "\", Start = \"" +
                        sqlStartTS + "\", End = \"" + sqlEndTS + "\", Title = \"" + title + "\", Type = \"" + type + "\", " +
                        "Customer_ID = " + custID + ", Contact_ID = " + contactID + ", User_ID = " + userID + "\n"
                        + "WHERE Appointment_ID = " + theID + ";";

                int result = stmt.executeUpdate(sqlStatement);
                System.out.println(result + " Rows Effected! for appt update");

                root = FXMLLoader.load(getClass().getResource("AppointmentsView.fxml"));
                stage = (Stage) submitButton.getScene().getWindow();
                Scene scene = new Scene(root);
                scene.setRoot(root);
                stage.setScene(scene);
                stage.show();
            }
            // insert into db or ADD
            else {
                System.out.println("TRYING ADD APPT");
                Statement stmt = JDBConnection.conn.createStatement();

                String sqlStatement = "INSERT INTO WJ0767M.appointments (Description, Location, Start, End, Title, Type, Customer_ID, Contact_ID, User_ID)\n" +
                        "VALUES(\"" + description + "\", \"" + location + "\", \"" + sqlStartTS + "\", \n" +
                        "\"" + sqlEndTS + "\", \"" + title + "\", \"" + type + "\"," + custID + "," + contactID + "," + userID + ");";

                int result = stmt.executeUpdate(sqlStatement);
                System.out.println(result + " Rows Effected! for appt update");

                root = FXMLLoader.load(getClass().getResource("AppointmentsView.fxml"));
                stage = (Stage) submitButton.getScene().getWindow();
                Scene scene = new Scene(root);
                scene.setRoot(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    // create error checking functions
    // create checker to see if values are all entered
    /**
     * This does an error check to make sure there are no null values
     *
     *
     */
    private boolean isNotNull() {
        if (contactComboBox.getValue().toString().length() < 1 ||
                customerIDComboBox.getValue().toString().length() < 1 ||
                userIDComboBox.getValue().toString().length() < 1 ||
                datePicker.getValue().toString().length() < 1 ||
                startComboBox.getValue().toString().length() < 1 ||
                endComboBox.getValue().toString().length() < 1 ||
                titleTextField.getLength() < 1 ||
                typeTextField.getLength() < 1 ||
                descriptionTextField.getLength() < 1 ||
                locationTextField.getLength() < 1) {
            // give null message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NULL VALUES ERROR");
            alert.setHeaderText("There is a problem in input.");
            alert.setContentText("You must have every box entered with a value. You have null values.");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    // check for customer conflicts
    /**
     * This makes sure that the add does not interfere with other appointments that the customer has
     *
     * @param newEndUTC the end utc time for the selected time
     * @param newStartUTC the start utc time for the selected time
     */
    private boolean hasConflict(ZonedDateTime newStartUTC, ZonedDateTime newEndUTC) {
        Timestamp timeStampStart = Timestamp.valueOf(newStartUTC.toLocalDateTime());
        Timestamp timeStampEnd = Timestamp.valueOf(newEndUTC.toLocalDateTime());
        int theApptID = theAppt.getAppointmentID();
        boolean isConflicted = false;
        try {

            PreparedStatement pst = JDBConnection.startConnection().prepareStatement(
                    "SELECT * FROM WJ0767M.appointments WHERE (\"" + timeStampStart + "\" BETWEEN Start AND End OR \n" +
                            "\"" + timeStampEnd + "\" BETWEEN Start AND End OR \"" + timeStampStart + "\" < Start AND \"" + timeStampEnd + "\" > End) \n" +
                            "AND Customer_ID = " + custID + "\n" +
                            "AND NOT Appointment_ID = " + theApptID + " ;");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Conflict");
                alert.setHeaderText("There is a conflict with this appt.");
                alert.setContentText("The customer has another appointment that is within this time.");
                alert.showAndWait();
                isConflicted = true;
            } else {
                isConflicted = false;
            }

        } catch (SQLException sqe) {
            System.out.println("SQL error 'hasConflict'.");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("non SQL error.");
            e.printStackTrace();
        }
        return isConflicted;
    }

    // check if in business hours
    /**
     * This makes sure that the appt is within the business hours
     *
     * @param newEndUTC the selected end time in utc form
     * @param newStartUTC the selected start time in utc form
     */
    private boolean isWithinBusinessHours(ZonedDateTime newStartUTC, ZonedDateTime newEndUTC) {
        int startHour = newStartUTC.getHour();
        int endHour = newEndUTC.getHour();
        System.out.println("START HOUR = " + startHour + "     END HOUR:" + endHour);
        boolean isWithin;
        //open from 8 am to 10 pm est
        if ((startHour >= 8 && startHour <= 21) && (endHour >= 8 && endHour <= 21)) {
            isWithin = true;
        } else {
            isWithin = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not within hours");
            alert.setHeaderText("There is a conflict with this appt.");
            alert.setContentText("The appointment you are saving is not within our business hours. Please add within " +
                    "8am to 10pm est time.");
            alert.showAndWait();
        }


        return isWithin;
    }
    /**
     * This takes in all error tests and gives a bool that tells if the appt can be added or updated or not
     *
     * @param hasConflict test to see if the appointment has a conflict
     * @param isNotNull  test to see if all inputs have been entered
     * @param isWithinBusinessHours tests to see if the appointment is within business hours
     */
    public boolean isValidAppt(boolean isNotNull, boolean hasConflict, boolean isWithinBusinessHours) {
        boolean isValid = false;
        if (isNotNull) {
            if (!hasConflict) {
                if (isWithinBusinessHours) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }
    /**
     * initializes the addupdcontroller and translates the page based on if it is an update or not
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("STARTING THE ADDUPDATEVIEW");
        try {
            fillContactComboBox();
            fillCustomerIDComboBox();
            fillUserIDComboBox();
            fillStartTimesList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleIsUpdateTranslation();
        System.out.println("ENDING ADD UPDVIEW");
    }
}
