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
 * @author Sophie Dang
 *This is the modify product controller class.
 */
public class ModifyProductController implements Initializable {
    String empFieldsErrMessage = "";
    String invalidInputMessage = "";
    Product productToModify;
    private ObservableList<Part> assocPartsList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Part, Integer> modifyProductPartTableIDCol;
    @FXML
    private TableColumn<Part, String> modifyProductPartTableNameCol;
    @FXML
    private TableColumn<Part, Integer> modifyProductPartTableInvCol;
    @FXML
    private TableColumn<Part, Double> modifyProductPartTablePriceCol;
    @FXML
    private TableColumn<Part, Integer> modifyProductAssocTableIDCol;
    @FXML
    private TableColumn<Part, String> modifyProductAssocTableNameCol;
    @FXML
    private TableColumn<Part, Integer> modifyProductAssocTableInvCol;
    @FXML
    private TableColumn<Part, Double> modifyProductAssocTablePriceCol;
    @FXML
    private TextField modifyProductIDTextField;
    @FXML
    private TextField modifyProductNameTextField;
    @FXML
    private TextField modifyProductInvTextField;
    @FXML
    private TextField modifyProductPriceTextField;
    @FXML
    private TextField modifyProductMaxTextField;
    @FXML
    private TextField modifyProductMinTextField;
    @FXML
    private TableView<Part> modifyProductAddPartTableView;
    @FXML
    private TableView<Part> modifyProductAssocTableView;
    @FXML
    private TextField modifyProductSearchTextField;

    /**
     * redirects the user back to the main form window.
     *
     * @param event redirects user back to the main screen.
     */
    @FXML
    public void backToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    /**
     * This method handles the search text field.
     * The user can search for a part by name or id.
     * The user can perform partial searches, and searches aren't case-sensitive.
     *
     * @param actionEvent modify product search field clicked.
     */
    @FXML
    private void modifyProductSearchTFClicked(ActionEvent actionEvent) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String partSearchString = modifyProductSearchTextField.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(partSearchString) || part.getName().toLowerCase().contains(partSearchString)
                    || part.getName().toUpperCase().contains(partSearchString)
                    || part.getName().regionMatches(true, 0, partSearchString, 0, 1)) {
                partsFound.add(part);
            }
        }

        modifyProductAddPartTableView.setItems(partsFound);

        if (partsFound.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Ensure all fields are complete and valid values are entered.");
            alert.showAndWait();
        }
    }


    /**
     * This method resets the Parts table when nothing is entered in the search bar.
     * @param keyEvent any key typed
     */
    @FXML
    void searchAssocPartBarClicked(KeyEvent keyEvent) {
        if (modifyProductSearchTextField.getText().isEmpty()) {
            modifyProductAddPartTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * This method handles the cancel button.
     * This method shows a confirmation message before returning the user to the main screen.
     * If the user clicks cancel, then they'll stay on the current page.
     *
     * @param actionEvent modify product cancel button clicked.
     * @throws IOException IOException
     */
    public void modifyProductCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure that you want to cancel these changes and return to the main screen?");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            backToMainScreen(actionEvent);
        }
    }


    /**
     * This method adds parts to the bottom associated parts table from a selection on the top table.
     * @param actionEvent select the add button to move a part into the bottom associated parts table
     */
    public void modifyProductAddButtonClicked(ActionEvent actionEvent) {
        Part partToAdd = modifyProductAddPartTableView.getSelectionModel().getSelectedItem(); //Select associated part from top table.
        if (partToAdd == null) { //Check if part is selected and give error if it is not.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Associated Part Addition Error");
            alert.setContentText("A part was not selected!");
            alert.showAndWait();
        } else {
            assocPartsList.add(partToAdd); //Add part to bottom table
            modifyProductAssocTableView.setItems(assocPartsList);

        }
    }

    /**
     * This method handles the delete associated parts button.
     * This method deletes a part from the associated parts table.
     * It shows an error message if no part was selected before the user clicks delete.
     * It shows a confirmation message asking the user to confirm before deleting the part.
     *
     * @param actionEvent modify product delete button clicked.
     */
    public void modifyProductDeletePartButtonClicked(ActionEvent actionEvent) {
        Part partToDelete = modifyProductAssocTableView.getSelectionModel().getSelectedItem();

        if (partToDelete == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No part selected.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure that you wish to remove the selected part?");
            Optional<ButtonType> response = alert.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {
                assocPartsList.remove(partToDelete);
                modifyProductAssocTableView.setItems(assocPartsList);
            }
        }
    }


    /**
     * THis method handles the save button.
     * This method updates the product in the inventory and launches the main screen
     * It will show alert messages if text fields are empty or contain invalid inputs.
     * @param actionEvent modify product save button clicked.
     */
    @FXML
    public void modifyProductSaveButtonClicked(ActionEvent actionEvent) {

        int id = productToModify.getProductID();
        //Retrieve inputs from text fields
        String modifyProductName = modifyProductNameTextField.getText();
        String modifyProductPrice = modifyProductPriceTextField.getText();
        String modifyProductInv = modifyProductInvTextField.getText();
        String modifyProductMax = modifyProductMaxTextField.getText();
        String modifyProductMin = modifyProductMinTextField.getText();


        empFieldsErrMessage = Product.textFieldsValidation(modifyProductName, modifyProductPrice, modifyProductInv,
                modifyProductMax, modifyProductMin, empFieldsErrMessage);
        if (empFieldsErrMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Addition Warning");
            alert.setContentText(empFieldsErrMessage);
            alert.showAndWait();
            empFieldsErrMessage = "";

        } else {
            int inv;
            try {
                inv = Integer.parseInt(modifyProductInv);
            } catch (NumberFormatException e) {
                displayErrAlert(3);
                return;
            }

            double price;
            try {
                price = Double.parseDouble(modifyProductPrice);
            } catch (NumberFormatException e) {
                displayErrAlert(4);
                return;
            }
            int max;
            try {
                max = Integer.parseInt(modifyProductMax);
            } catch (NumberFormatException e) {
                displayErrAlert(5);
                return;
            }

            int min;
            try {
                min = Integer.parseInt(modifyProductMin);
            } catch (NumberFormatException e) {
                displayErrAlert(6);
                return;
            }

            try {
                invalidInputMessage = Product.getProductValidation(
                        Double.parseDouble(modifyProductPrice),
                        Integer.parseInt(modifyProductInv),
                        Integer.parseInt(modifyProductMax),
                        Integer.parseInt(modifyProductMin),
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

                    Product modifiedProduct = new Product(id, modifyProductName, price, inv, max,
                            min);
                    for (Part part : assocPartsList) {
                        modifiedProduct.addAssociatedPart(part);
                    }

                    Inventory.deleteProduct(productToModify);
                    Inventory.addProduct(modifiedProduct);
                    backToMainScreen(actionEvent);
                }

            } catch (Exception e) {
                displayErrAlert(1);
            }
        }
    }


        /**
         * This method initializes the controller and adds data associated with selected part to the right text fields.
         * @param url url
         * @param resourceBundle resource bundle
         */
        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            productToModify = MainFormController.getSelectedProduct();
            assocPartsList = productToModify.getAllAssociatedParts();

            modifyProductPartTableIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            modifyProductPartTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            modifyProductPartTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            modifyProductPartTableInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            modifyProductAddPartTableView.setItems(Inventory.getAllParts());

            modifyProductAssocTableIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            modifyProductAssocTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            modifyProductAssocTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            modifyProductAssocTableInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            modifyProductAssocTableView.setItems(assocPartsList);

            modifyProductIDTextField.setText(String.valueOf(productToModify.getProductID()));
            modifyProductNameTextField.setText(productToModify.getProductName());
            modifyProductPriceTextField.setText(String.valueOf(productToModify.getProductPrice()));
            modifyProductInvTextField.setText(String.valueOf(productToModify.getProductStock()));
            modifyProductMaxTextField.setText(String.valueOf(productToModify.getProductMax()));
            modifyProductMinTextField.setText(String.valueOf(productToModify.getProductMin()));
        }

        /**
         * This method displays different error messages depending on the error.
         * @param alertType alerts and shows different error messages.
         */
        private void displayErrAlert ( int alertType){
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
                    alert.setContentText("Please fix the Inventory value.");
                    alert.showAndWait();
                }
                case 4 -> {
                    alert.setTitle("Price Error");
                    alert.setHeaderText("The Price value must be a number!");
                    alert.setContentText("Please fix the Price value.");
                    alert.showAndWait();
                }
                case 5 -> {
                    alert.setTitle("Max Value Error");
                    alert.setHeaderText("The max value must be a number!");
                    alert.setContentText("Please fix the Max value");
                    alert.showAndWait();
                }
                case 6 -> {
                    alert.setTitle("Min Value Error");
                    alert.setHeaderText("The min value must be a number!");
                    alert.setContentText("Please fix the Min value");
                    alert.showAndWait();
                }
            }
        }
    }
