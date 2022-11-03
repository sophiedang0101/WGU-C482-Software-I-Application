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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.c482project.Inventory.getAllParts;

/**
 * Handles every element that exists on the Main Screen.
 *@author Sophie Dang
 * FUTURE ENHANCEMENT: Add an "Undo" button so that the user can undo and get back a part/product they deleted.
 * FUTURE ENHANCEMENT: It would be good to make it so the user can just press any key to search for a part/product
 * instead of having to specifically press "Enter".
 */
public class MainFormController implements Initializable {
    public static Part partSelectedToModify;
    public static Product productSelectedToModify;
    public static Part getSelectedPart() {return partSelectedToModify;}
    public static Product getSelectedProduct(){return productSelectedToModify;}
    @FXML private TextField mainSearchPartTextField;
    @FXML private TextField mainSearchProductTextField;
    @FXML private TableView<Part> mainPartsTableView;
    @FXML private TableColumn<Part, Integer> allPartsIDsCol;
    @FXML private TableColumn<Part, String> allPartNamesCol;
    @FXML private TableColumn<Part, Integer> allPartsInvCol;
    @FXML private TableColumn<Part, Double> allPartsPriceCol;
    @FXML private TableView<Product> mainProductsTableView;
    @FXML private TableColumn<Product, Integer> allProductsIDsCol;
    @FXML private TableColumn<Product, String> allProductNamesCol;
    @FXML private TableColumn<Product, Integer> allProductsInvCol;
    @FXML private TableColumn<Product, Double> allProductsPriceCol;


    /**
     * This method handles the part search text field.
     * This method allows the users to search for part either by id or name.
     * This method allows the users to perform partial searches.
     * Searches aren't case-sensitive.
     * The method will show an error message if the part searched is not found.
     * @param actionEvent part search field clicked.
     */
    public void partSearchTextFieldClicked(ActionEvent actionEvent) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        String searchString = mainSearchPartTextField.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().toLowerCase().contains(searchString)
                    || part.getName().toUpperCase().contains(searchString)
                    || part.getName().regionMatches(true,0,searchString,0,1)) {
                    foundParts.add(part);
            }
        }
        mainPartsTableView.setItems(foundParts);
        if (foundParts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Part Not Found! Please enter a valid part id or name");
            alert.showAndWait();
        }
    }


    /**
     * This method handles the part add button.
     * The method opens up the Add Part Screen.
     * @param actionEvent add button clicked.
     * @throws IOException IOException.
     */
    public void addPartButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddPart.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method handles the part modify button.
     * This method launches the modify part screen.
     * @param actionEvent part modify button clicked.
     * @throws IOException
     */
    public void partModifyButtonClicked(ActionEvent actionEvent) throws IOException {
        partSelectedToModify = mainPartsTableView.getSelectionModel().getSelectedItem();
        if (partSelectedToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Part Selected");
            alert.showAndWait();
        } else {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPart.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This method handles the part delete button.
     * The method shows an error message if no part was selected.
     * The method shows a confirmation message asking user to confirm before deleting the part.
     * If the user clicks cancel, then nothing happens and the part will not be removed.
     * @param event part delete button clicked.
     */
    @FXML public void partDeleteButtonClicked(ActionEvent event){
        partSelectedToModify = mainPartsTableView.getSelectionModel().getSelectedItem();

        if(partSelectedToModify == null){
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Part Deletion Error");
            nullAlert.setContentText("No part was selected.!");
            nullAlert.showAndWait();
        }
        else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION); //Confirm deletion request
                alert.setTitle("Deletion Confirmation");
                alert.setHeaderText("Are you sure you want to delete?");
                alert.setContentText("Press OK to delete part. \nPress Cancel to keep part.");
                Optional<ButtonType> response = alert.showAndWait();

                if(response.isPresent() && response.get() == ButtonType.OK){
                    Inventory.deletePart(partSelectedToModify);
                }
            }
        }


    /**
     * This method handles the product search text field.
     * This method allows users to search for product by id or name.
     * Searches aren't case-sensitive.
     * The method allows users to perform partial searches.
     * The method shows an error message if the product searched is not found.
     * @param actionEvent product search field clicked.
     */
    @FXML public void productSearchTextFieldClicked(ActionEvent actionEvent){
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();

        String searchString = mainSearchProductTextField.getText();
        for (Product product : allProducts) {
            if (String.valueOf(product.getProductID()).contains(searchString)||
                    product.getProductName().toLowerCase().contains(searchString)
                    || product.getProductName().toUpperCase().contains(searchString)
                    || product.getProductName().regionMatches(true,0,searchString,0,1)) {
                    foundProducts.add(product);
            }
        }
        mainProductsTableView.setItems(foundProducts);
        if (foundProducts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Product Not Found! Please enter a valid part id or name");
            alert.showAndWait();
        }
    }

    /**
     * This method handles the product add button.
     * The method launches the Add Product Screen.
     * @param actionEvent add product button clicked.
     * @throws IOException IOException.
     */
    public void addProductButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddProduct.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method handles the product modify button.
     * The method launches the Modify Product Screen.
     * @param actionEvent modify product button clicked.
     * @throws IOException IOException
     */
    public void modifyProductButtonClicked(ActionEvent actionEvent) throws IOException {
        productSelectedToModify = mainProductsTableView.getSelectionModel().getSelectedItem();
        if (productSelectedToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Product Selected");
            alert.showAndWait();
        } else {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyProduct.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This method handles the product delete button.
     * The method shows an error message if no product was selected for deletion.
     * The method shows a confirmation message before removing the product.
     * The method prevents the users from deleting a product that has one or more associated parts.
     * The user must delete all associated parts before they can delete the product.
     * @param actionEvent product delete button clicked.
     */
    @FXML public void productDeleteButtonClicked(ActionEvent actionEvent){
        productSelectedToModify = mainProductsTableView.getSelectionModel().getSelectedItem();

        if(productSelectedToModify == null){
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Error");
            nullAlert.setContentText("Please select a product to delete.");
            nullAlert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Product Deletion Confirmation");
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> response = alert.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {

                ObservableList<Part> associatedPartList = productSelectedToModify.getAllAssociatedParts();

                if (associatedPartList.size() >= 1) {
                    Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                    alertInfo.setTitle("Error");
                    alertInfo.setHeaderText("There are other parts are associated with this Product");
                    alertInfo.setContentText("You must delete all parts associated with this product before deletion can be successful");
                    alertInfo.showAndWait();
                } else {
                    Inventory.deleteProduct(productSelectedToModify);
                }
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPartsTableView.setItems(getAllParts());
        allPartsIDsCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartNamesCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProductsTableView.setItems(Inventory.getAllProducts());
        allProductsIDsCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        allProductNamesCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        allProductsInvCol.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        allProductsPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

    }


    /**
     * this method handles the exit button and allows users to exit out of the inventory system.
     * @param event exit button clicked.
     */
    public void mainFormExitButtonClicked (ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm exit.");
        alert.setHeaderText("Do you wish to exit?");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("Exiting Canceled. Please fill out the form.");
        }
    }
}