// FINISHED!!!!

package Models;

/**
 * Appointment Model: this is used to add appointment object functionality
 *
 * @author calebbayles
 */
public class Appointment {
    
    private int appointmentID;
    private int customerID;
    private int userID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String startTime;
    private String endTime;
    /**
     * constructor for Appointment
     */
    public Appointment(int appointmentID,  String title, String description, String location, String contact,
                       String type, String startTime, String endTime, int customerID, int userID){
        setAppointmentID(appointmentID);
        setCustomerID(customerID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setStart(startTime);
        setEnd(endTime);
        setUserID(userID);
    }
    /**
     *  blank constructor for appointment
     */
    public Appointment(){
    }
        
    //getters
    /**
     * This is used to get the appoinment ID from the object
     * @return the appointment ID
     */
    public int getAppointmentID(){
        return this.appointmentID;
    }
    /**
     * This is used to get the Customer ID from the object
     * @return the customer ID
     */
    public int getCustomerID(){
        return this.customerID;
    }
    /**
     * This is used to get the USER ID from the object
     * @return the USER ID
     */
    public int getUserID(){
        return this.userID;
    }
    /**
     * This is used to get the TITLE from the object
     * @return the Title
     */
    public String getTitle(){
        return this.title;
    }
    /**
     * This is used to get the Description from the object
     * @return the Appt. description
     */
    public String getDescription(){
        return this.description;
    }
    /**
     * This is used to get the location from the object
     * @return the appt location
     */
    public String getLocation(){
        return this.location;
    }
    /**
     * This is used to get the contact from the object
     * @return the Appt.  contact name
     */
    public String getContact(){
        return this.contact;
    }
    /**
     * This is used to get the Type from the object
     * @return the Appt. Type
     */
    public String getType(){
        return this.type;
    }
    /**
     * This is used to get the start time from the object
     * @return the Appt. start time in timestamp string form
     */
    public String getStart(){        
        return this.startTime;
    }
    /**
     * This is used to get the end time from the object
     * @return the Appt. end time in timestamp string form
     */
    public String getEnd(){       
        return this.endTime;
    }

    //setters
    /**
     * This is used to set the ApptID for the object
     * @param appointmentID the appointment id
     */
    private void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }
    /**
     * This is used to set the customer ID for the object
     * @param customerID
     */
    public void setCustomerID(int customerID){
        this.customerID = customerID;
    }
    /**
     * This is used to set the user ID for the object
     * @param userID
     */
    public void setUserID(int userID){
        this.userID = userID;
    }
    /**
     * This is used to set the title for the object
     * @param title
     */
    private void setTitle(String title){
        this.title = title;
    }
    /**
     * This is used to set the description for the object
     * @param description
     */
    private void setDescription(String description){
        this.description = description;
    }
    /**
     * This is used to set the location for the object
     * @param location
     */
    private void setLocation(String location){
        this.location = location;
    }
    /**
     * This is used to set the contact name for the object
     * @param contact
     */
    public void setContact(String contact){
        this.contact = contact;
    }
    /**
     * This is used to set the type for the object
     * @param type
     */
    private void setType(String type){
        this.type = type;
    }
    /**
     * This is used to set the start time for the object in timestamp form
     * @param startTime
     */
    private void setStart(String startTime){
        this.startTime = startTime;
    }
    /**
     * This is used to set the end time for the object in timestamp form
     * @param endTime
     */
    private void setEnd(String endTime){
        this.endTime = endTime;
    }

}