//FINISHED!!!!!
package Models;

import Utilities.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Customers Model: this is used to add customer object functionality
 *
 * @author calebbayles
 */
public class Customers {
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerCountry;
    private String customerFirstLevelDiv;
    private String customerPhone;
    private Date customerCreateDate;
    private String customerCreatedBy;
    private Timestamp customerLastUpdated;
    private String customerLastUpdatedBy;
    private int customerDivisionID;

    /**
     * constructor for Customers
     */
    public Customers(int custID, String custName, String custAddress, 
            String custPostalCode,  String custCountry, String custPhone, Date custCreateDate,
            String custCreatedBy, Timestamp custLastUpdated, String custLastUpdatedBy, String theCustomerFirstLevelDiv, int custDivisionID){
        this.customerID = custID;
        this.customerName = custName;
        this.customerAddress = custAddress;
        this.customerPostalCode = custPostalCode;
        this.customerCountry = custCountry;
        this.customerPhone = custPhone;
        this.customerCreateDate = custCreateDate;
        this.customerCreatedBy = custCreatedBy;
        this.customerLastUpdated = custLastUpdated;
        this.customerLastUpdatedBy = custLastUpdatedBy;
        this.customerFirstLevelDiv = theCustomerFirstLevelDiv;
        this.customerDivisionID = custDivisionID;
    }
    /**
     * another constructor for customers
     */
    public Customers(int customerID, String customerName) {
        setCustomerID(customerID); //this is Auto Incremented in the database
        setCustomerName(customerName);
    }
    /**
     * another constructor for customers
     */
    public Customers() {

    }


    //Getters
    /**
     * This is used to get the FLD for the Customer
     * @return customerFirstLevelDivision
     */
    public String getCustFirstLevelDiv(){
        return customerFirstLevelDiv;
    }
    /**
     * This is used to get the ID for the Customer
     * @return customer ID
     */
    public int getCustID(){
        return customerID;
    }
    /**
     * This is used to get the name for the Customer
     * @return customerName
     */
    public String getCustName(){
        return customerName;
    }
    /**
     * This is used to get the address for the Customer
     * @return customerAddress
     */
    public String getCustAddress(){
        return customerAddress;
    }
    /**
     * This is used to get the PostalCode for the Customer
     * @return customerPostalCode
     */
    public String getCustPostalCode(){
        return customerPostalCode;
    }
    /**
     * This is used to get the country for the Customer
     * @return customerCountry
     */
    public String getCustCountry(){
        return customerCountry;
    }
    /**
     * This is used to get the phone for the Customer
     * @return customerPhone
     */
    public String getCustPhone(){
        return customerPhone;
    }
    /**
     * This is used to get the create date for the Customer
     * @return customerCreate date
     */
    public Date getCustCreateDate(){
        return customerCreateDate;
    }
    /**
     * This is used to get who last updated the Customer
     * @return who last updated the Customer
     */
    public Timestamp getCustLastUpdated(){
        return customerLastUpdated;
    }
    /**
     * This is used to get the div id for the Customer
     * @return division ID
     */
    public int getCustDivisionID(){
        return customerDivisionID;
    }
    
    //setters complete
    /**
     * This is used to set the FLD for the Customer
     * @param theCustomerFirstLevelDiv
     */
    public void setCustomerFirstLevelDiv(String theCustomerFirstLevelDiv){
        this.customerFirstLevelDiv = theCustomerFirstLevelDiv;
    }
    /**
     * This is used to set the id for the Customer
     * @param customerID
     */
    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }
    /**
     * This is used to find the name for the contact
     * @param Id
     * @return contact name
     */
    public static String getContactName(int Id) throws SQLException{
            String theName = "Name not found";

            Statement stmt = JDBConnection.conn.createStatement();
            String sqlStatement = "SELECT * FROM WJ0767M.contacts WHERE Contact_ID = " + Id + ";";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                theName = result.getString("Contact_Name");
            }
            return theName;
    }
    /**
     * This is used to Find the contact id
     * @param contactName
     * @return the Contact id
     */
    public static int findContactID(String contactName) throws SQLException{
        int ContactID = -1;
        Statement stmt = JDBConnection.conn.createStatement();
        String sqlStatement = "SELECT * FROM WJ0767M.contacts WHERE Contact_Name = \"" + contactName + "\";";
        ResultSet result = stmt.executeQuery(sqlStatement);

        while (result.next()) {
            ContactID = result.getInt("Contact_ID");
        }
        return ContactID;
    }
    /**
     * This is used to set the name for the Customer
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /**
     * This is used to set the address for the Customer
     * @param address
     */
    public void setCustomerAddress(String address) {
        this.customerAddress = address;
    }
    /**
     * This is used to set the postal code for the Customer
     * @param postalCode
     */
    public void setCustomerPostalCode(String postalCode) {
        this.customerPostalCode = postalCode;
    }
    /**
     * This is used to set the phone for the Customer
     * @param phone
     */
    public void setCustomerPhone(String phone) {
        this.customerPhone = phone;
    }
    /**
     * This is used to set the country for the Customer
     * @param country
     */
    public void setCustomerCountry(String country) {
        this.customerCountry = country;
    }
    /**
     * This is used to set the lastUpdated time for the Customer
     * @param lastUpdate
     */
    public void setCustomerLastUpdated(Timestamp lastUpdate) {
        this.customerLastUpdated = lastUpdate;
    }
    /**
     * This is used to set the divison id for the Customer
     * @param divisionID
     */
    public void setCustomerDivisionID(int divisionID){
        this.customerDivisionID = divisionID;
    }

    
}
