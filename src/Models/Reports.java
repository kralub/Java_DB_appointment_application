// finished!!!!!
package Models;

/**
 * Reports Model: this is used to add Reports object functionality
 *
 * @author calebbayles
 */
public class Reports {

    private String month;
    private int boardMeetings;
    private int salesMeetings;
    private int customerMeetups;
    private int followUps;
    private int typesArray[];

    /**
     * constructor for Reports
     */
    public Reports(String month, int typesArray[]) {
        setMonth(month);
        setTypesArray(typesArray);
    }
    /**
     * another constructor for reports
     */
    public Reports(String month, int boardMeetings, int salesMeetings, int followUps, int customerMeetups){
        setMonth(month);
        setBoardMeetings(boardMeetings);
        setSalesMeetings(salesMeetings);
        setCustomerMeetups(customerMeetups);
        setFollowUps(followUps);
    }
    
    //getters
    /**
     * This is used to get the month for the object
     * @return the month for the appt
     */
    public String getMonth() {
        return this.month;
    }
    /**
     * This is used to get the types for the object
     * @return the array for the types by month
     */
    public int[] getTypesArray() {
        return this.typesArray;
    }
    /**
     * This is used to get the amnt of board meetings for the object
     * @return board meetings amnt
     */
    public int getBoardMeetings(){
        return this.boardMeetings;
    }
    /**
     * This is used to get the sales meeting amt for the object
     * @return the sales meetings amnt
     */
    public int getSalesMeetings(){
        return this.salesMeetings;
    }
    /**
     * This is used to get the customer meetups amnt for the object
     * @return the amnt of customer meetups
     */
    public int getCustomerMeetups(){
        return this.customerMeetups;
    }
    /**
     * This is used to get the follow up meetings  for the object
     * @return the follow up amnt
     */
    public int getFollowUps(){
        return this.followUps;
    }


    //setters
    /**
     * This is used to set the month for the object
     * @param month
     */
    private void setMonth(String month) {
        this.month = month;
    }
    /**
     * This is used to set the board meeting amnt for the object
     * @param boardMeetings amnt
     */
    private void setBoardMeetings(int boardMeetings){
        this.boardMeetings = boardMeetings;
    }
    /**
     * This is used to set the sales meetings for the object
     * @param salesMeetings amnt
     */
    private void setSalesMeetings(int salesMeetings){
        this.salesMeetings = salesMeetings;
    }
    /**
     * This is used to set the customer meetups amnt for the object
     * @param customerMeetups amnt
     */
    private void setCustomerMeetups(int customerMeetups){
        this.customerMeetups = customerMeetups;
    }
    /**
     * This is used to set the follow ups for the object
     * @param followUps amnt
     */
    private void setFollowUps(int followUps){
        this.followUps = followUps;
    }
    /**
     * This is used to set the types array for the object
     * @param typesArray
     */
    private void setTypesArray(int[] typesArray) {
        this.typesArray = typesArray;
    }
}