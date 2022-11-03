package com.example.c482project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *This class controls the Add Product UI
 * @author Sophie Dang
 */
public class AddProductController implements Initializable {
    String empFieldsErrMessage = "";
    String invalidInputMessage = "";
    private final ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    @FXML private TextField addProductNameTextField;
    @FXML private TextField addProductInvTextField;
    @FXML private TextField addProductPriceTextField;
    @FXML private TextField addProductMaxTextField;
    @FXML private TextField addProductMinTextField;
    @FXML private TextField addProductSearchTextField;
    @FXML private TableView<Part> productAddPartsTable;
    @FXML private TableColumn<Part, Integer> addTablePartIDCol;
    @FXML private TableColumn<Part, String> addTablePartNameCol;
    @FXML private TableColumn<Part, Integer> addTableInvLevelCol;
    @FXML private TableColumn<Part, Double> addTablePriceCol;
    @FXML private TableView<Part> productAssociatedPartsTable;
    @FXML private TableColumn<Part, Integer> associatedPartIDCol;
    @FXML private TableColumn<Part, String> associatedPartNameCol;
    @FXML private TableColumn<Part, Integer> associatedPartInvCol;
    @FXML private TableColumn<Part, Double> associatedPartPriceCol;

    public AddProductController() {
    }

    /**
     * This method controls the search text field.
     * This method allows the user to search for a part by id or name.
     * Searches aren't case-sensitive.
     * The user is able to do partial searches, meaning they don't have to enter the full name of a part.
     * @param actionEvent search product by name or id.
     */
    @FXML
    public void addProductSearchFieldClicked(ActionEvent actionEvent) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        String searchProductString = addProductSearchTextField.getText();

        for (Part part : Inventory.getAllParts()) {
            if (String.valueOf(part.getId()).contains(searchProductString) ||
                    part.getName().toLowerCase().contains(searchProductString) ||
                    part.getName().toUpperCase().contains(searchProductString) ||
                    part.getName().regionMatches(true,0,searchProductString,0,1)){
                foundParts.add(part);
            }
        }
        productAddPartsTable.setItems(foundParts);

        if(foundParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please ensure that all fields are complete and valid values are entered.");
            alert.showAndWait();
    }
}
    /**
     *This method sets the parts table to display all parts when the search field is empty.
     * @param keyEvent key event
     */
    @FXML public void addProductSearchBarClicked(KeyEvent keyEvent){
        if(addProductSearchTextField.getText().isEmpty()){
            productAddPartsTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * This method handles the save button.
     * This method adds the new product to the inventory.
     * If the addition is done successfully, then it takes the user back to the main form.
     * This method will display alert messages if any text field is empty or contains invalid values.
     * @param actionEvent
     *
     * RUNTIME ERROR: the function saved the wrong info for max and min, the value saved for max was switched with min and vice verse.
     * This was because I had the max and min text fields in the wrong places, so I switched the text fields and the info was saved correctly.
     */
    @FXML public void addProductSaveButtonClicked(ActionEvent actionEvent) {
        int productToAddId = 0;
        //Retrieve inputs from text fields
        String name = addProductNameTextField.getText();
        String stock = addProductInvTextField.getText();
        String price = addProductPriceTextField.getText();
        String max = addProductMaxTextField.getText();
        String min = addProductMinTextField.getText();

        empFieldsErrMessage = Product.textFieldsValidation(name, stock, price, max, min, empFieldsErrMessage);
        if (empFieldsErrMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Addition Warning");
            alert.setContentText(empFieldsErrMessage);
            alert.showAndWait();
            empFieldsErrMessage = "";

        } else {
            int productToAddInv;
            try {
                productToAddInv = Integer.parseInt(stock);
            } catch (NumberFormatException e) {
                displayErrAlert(3);
                return;
            }

            double productToAddPrice;
            try {
                productToAddPrice = Double.parseDouble(price);
            } catch (NumberFormatException e) {
               displayErrAlert(4);
                return;
            }
            int productToAddMax;
            try {
                productToAddMax = Integer.parseInt(max);
            } catch (NumberFormatException e) {
                displayErrAlert(5);
                return;
            }

            int productToAddMin;
            try {
                productToAddMin = Integer.parseInt(min);
            } catch (NumberFormatException e) {
                displayErrAlert(6);
                return;
            }

            try {
                invalidInputMessage = Product.getProductValidation(
                        Double.parseDouble(price),
                        Integer.parseInt(stock),
                        Integer.parseInt(max),
                        Integer.parseInt(min),
                        invalidInputMessage
                );

                if (invalidInputMessage.length() > 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Addition Warning");
                    alert.setHeaderText("The part was NOT added!");
                    alert.setContentText(invalidInputMessage);
                    alert.showAndWait();
                    invalidInputMessage = "";
                } else {

                    Product newProductToAdd = new Product(productToAddId, name, productToAddPrice, productToAddInv,productToAddMax,
                            productToAddMin);
                    for (Part part : associatedPartsList) {
                        newProductToAdd.addAssociatedPart(part);
                    }

                    newProductToAdd.setProductID(Inventory.getIncrementProductId());
                    Inventory.addProduct(newProductToAdd);
                    backToMainScreen(actionEvent);
                }

            } catch (Exception e) {
                // displays an error message to the user if there are empty fields or invalid values.
               displayErrAlert(1);
            }
        }
    }


    /**
     * this method handles the cancel button.It does not save the input data and takes the user back to main screen.
     * the method shows a confirmation message and takes the user back to the main screen if confirmed.
     * @param actionEvent cancel button clicked.
     * @throws IOException IOException
     */
    @FXML
    public void addProductCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation Confirmation.");
        alert.setContentText("Are you sure you wish to cancel?");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            backToMainScreen(actionEvent);
        } else {
            System.out.println("You selected cancel.");
        }
    }

    /**
     * This method handles the add button.
     * This method adds parts to the bottom associated parts table from a selection on the top table.
     * @param actionEvent add button clicked.
     */
    @FXML
    public void addProductAddButtonClicked(ActionEvent actionEvent) {
        /*selectedPart is the user selected part to add to the associated parts table.*/
        Part selectedPart = productAddPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No part selected.");
            alert.showAndWait();
        }else{
            associatedPartsList.add(selectedPart);
            productAssociatedPartsTable.setItems(associatedPartsList);
        }
    }

    /**
     * This method handles the delete button. It removes a part from the associated parts table.
     * This method shows a confirmation message before deleting the selected part.
     * @param actionEvent delete button clicked.
     */
    @FXML
    public void addProductDeletePartButtonClicked(ActionEvent actionEvent) {
        Part selectedPart = productAssociatedPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No part selected!");
            alert.setContentText("You must select an Associated Part.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part Confirmation");
            alert.setContentText("Are you sure you wish to remove the part?");
            Optional<ButtonType> response = alert.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {
                associatedPartsList.remove(selectedPart);
            } else {
                System.out.println("You selected cancel.");
            }
        }
    }

    /**
     * Initializes the add product controller and populates data into the tables
     * @param url ur
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTablePartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addTablePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addTableInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productAddPartsTable.setItems(Inventory.getAllParts());

        associatedPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * this method takes the user back to the main form window.
     * @param event redirects user to main form window.
     * @throws IOException IOException
     */
    public void backToMainScreen (ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method displays different error messages depending on the error.
     * @param alertType different alert messages.
     */
    private void displayErrAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1 -> {
                alert.setTitle("Empty/Invalid Fields Error");
                alert.setHeaderText("Part Couldn't Be Modified");
                alert.setContentText("Field contains blank fields or invalid values.");
                alert.showAndWait();
            }
            case 2 -> {
                alert.setTitle("MachineID Error");
                alert.setHeaderText("Machine ID Not Valid");
                alert.setContentText("The Machine ID may only contain numbers.");
                alert.showAndWait();
            }
            case 3 -> {
                alert.setTitle("Inventory Error");
                alert.setHeaderText("Inventory value must be a number!");
                alert.setContentText("Please fix Inv value.");
                alert.showAndWait();
            }
            case 4 -> {
                alert.setTitle("Price Error");
                alert.setHeaderText("The Price value must be a number!");
                alert.setContentText("Please fix Price value.");
                alert.showAndWait();
            }
            case 5 ->{
                alert.setTitle("Max Value Error");
                alert.setHeaderText("The max value must be a number!");
                alert.setContentText("Please fix Max value");
                alert.showAndWait();
            }
            case 6 ->{
                alert.setTitle("Min Value Error");
                alert.setHeaderText("The min value must be a number!");
                alert.setContentText("Please fix Min value");
                alert.showAndWait();
            }
        }
    }
}

