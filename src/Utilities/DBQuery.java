// FINISHED!!!!!
package Utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBQuery Model: this is used to use statements for the db
 *
 * @author calebbayles
 */
public class DBQuery {
    /**
     * This is used to create a statement object
     *
     */
    private static Statement statement;

    /**
     * This is used to set the statement with the connection
     * @param conn
     *
     */
    public static void setStatement(Connection conn) throws SQLException{
        statement = conn.createStatement();
        
    }
    /**
     * This is used to return the statement
     * @return the statement
     */
    public static Statement getStatement(){
        return statement;
    }
}
