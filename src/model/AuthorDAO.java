package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDAO {

  //*******************************
  //SELECT an Author
  //*******************************
  public static Author searchAuthor(String authId) throws SQLException, ClassNotFoundException {
    //Declare a SELECT statement
    String selectStmt = "SELECT * FROM AUTHORS WHERE AUTHORID=" + authId;

    //Execute SELECT statement
    try {
      //Get ResultSet from dbExecuteQuery method
      ResultSet rsAuth = DBUtil.dbExecuteQuery(selectStmt);

      //Send ResultSet to the getAuthorFromResultSet method and get author object
      Author author = getAuthorFromResultSet(rsAuth);

      //Return employee object
      return author;
    } catch (SQLException e) {
      System.out
          .println("While searching an author with " + authId + " id, an error occurred: " + e);
      //Return exception
      throw e;
    }
  }

  //Use ResultSet from DB as parameter and set Author Object's attributes and return author object.
  private static Author getAuthorFromResultSet(ResultSet rs) throws SQLException {
    Author author = null;
    if (rs.next()) {
      author = new Author();
      author.setAuthorId(rs.getInt("AUTHORID"));
      author.setFirstName(rs.getString("FIRSTNAME"));
      author.setLastName(rs.getString("LASTNAME"));
    }
    return author;
  }

  //*******************************
  //SELECT Authors
  //*******************************
  public static ObservableList<Author> searchAuthors() throws SQLException, ClassNotFoundException {
    //Declare a SELECT statement
    String selectStmt = "SELECT * FROM AUTHORS";

    //Execute SELECT statement
    try {
      //Get ResultSet from dbExecuteQuery method
      ResultSet rsAuth = DBUtil.dbExecuteQuery(selectStmt);

      //Send ResultSet to the getEmployeeList method and get employee object
      ObservableList<Author> authList = getAuthorList(rsAuth);

      //Return employee object
      return authList;
    } catch (SQLException e) {
      System.out.println("SQL select operation has been failed: " + e);
      //Return exception
      throw e;
    }
  }

  //Select * from authors operation
  private static ObservableList<Author> getAuthorList(ResultSet rs)
      throws SQLException, ClassNotFoundException {
    //Declare a observable List which comprises of Employee objects
    ObservableList<Author> authList = FXCollections.observableArrayList();

    while (rs.next()) {
      Author author = new Author();
      author.setAuthorId(rs.getInt("AUTHORID"));
      author.setFirstName(rs.getString("FIRSTNAME"));
      author.setLastName(rs.getString("LASTNAME"));

      //Add employee to the ObservableList
      authList.add(author);
    }
    //return empList (ObservableList of Employees)
    return authList;
  }

  //*************************************
  //DELETE an author
  //*************************************
  public static void deleteAuthWithId(String authId) throws SQLException, ClassNotFoundException {
    //Declare a DELETE statement
    String updateStmt =
        "DELETE FROM AUTHORS " +
            "WHERE AUTHORID =" + authId;

    //Execute UPDATE operation
    try {
      DBUtil.dbExecuteUpdate(updateStmt);
    } catch (SQLException e) {
      System.out.print("Error occurred while DELETE Operation: " + e);
      throw e;
    }
  }

  //*************************************
  //INSERT an author
  //*************************************
  public static void insertAuth(String name, String lastname)
      throws SQLException, ClassNotFoundException {
    //Declare a DELETE statement
    String updateStmt =
        "INSERT INTO AUTHORS " +
            "(FIRSTNAME, LASTNAME) " +
            "VALUES " +
            "('" + name + "', '" + lastname + "')";

    //Execute DELETE operation
    try {
      DBUtil.dbExecuteUpdate(updateStmt);
    } catch (SQLException e) {
      System.out.print("Error occurred while DELETE Operation: " + e);
      throw e;
    }
  }
}