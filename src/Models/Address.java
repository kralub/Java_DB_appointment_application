// FINISHED!!!
package Models;

import Utilities.JDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Address Model: this is used to add address object functionality
 *
 * @author calebbayles
 */
public class Address {
    private int divisionID = 0;//auto generated
    private String address;
    private String postalCode;

    /**
     * constructor for Address
     */
    public Address(int addressID, String address, String postalCode){
        setDivisionID(addressID);//auto generated
        setAddress(address);
        setPostalCode(postalCode);
    }
    /**
     * This is used to find the division NAME in the database
     * so that it can be used to populate the CustomerAddUpdController combo boxes
     *
     * @return the division Name in string form
     * @param division_ID
     */
    public static String findDivision(int division_ID) throws SQLException{
        String theDivision = "div not found";
        
        Statement stmt = JDBConnection.conn.createStatement();
        String sqlStatement = "SELECT * FROM WJ0767M.first_level_divisions WHERE Division_ID = " + division_ID + ";";
        
        
        //execute statement and create resultset object
        ResultSet result = stmt.executeQuery(sqlStatement);
        
        //get all records from resultset object
        while (result.next()) {
            theDivision = result.getString("Division");
        }
        return theDivision;
    }
    /**
     * This is used to find the division ID in the database
     * so that it can be used to populate the CustomerAddUpdController combo boxes
     *
     * @return the division ID in int form
     * @param division
     */
    public static int findDivisionID(String division) throws SQLException{
        int theDivisionID = -1;
        division = "\"" + division + "\"";
        Statement stmt = JDBConnection.conn.createStatement();
        String sqlStatement = "SELECT * FROM WJ0767M.first_level_divisions WHERE Division = " + division + ";";
        
        //execute statement and create resultset object
        ResultSet result = stmt.executeQuery(sqlStatement);
        
        //get all records from resultset object
        while (result.next()) {
            theDivisionID = result.getInt("Division_ID");
        }
        return theDivisionID;
    }
    /**
     * This is used to find the country ID in the database
     * so that it can be used to populate the CustomerAddUpdController combo boxes
     *
     * @return the country id in int form
     * @param country
     */
    public static int findCountryID(String country) throws SQLException{
        country = "\"" + country + "\"";
        int countryID = -1;
        Statement stmt = JDBConnection.conn.createStatement();
        String sqlStatement = "SELECT * FROM WJ0767M.countries WHERE Country = " + country + ";";
        
        //execute statement and create resultset object
        ResultSet result = stmt.executeQuery(sqlStatement);
        
        //get all records from resultset object
        while (result.next()) {
            countryID = result.getInt("Country_ID");
            break;
        }
        return countryID;
    }
    /**
     * This is used to find the country NAME in the database
     * so that it can be used to populate the CustomerAddUpdController combo boxes
     *
     * @return the country Name in string form
     * @param division_ID
     */
    public static String findCountry(int division_ID) throws SQLException{
        String theCountry = "country not found";
        Statement stmt = JDBConnection.conn.createStatement();
        String sqlStatement = "SELECT Country_ID FROM WJ0767M.first_level_divisions WHERE Division_ID = " + division_ID + ";";

        //execute statement and create resultset object
        ResultSet result = stmt.executeQuery(sqlStatement);
        
        int theCountryID = -1;
        //get all records from resultset object
        while (result.next()) {
            theCountryID = result.getInt("Country_ID");
        }
        
        Statement stmt2 = JDBConnection.conn.createStatement();
        String sqlStatement2 = "SELECT * FROM WJ0767M.countries WHERE Country_ID = " + theCountryID + ";";
        
        //execute statement and create resultset object
        ResultSet result2 = stmt2.executeQuery(sqlStatement2);
        
        while (result2.next()) {
            theCountry = result2.getString("Country");
        }
        
        return theCountry;
    }

    /**
     * This is used to get the division id from the object
     * @return the division id in int form
     */
    public int getDivisionID(){
        return this.divisionID;
    }
    /**
     * This is used to get the address from the object
     * @return the customer address
     */
    public String getAddress(){
        return this.address;
    }
    /**
     * This is used to get the postal code from the object
     * @return the postal code in string form
     */
    public String getPostalCode(){
        return this.postalCode;
    }

    /**
     * This is used to set the division id from the object
     * @param divisionID
     */
    private void setDivisionID(int divisionID){
        this.divisionID = divisionID;//auto generated
    }
    /**
     * This is used to set the address from the object
     * @param address
     */
    private void setAddress(String address){
        this.address = address;
    }
    /**
     * This is used to set the postal code from the object
     * @param postalCode
     */
    private void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

}