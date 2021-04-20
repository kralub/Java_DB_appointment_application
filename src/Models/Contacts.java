// FINISHED!!!!
package Models;
/**
 * contacts Model: this is used to add contact object functionality for reports
 *
 * @author calebbayles
 */
public class Contacts {
    private int contactID;
    private String contactName;
    private String contactEmail;
    /**
     * constructor for Contacts
     */
    public Contacts(int contactID, String contactName, String contactEmail){
        setContactID(contactID);
        setContactName(contactName);
        setContactEmail(contactEmail);
    }

    // getters
    /**
     * This is used to get the contact ID for the object
     * @return contactID the contacts id
     */
    public int getContactID(){
        return this.contactID;
    }
    /**
     * This is used to get the contact ID for the object
     * @return contactName the contacts name
     */
    public String getContactName(){
        return this.contactName;
    }
    /**
     * This is used to get the contacts email for the object
     * @return contactEmail
     */
    public String getContactEmail(){
        return this.contactEmail;
    }

    // setters
    /**
     * This is used to set the contact ID for the object
     * @param contactID the contact id
     */
    public void setContactID(int contactID){
        this.contactID = contactID;
    }
    /**
     * This is used to set the contact name for the object
     * @param contactName the contacts name
     */
    public void setContactName(String contactName){
        this.contactName = contactName;
    }
    /**
     * This is used to set the contact email for the object
     * @param contactEmail the contact's email
     */
    public void setContactEmail(String contactEmail){
        this.contactEmail = contactEmail;
    }


}
