package MainAndViews;

import Models.Appointment;
import Models.Contacts;
import Models.Customers;
import Models.Reports;
import Utilities.JDBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 *
 *
 * @author calebbayles
 */

public class ReportsViewController implements Initializable {

    @FXML
    Button backButton;
    @FXML
    private void handleBackButton() throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }


    // contact appointments table
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

    ObservableList<Appointment> appointmentsOL = FXCollections.observableArrayList();

    // appointment types by month
    @FXML
    private TableView<Reports> typesByMonthTable;
    @FXML
    private TableColumn<Reports, String> monthColumn;
    @FXML
    private TableColumn<Reports, Integer> boardMeetingsColumn;
    @FXML
    private TableColumn<Reports, Integer> salesMeetingsColumn;
    @FXML
    private TableColumn<Reports, Integer> customerMeetupsColumn;
    @FXML
    private TableColumn<Reports, Integer> followUpColumn;

    private ObservableList<Reports> typesByMonthOL = FXCollections.observableArrayList();

    private int monthTypes[][] = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    // contacts info table

    @FXML
    private TableView<Contacts> contactsInfoTable;
    @FXML
    private TableColumn<Contacts, Integer> contactsInfoIDColumn;
    @FXML
    private TableColumn<Contacts, String> contactsInfoNameColumn;
    @FXML
    private TableColumn<Contacts, String> contactsInfoEmailColumn;

    private ObservableList<Contacts> contactsInfoOL = FXCollections.observableArrayList();

    Parent root;
    Stage stage;


    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");



    public void setAppointmentsTable() throws SQLException {
        System.out.println("UPDATING Appointments TABLE");

        try {
            Statement stmt = JDBConnection.conn.createStatement();
            String sqlStatement = ("SELECT * FROM WJ0767M.appointments, WJ0767M.customers  " +
                    "WHERE appointments.Customer_ID = customers.Customer_ID"
                    + " ORDER BY 'Start';");
            ResultSet result = stmt.executeQuery(sqlStatement);
            appointmentsOL.clear();
            while (result.next()) {
                int appointmentID = result.getInt("Appointment_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                int contactID = result.getInt("Contact_ID");
                String contact = Customers.getContactName(contactID);
                String type = result.getString("Type");
                int customerID = result.getInt("Customer_ID");
                int userID = result.getInt("User_ID");

                String startUTC = result.getString("Start").substring(0, 19);

                String endUTC = result.getString("End").substring(0, 19);

                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);

                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                appointmentsOL.add(new Appointment(appointmentID, title, description, location, contact, type, localStartDT, localEndDT, customerID, userID));
            }
            apptTable.setItems(appointmentsOL);
        } catch (SQLException sqe) {
            System.out.println("Update Appointment Table SQL error!");
        } catch (Exception e) {
            System.out.println("Something other than SQL has caused an error!");
        }

        System.out.println("UPDATE FINISHED");
    }
    // this works!!!!
    private void setContactsInfoTable() throws SQLException {
        System.out.println("UPDATING contacts info TABLE");
        try {
            Statement stmt = JDBConnection.conn.createStatement();
            String sqlStatement = ("SELECT * FROM WJ0767M.contacts;");
            ResultSet result = stmt.executeQuery(sqlStatement);
            contactsInfoOL.clear();
            while (result.next()) {
                int contactID = result.getInt("Contact_ID");
                String name = result.getString("Contact_Name");
                String email = result.getString("Email");
                contactsInfoOL.add(new Contacts(contactID, name, email));
            }
            contactsInfoTable.setItems(contactsInfoOL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
        System.out.println("Something other than SQL has caused an error!");
    }
    }


    // it works!!!!!!!
    private void setReportsTypeByMonthTable() throws SQLException, Exception {
        System.out.println("FILLING REPORTS TYPES WITH MONTH");
        PreparedStatement ps;
        try {
            ps = JDBConnection.startConnection().prepareStatement(
                    "SELECT * "
                            + "FROM WJ0767M.appointments");

            ResultSet rs = ps.executeQuery();
            typesByMonthOL.clear();
            while (rs.next()) {
                //get database start time stored as UTC
                String startUTC = rs.getString("Start").substring(0, 19);

                //get database end time stored as UTC
                String endUTC = rs.getString("End").substring(0, 19);

                //convert database UTC to LocalDateTime
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);

                //convert times UTC zoneId to local zoneId
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

                //convert ZonedDateTime to a string for insertion into AppointmentsTableView
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);

                String monthParse = localStartDT.substring(5, 7);
                int month = Integer.parseInt(monthParse);
                month = month - 1;
                String type = rs.getString("Type");

                //increment array values of each type for each month
                if (month == 0) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[0][0]++;
                        case "Sales Meeting" -> monthTypes[0][1]++;
                        case "Customer Meetup" -> monthTypes[0][2]++;
                        case "Follow Up" -> monthTypes[0][3]++;
                    }

                } else if (month == 1) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[1][0]++;
                        case "Sales Meeting" -> monthTypes[1][1]++;
                        case "Customer Meetup" -> monthTypes[1][2]++;
                        case "Follow Up" -> monthTypes[1][3]++;
                    }
                } else if (month == 2) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[2][0]++;
                        case "Sales Meeting" -> monthTypes[2][1]++;
                        case "Customer Meetup" -> monthTypes[2][2]++;
                        case "Follow Up" -> monthTypes[2][3]++;
                    }
                } else if (month == 3) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[3][0]++;
                        case "Sales Meeting" -> monthTypes[3][1]++;
                        case "Customer Meetup" -> monthTypes[3][2]++;
                        case "Follow Up" -> monthTypes[3][3]++;
                    }
                } else if (month == 4) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[4][0]++;
                        case "Sales Meeting" -> monthTypes[4][1]++;
                        case "Customer Meetup" -> monthTypes[4][2]++;
                        case "Follow Up" -> monthTypes[4][3]++;
                    }
                } else if (month == 5) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[5][0]++;
                        case "Sales Meeting" -> monthTypes[5][1]++;
                        case "Customer Meetup" -> monthTypes[5][2]++;
                        case "Follow Up" -> monthTypes[5][3]++;
                    }
                } else if (month == 6) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[6][0]++;
                        case "Sales Meeting" -> monthTypes[6][1]++;
                        case "Customer Meetup" -> monthTypes[6][2]++;
                        case "Follow Up" -> monthTypes[6][3]++;
                    }
                } else if (month == 7) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[7][0]++;
                        case "Sales Meeting" -> monthTypes[7][1]++;
                        case "Customer Meetup" -> monthTypes[7][2]++;
                        case "Follow Up" -> monthTypes[7][3]++;
                    }
                } else if (month == 8) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[8][0]++;
                        case "Sales Meeting" -> monthTypes[8][1]++;
                        case "Customer Meetup" -> monthTypes[8][2]++;
                        case "Follow Up" -> monthTypes[8][3]++;
                    }
                } else if (month == 9) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[9][0]++;
                        case "Sales Meeting" -> monthTypes[9][1]++;
                        case "Customer Meetup" -> monthTypes[9][2]++;
                        case "Follow Up" -> monthTypes[9][3]++;
                    }
                } else if (month == 10) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[10][0]++;
                        case "Sales Meeting" -> monthTypes[10][1]++;
                        case "Customer Meetup" -> monthTypes[10][2]++;
                        case "Follow Up" -> monthTypes[10][3]++;
                    }
                } else if (month == 11) {
                    switch (type) {
                        case "Board Meeting" -> monthTypes[11][0]++;
                        case "Sales Meeting" -> monthTypes[11][1]++;
                        case "Customer Meetup" -> monthTypes[11][2]++;
                        case "Follow Up" -> monthTypes[11][3]++;
                    }
                }
            }

        } catch (SQLException sqe) {
            System.out.println("Reports By Month has SQL error!");
        } catch (Exception e) {
            System.out.println("Something other than SQL has caused an error!");
        }
        for (int i = 0; i < 12; i++) {
            //assign variables for insertion into typesByMonthOL
            int boardMeeting = monthTypes[i][0];
            int salesMeeting = monthTypes[i][1];
            int customerMeetup = monthTypes[i][2];
            int followUp = monthTypes[i][3];


            typesByMonthOL.add(new Reports(getAbbreviatedMonth(i), boardMeeting, salesMeeting, followUp, customerMeetup));

        }
        System.out.println("ENDING REPORT FILL");
    }


    private String getAbbreviatedMonth(int month) {
        String abbreviatedMonth = null;
        if (month == 0) {
            abbreviatedMonth = "JAN";
        }
        if (month == 1) {
            abbreviatedMonth = "FEB";
        }
        if (month == 2) {
            abbreviatedMonth = "MAR";
        }
        if (month == 3) {
            abbreviatedMonth = "APR";
        }
        if (month == 4) {
            abbreviatedMonth = "MAY";
        }
        if (month == 5) {
            abbreviatedMonth = "JUN";
        }
        if (month == 6) {
            abbreviatedMonth = "JUL";
        }
        if (month == 7) {
            abbreviatedMonth = "AUG";
        }
        if (month == 8) {
            abbreviatedMonth = "SEP";
        }
        if (month == 9) {
            abbreviatedMonth = "OCT";
        }
        if (month == 10) {
            abbreviatedMonth = "NOV";
        }
        if (month == 11) {
            abbreviatedMonth = "DEC";
        }
        return abbreviatedMonth;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("STARTING REPORTS VIEW");

        //Populate Contact Schedule table with values
        PropertyValueFactory<Appointment, String> apptContactFactory = new PropertyValueFactory<>("Contact");
        PropertyValueFactory<Appointment, Integer> apptIDFactory = new PropertyValueFactory<>("AppointmentID");
        PropertyValueFactory<Appointment, String> apptTitleFactory = new PropertyValueFactory<>("Title");
        PropertyValueFactory<Appointment, String> apptDescFactory = new PropertyValueFactory<>("Description");
        PropertyValueFactory<Appointment, String> apptLocationFactory = new PropertyValueFactory<>("Location");
        PropertyValueFactory<Appointment, String> apptTypeFactory = new PropertyValueFactory<>("Type");
        PropertyValueFactory<Appointment, String> apptStartFactory = new PropertyValueFactory<>("Start");
        PropertyValueFactory<Appointment, String> apptEndFactory = new PropertyValueFactory<>("End");
        PropertyValueFactory<Appointment, Integer> apptCustomerFactory = new PropertyValueFactory<>("CustomerID");

        contactColumn.setCellValueFactory(apptContactFactory);
        apptIDColumn.setCellValueFactory(apptIDFactory);
        titleColumn.setCellValueFactory(apptTitleFactory);
        descColumn.setCellValueFactory(apptDescFactory);
        locationColumn.setCellValueFactory(apptLocationFactory);
        typeColumn.setCellValueFactory(apptTypeFactory);
        startTimeColumn.setCellValueFactory(apptStartFactory);
        endTimeColumn.setCellValueFactory(apptEndFactory);
        customerIDColumn.setCellValueFactory(apptCustomerFactory);

        try {
            setAppointmentsTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Populate Types By Month
        PropertyValueFactory<Reports, String> monthFactory = new PropertyValueFactory<>("Month");
        PropertyValueFactory<Reports, Integer> boardMeetingsFactory = new PropertyValueFactory<>("BoardMeetings");
        PropertyValueFactory<Reports, Integer> salesMeetingsFactory = new PropertyValueFactory<>("SalesMeetings");
        PropertyValueFactory<Reports, Integer> followUpFactory = new PropertyValueFactory<>("FollowUps");
        PropertyValueFactory<Reports, Integer> customerMeetupsFactory = new PropertyValueFactory<>("CustomerMeetups");

        //assign cell values to Types By Month
        monthColumn.setCellValueFactory(monthFactory);
        boardMeetingsColumn.setCellValueFactory(boardMeetingsFactory);
        salesMeetingsColumn.setCellValueFactory(salesMeetingsFactory);
        followUpColumn.setCellValueFactory(followUpFactory);
        customerMeetupsColumn.setCellValueFactory(customerMeetupsFactory);
        try {
            setReportsTypeByMonthTable();
        } catch (Exception ex) {
            System.out.println("THERE WAS A PROBLEM WITH REPORTS TYPES BY MONTH");
        }
        typesByMonthTable.setItems(typesByMonthOL);

        // Contact info table
        PropertyValueFactory<Contacts, Integer> contactIDFactory = new PropertyValueFactory<>("ContactID");
        PropertyValueFactory<Contacts, String> contactNameFactory = new PropertyValueFactory<>("ContactName");
        PropertyValueFactory<Contacts, String> contactEmailFactory = new PropertyValueFactory<>("ContactEmail");
        contactsInfoIDColumn.setCellValueFactory(contactIDFactory);
        contactsInfoNameColumn.setCellValueFactory(contactNameFactory);
        contactsInfoEmailColumn.setCellValueFactory(contactEmailFactory);
        try {
            setContactsInfoTable();
        } catch (Exception ex) {
            System.out.println("THERE WAS A PROBLEM WITH CONTACTS INFO TABLE");
        }


    }
}
