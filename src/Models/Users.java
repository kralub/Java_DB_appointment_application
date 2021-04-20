// FINISHED!!!!!
package Models;

/**
 * Users Model: this is used to add Users object functionality
 *
 * @author calebbayles
 */
public class Users {
    private  int User_ID;
    private  String User_Name;
    private  String Password;
    /**
     * constructor for Users
     */
    public Users() {
        User_ID = 0;
        User_Name = null;
        Password = null;
    }
    /**
     * another constructor for users
     */
    public Users(int theUser_ID, String theUser_Name, String thePassword){
        this.User_ID = theUser_ID;
        this.User_Name = theUser_Name;
        this.Password = thePassword;
    }
    
    //getters
    /**
     * This is used to get the user id for the object
     * @return the user id
     */
    public  int getUserID(){
        return this.User_ID;
    }
    /**
     * This is used to get the user name for the object
     * @return the user name
     */
    public  String getUserName(){
        return this.User_Name;
    }
    /**
     * This is used to get the user password for the object
     * @return the user id
     */
    public  String getPassword(){
        return this.Password;
    }
    //setters
    /**
     * This is used to set the user id for the object
     * @param ID
     */
    public  void setUserID(int ID){
        this.User_ID = ID;
    }
    /**
     * This is used to set the user name for the object
     * @param name
     */
    public  void setUserName(String name){
        this.User_Name = name;
    }
    /**
     * This is used to set the user password for the object
     * @param password
     */
    public  void setUserPassword(String password){
        this.Password = password;
    }
}
