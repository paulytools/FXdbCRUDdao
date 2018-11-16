package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

  private static int recordNumber = 1;
  final String DATABASE_URL = "jdbc:derby:lib\\books";

  @FXML
  private Label lblFirst;

  @FXML
  private Label lblLast;

  @FXML
  private TextField txtFirst;

  @FXML
  private TextField txtLast;

  @FXML
  void createRecord(ActionEvent event) {
    final String INSERT_QUERY =
        "INSERT INTO AUTHORS(firstName, lastName) "
            + "VALUES ('" + txtFirst.getText() +
            "', '" + txtLast.getText() + "')";
    connectAndExecute(INSERT_QUERY);
  }

  @FXML
  void deleteRecord(ActionEvent event) {
    final String DELETE_QUERY =
        "DELETE FROM AUTHORS WHERE FIRSTNAME = '" + lblFirst.getText() + "' AND LASTNAME = '" +
            lblLast.getText() + "'";
    connectAndExecute(DELETE_QUERY);
  }

  @FXML
  void nextRecord(ActionEvent event) {
    final String SELECT_QUERY =
        "SELECT authorID, firstName, lastName FROM authors";
    recordNumber++;
    // use try-with-resources to connect to and query the database
    try (
        Connection connection = DriverManager.getConnection(
            DATABASE_URL, "deitel", "deitel");
        Statement statement = connection
            .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY)) {
      if (resultSet.next()) {
        resultSet.absolute(recordNumber);
      }

      if (resultSet.next()) {
        lblFirst.setText(resultSet.getString(2));
        lblLast.setText(resultSet.getString(3));
      } else {
        resultSet.first();
        recordNumber = 1;
        lblFirst.setText(resultSet.getString(2));
        lblLast.setText(resultSet.getString(3));
      }
    } // AutoCloseable objects' close methods are called now
    catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  @FXML
  void updateRecord(ActionEvent event) {
    final String UPDATE_QUERY =
        "UPDATE AUTHORS SET FIRSTNAME = '" + txtFirst.getText() + "', LASTNAME = '" +
            txtLast.getText() + "' WHERE FIRSTNAME = '" + lblFirst.getText() + "' AND LASTNAME = '"
            + lblLast.getText() + "'";
    connectAndExecute(UPDATE_QUERY);
  }

  void connectAndExecute(String query) {
    // use try-with-resources to connect to and query the database
    try {
      Connection connection = DriverManager.getConnection(
          DATABASE_URL, "deitel", "deitel");
      Statement statement = connection.createStatement();
      statement.execute(query);
    }
    // AutoCloseable objects' close methods are called now
    catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

}
