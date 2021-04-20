//FINISHED!!!!!
package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * JDBConn... Model: this is used to functionality to connect to the db.
 *
 * @author calebbayles
 */
public class JDBConnection {
    public static boolean isConnected = false;
    // JDBC URL PARTS
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "HIDDEN";
    // concat to make url
    private static final String JDBCURL = protocol + vendorName + ipAddress;
    // driver interface reference / connection
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    public static Connection conn = null;
    // declare username
    private static final String username = "HIDDEN";
    // password
    private static final String password = "HIDDEN";
    /**
     * This is used to start the connection to the db
     * @return the connection object
     */
    public static Connection startConnection() {
        try{
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(JDBCURL, username, password);
            System.out.println("Connecting to DB...");
            isConnected = true;
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
    /**
     * This is used to cleanly close the connection to the db and display a message
     *
     */
    public static void closeConnection() 
    {
        try{
            conn.close();
            System.out.println("Connection closed...");
        }
        catch(SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
