package util;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

/**
 * Prof. Vanselow
 * Modified code from ONUR BASKIRT.
 * https://www.swtestacademy.com/database-operations-javafx/
 */
public class DBUtil {
  //Declare JDBC Driver
  //private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

  //Connection
  private static Connection connection = null;

  final static String DATABASE_URL = "jdbc:derby:lib\\books";

  //Connect to DB
  public static void dbConnect() throws SQLException, ClassNotFoundException {

    //Establish the Oracle Connection using Connection String
    try {
      connection = DriverManager.getConnection(
          DATABASE_URL, "deitel", "deitel");
    } catch (SQLException e) {
      System.out.println("Connection Failed! Check output console" + e);
      e.printStackTrace();
      throw e;
    }
  }

  //Close Connection
  public static void dbDisconnect() throws SQLException {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (Exception e) {
      throw e;
    }
  }

  //DB Execute Query Operation
  public static ResultSet dbExecuteQuery(String queryStmt)
      throws SQLException, ClassNotFoundException {
    //Declare statement, resultSet and CachedResultSet as null
    Statement stmt = null;
    ResultSet resultSet = null;
    CachedRowSetImpl crs = null;
    try {
      //Connect to DB
      dbConnect();
      System.out.println("Select statement: " + queryStmt + "\n");

      //Create statement
      stmt = connection.createStatement();

      //Execute select (query) operation
      resultSet = stmt.executeQuery(queryStmt);

      //CachedRowSet Implementation
      //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
      //We are using CachedRowSet
      crs = new CachedRowSetImpl();
      crs.populate(resultSet);
    } catch (SQLException e) {
      System.out.println("Problem occurred at executeQuery operation : " + e);
      throw e;
    } finally {
      if (resultSet != null) {
        //Close resultSet
        resultSet.close();
      }
      if (stmt != null) {
        //Close Statement
        stmt.close();
      }
      //Close connection
      dbDisconnect();
    }
    //Return CachedRowSet
    return crs;
  }

  //DB Execute Update (For Update/Insert/Delete) Operation
  public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
    //Declare statement as null
    Statement stmt = null;
    try {
      //Connect to DB (Establish Oracle Connection)
      dbConnect();
      //Create Statement
      stmt = connection.createStatement();
      //Run executeUpdate operation with given sql statement
      stmt.executeUpdate(sqlStmt);
    } catch (SQLException e) {
      System.out.println("Problem occurred at executeUpdate operation : " + e);
      throw e;
    } finally {
      if (stmt != null) {
        //Close statement
        stmt.close();
      }
      //Close connection
      dbDisconnect();
    }
  }
}