package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Author;
import model.AuthorDAO;
import java.sql.SQLException;

public class AuthorController {

  @FXML
  private TextField authIdText;
  @FXML
  private TextArea resultArea;
  @FXML
  private TextField nameText;
  @FXML
  private TextField surnameText;
  @FXML
  private TableView authorTable;
  @FXML
  private TableColumn<Author, Integer> authIdColumn;
  @FXML
  private TableColumn<Author, String> authNameColumn;
  @FXML
  private TableColumn<Author, String> authLastNameColumn;

  //Search an author
  @FXML
  private void searchAuthor(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
    try {
      //Get Author information
      Author author = AuthorDAO.searchAuthor(authIdText.getText());
      //Populate Author on TableView and Display on TextArea
      populateAndShowAuthor(author);
    } catch (SQLException e) {
      e.printStackTrace();
      resultArea.setText("Error occurred while getting author information from DB.\n" + e);
      throw e;
    }
  }

  //Search all authors
  @FXML
  private void searchAuthors(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    try {
      //Get all Author information
      ObservableList<Author> authData = AuthorDAO.searchAuthors();
      //Populate Authors on TableView
      populateAuthors(authData);
    } catch (SQLException e) {
      System.out.println("Error occurred while getting authors information from DB.\n" + e);
      throw e;
    }
  }

  //Initializing the controller class.
  //This method is automatically called after the fxml file has been loaded.
  @FXML
  private void initialize() {
        /*
        The setCellValueFactory(...) that we set on the table columns are used to determine
        which field inside the Author objects should be used for the particular column.
        The arrow -> indicates that we're using a Java 8 feature called Lambdas.
        (Another option would be to use a PropertyValueFactory, but this is not type-safe

        When you want to use IntegerProperty or DoubleProperty, the setCellValueFactory(...)
        must have an additional asObject():
        */
    authIdColumn
        .setCellValueFactory(cellData -> cellData.getValue().author_idProperty().asObject());
    authNameColumn.setCellValueFactory(cellData -> cellData.getValue().first_nameProperty());
    authLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().last_nameProperty());
  }

  //Populate Author
  @FXML
  private void populateAuthor(Author author) throws ClassNotFoundException {
    //Declare and ObservableList for table view
    ObservableList<Author> authData = FXCollections.observableArrayList();
    //Add employee to the ObservableList
    authData.add(author);
    //Set items to the employeeTable
    authorTable.setItems(authData);
  }

  //Set Author information to Text Area
  @FXML
  private void setAuthInfoToTextArea(Author author) {
    resultArea.setText("First Name: " + author.getFirstName() + "\n" +
        "Last Name: " + author.getLast_name());
  }

  //Populate Author for TableView and Display Author on TextArea
  @FXML
  private void populateAndShowAuthor(Author author) throws ClassNotFoundException {
    if (author != null) {
      populateAuthor(author);
      setAuthInfoToTextArea(author);
    } else {
      resultArea.setText("This author does not exist!\n");
    }
  }

  //Populate Authors for TableView
  @FXML
  private void populateAuthors(ObservableList<Author> authData) throws ClassNotFoundException {
    //Set items to the employeeTable
    authorTable.setItems(authData);
  }


  //Insert an author to the DB
  @FXML
  private void insertAuthor(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    try {
      AuthorDAO.insertAuth(nameText.getText(), surnameText.getText());
      resultArea.setText("Author inserted! \n");
      searchAuthors(new ActionEvent());
    } catch (SQLException e) {
      resultArea.setText("Problem occurred while inserting author " + e);
      throw e;
    }
  }

  //Delete an author with a given Id from DB
  @FXML
  private void deleteAuthor(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    try {
      AuthorDAO.deleteAuthWithId(authIdText.getText());
      resultArea.setText("Author deleted! Author id: " + authIdText.getText() + "\n");
      searchAuthors(new ActionEvent());
    } catch (SQLException e) {
      resultArea.setText(
          "Enter the id of the author to delete. \nProblem occurred while deleting author " + e);
      throw e;
    }
  }
}
