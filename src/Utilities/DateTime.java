// finished!!!!!
package Utilities;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DateTime Model: this is used to add time object functionality
 *
 * @author calebbayles
 */
public class DateTime {
    /**
     * This is used to get the time stamp for a date time
     * @return the timestamp for a date
     */
    public static java.sql.Timestamp getTimeStamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        java.sql.Timestamp timeStamp = Timestamp.valueOf(localDateTime);
        return timeStamp;
    }
    /**
     * This is used to set the current date for the object
     * @return the date
     */
    public static java.sql.Date getDate() {
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
        return date;
    }
}