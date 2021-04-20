// FINISHED!!!!
package MainAndViews;

import Models.Appointment;
import Models.Customers;
import Utilities.JDBConnection;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This is the appointments view controller. it is used to show the appointments table and to launch add or update view.
 *
 * @author calebbayles
 */
public class AppointmentsViewController implements Initializable {
    @FXML
    private Button backButton;
    @FXML
    private TableView<Appointment> apptTable;
    @FXML
    private TableColumn<Appointment, Integer> apptIDColumn;
    @FXML
    private TableColumn<Appointment, String> descColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIDColumn;
    @FXML
    private RadioButton weekRadioButton;

    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;

    private ToggleGroup RadioButtonToggleGroup;
    private Customers customer = new Customers();
    private Appointment appointment = new Appointment();

    // transfer variables
    public static boolean isUpdate = false;
    public static Appointment selectedAppointment = new Appointment();
    static public int maxID;
    static public int selectedID;
    static public int thisID;


    ObservableList<Appointment> appointmentsOL = FXCollections.observableArrayList();


    Parent root;
    Stage stage;

    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private boolean isWeekly = true;
    /**
     * this is the main function that populates the appointmnets table
     * @throws SQLException
     */
    public void setAppointmentsTable() throws SQLException {
        System.out.println("UPDATING TABLE");

        try {
            Statement stmt = JDBConnection.conn.createStatement();
            String sqlStatement = ("SELECT * FROM WJ0767M.appointments, WJ0767M.customers  " +
                    "WHERE appointments.Customer_ID = customers.Customer_ID"
                    + " ORDER BY 'Start';");
            ResultSet result = stmt.executeQuery(sqlStatement);
            appointmentsOL.clear();
            while (result.next()) {
                int appointmentID = result.getInt("Appointment_ID");
                if (result.getInt("Appointment_ID") > maxID) maxID = result.getInt("Appointment_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                int contactID = result.getInt("Contact_ID");
                String contact = Customers.getContactName(contactID);
                String type = result.getString("Type");
                int customerID = result.getInt("Customer_ID");
                int userID = result.getInt("User_ID");

                //DB times in UTC
                String startUTC = result.getString("Start").substring(0, 19);
                //DB end times in UTC
                String endUTC = result.getString("End").substring(0, 19);
                //convert UTC to the LOCAL TIME
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);

                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);

                appointmentsOL.add(new Appointment(appointmentID, title, description, location, contact, type, localStartDT, localEndDT, customerID, userID));
            }
            apptTable.setItems(appointmentsOL);

            // FILTERING
            if (isWeekly) {
                filterAppointmentsByWeek(appointmentsOL);
            } else {
                filterAppointmentsByMonth(appointmentsOL);
            }
        } catch (SQLException sqe) {
            System.out.println("SQL ERROR FOR UPDATE APPT TABLE!");
        } catch (Exception e) {
            System.out.println("SYNTAX ERROR");
        }
        System.out.println("UPDATE FINISHED");
    }
    /**
     * discussion of lambda 1
     * HERE IS MY LAMBDA 1 EXPRESSION TO INCREASE EFFICIENCY
     * I THOUGHT THAT IT WOULD WORK WELL WITH THE FILTERED DATA FOR MONTH AND WEEK TO INCREASE EFFICIENCY
     * @param appointmentsOL to filter it to only month view
     * @throws SQLException
     */
    public void filterAppointmentsByMonth(ObservableList appointmentsOL) throws SQLException {
        //GETTING A MONTH TIME RANGE
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);
        //CREATING A NEW FILTERED LIST
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsOL);
        // USING LAMBDA HERE!
        filteredData.setPredicate(row -> {
            LocalDate rowDate = LocalDate.parse(row.getStart(), datetimeDTF);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });
        apptTable.setItems(filteredData);
    }
    /**
     * discussion of lambda 2
     * HERE IS MY LAMBDA 2 EXPRESSION TO INCREASE EFFICIENCY
     * I THOUGHT THAT IT WOULD WORK WELL WITH THE FILTERED DATA FOR MONTH AND WEEK TO INCREASE EFFICIENCY
     * @param appointmentsOL to filter it to only week view
     */
    public void filterAppointmentsByWeek(ObservableList appointmentsOL) {
        //GETTING A WEEK TIME RANGE
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Week = now.plusWeeks(1);
        //CREATING A NEW FILTERED LIST
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsOL);
        // USING LAMBDA HERE!
        filteredData.setPredicate(row -> {
            LocalDate rowDate = LocalDate.parse(row.getStart(), datetimeDTF);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Week);
        });
        apptTable.setItems(filteredData);
    }
    /**
     * this is function adds functionality to the back button so that you can go back to the main menu
     * @throws IOException
     */
    @FXML
    private void handleBackButton() throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * this is function adds functionality to the delete button so that you delete the selected row
     * @throws Exception
     */
    @FXML // WORKS!!!!!!
    private void handleDeleteButton() throws Exception {
        if (apptTable.getSelectionModel().getSelectedItem() != null) {
            Appointment appt = apptTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Required");
            alert.setHeaderText("CONFIRM");
            alert.setContentText("Are you sure you want to delete this Appt with the ID: " +
                    appt.getAppointmentID() + " and Type: " + appt.getType() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deleteAppointment(appt);
                setAppointmentsTable();
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText("Appt. Removed");
                alert2.setContentText("You have successfully deleted AppointmentID: " + appt.getAppointmentID() + ". Type: " + appt.getType());
                alert2.showAndWait();
            } else {
                System.out.println("delete canceled.");
            }
        } else {
            System.out.println("No appointment was selected...");
        }
    }
    /**
     * this is function adds functionality to the handleDeleteButton() class to test if you can or not based
     * on other appointments
     * @throws Exception
     */
    private void deleteAppointment(Appointment appointment) throws Exception {
        Appointment appt = appointment;
        try {
            PreparedStatement ps = JDBConnection.startConnection().prepareStatement("DELETE FROM " +
                    "WJ0767M.appointments WHERE appointments.Appointment_ID = " + appt.getAppointmentID() + " ;");
            System.out.println("Deleting appt with the id:  " + appt.getAppointmentID());

            int result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL ERROR FOR DELETE APPT");
        }
        setAppointmentsTable();
    }
    /**
     * this is function adds functionality to the add button so that you can go to the addUpdateView
     * @throws IOException
     */
    @FXML
    private void handleAddButton() throws IOException {
        thisID = maxID + 1;
        isUpdate = false;
        root = FXMLLoader.load(getClass().getResource("AppointmentsAddUpdView.fxml"));
        stage = (Stage) addButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * this is function adds functionality to the update button so that you can go to the addUpdateView
     *
     */
    @FXML
    private void handleUpdateButton() throws IOException {
        // thisID will be transferred
        isUpdate = true;
        // selectedAppointment will be transferred

        root = FXMLLoader.load(getClass().getResource("AppointmentsAddUpdView.fxml"));
        stage = (Stage) updateButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * this is function handles the week radio button
     * @throws Exception
     * @param event
     */
    @FXML // works
    private void handleWeekRadioButton(ActionEvent event) throws Exception {
        isWeekly = true;
        setAppointmentsTable();
    }
    /**
     * this is function handles the month radio button
     * @throws Exception
     * @param event
     */
    @FXML
    private void handleMonthRadioButton(ActionEvent event) throws Exception {
        isWeekly = false;
        setAppointmentsTable();
    }
    /**
     * this is function handles the month radio button
     * @param appointment
     */
    public void AppointmentsListener(Appointment appointment) {
        System.out.println("LISTENING TO THIS SELECTION");
        selectedAppointment = appointment;
        thisID = selectedAppointment.getAppointmentID();
    }
    /**
     * this is initializes the AppointmentsViewController
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("APPT VIEW STARTING");

        maxID = -1;
        thisID = -1;

        //Populate CustomerTable with values
        PropertyValueFactory<Appointment, Integer> apptIDFactory = new PropertyValueFactory<>("AppointmentID");
        PropertyValueFactory<Appointment, String> apptDescFactory = new PropertyValueFactory<>("Description");
        PropertyValueFactory<Appointment, String> apptLocationFactory = new PropertyValueFactory<>("Location");
        PropertyValueFactory<Appointment, String> apptStartFactory = new PropertyValueFactory<>("Start");
        PropertyValueFactory<Appointment, String> apptEndFactory = new PropertyValueFactory<>("End");
        PropertyValueFactory<Appointment, String> apptTitleFactory = new PropertyValueFactory<>("Title");
        PropertyValueFactory<Appointment, String> apptTypeFactory = new PropertyValueFactory<>("Type");
        PropertyValueFactory<Appointment, Integer> apptCustomerFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<Appointment, String> apptContactFactory = new PropertyValueFactory<>("Contact");

        apptIDColumn.setCellValueFactory(apptIDFactory);
        titleColumn.setCellValueFactory(apptTitleFactory);
        descColumn.setCellValueFactory(apptDescFactory);
        locationColumn.setCellValueFactory(apptLocationFactory);
        contactColumn.setCellValueFactory(apptContactFactory);
        typeColumn.setCellValueFactory(apptTypeFactory);
        startTimeColumn.setCellValueFactory(apptStartFactory);
        endTimeColumn.setCellValueFactory(apptEndFactory);
        customerIDColumn.setCellValueFactory(apptCustomerFactory);

        //setint togglegroup
        RadioButtonToggleGroup = new ToggleGroup();
        weekRadioButton.setToggleGroup(RadioButtonToggleGroup);
        monthRadioButton.setToggleGroup(RadioButtonToggleGroup);
        weekRadioButton.setSelected(true);
        monthRadioButton.setSelected(false);

        try {
            setAppointmentsTable();
            //System.out.println("max id Is " + maxID);
        } catch (SQLException ex) {
            System.out.println("SQL error when 'setAppointmentTable' was called.");
        }
        try {
            filterAppointmentsByMonth(appointmentsOL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        apptTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        AppointmentsListener(newValue);
                    } catch (Exception ex) {
                        System.out.println("CUSTOMER LISTENER ERROR");
                    }
                });
    }
}
