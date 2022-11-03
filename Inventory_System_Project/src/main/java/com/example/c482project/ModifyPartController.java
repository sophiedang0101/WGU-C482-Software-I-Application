package com.example.c482project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.c482project.MainFormController.getSelectedPart;

/**
 * @author Sophie Dang
 * This is the modify part controller class.
 */
public class ModifyPartController implements Initializable {

    String empFieldErrMessage = "";
    String invalidInputMessage = "";
    @FXML public ToggleGroup inHouseOrOutsourcedToggle;
    private int partId = getSelectedPart().getId();
    public Part partToModify;
    @FXML private Label modifyPartMachIDCompNameLabel;
    @FXML private TextField modifyPartIDTextField;
    @FXML private TextField modifyPartNameTextField;
    @FXML private TextField modifyPartInvTextField;
    @FXML private TextField modifyPartPriceTextField;
    @FXML private TextField modifyPartMaxTextField;
    @FXML private TextField modifyPartMinTextField;
    @FXML private TextField modifyPartMachIDCompNameField;
    @FXML private RadioButton modifyPartInHouseRadioButton;
    @FXML private RadioButton modifyPartOutsourcedRadioButton;


    /**
     * This method controls the in-house radio button.
     * The method sets the label of the modify part form to "Machine ID".
     * @param actionEvent modify part in house button clicked.
     */
    public void modifyPartInHouseRadioButtonClicked(ActionEvent actionEvent) {
        if (this.inHouseOrOutsourcedToggle.getSelectedToggle().equals(this.modifyPartInHouseRadioButton))
            modifyPartMachIDCompNameLabel.setText("Machine ID");
    }


    /**
     * This method handles the outsourced radio button.
     * The method sets the label of the modify part form to "Company Name".
     * @param actionEvent modify part outsourced radio button clicked.
     */
    public void modifyPartOutsourcedRadioButtonClicked(ActionEvent actionEvent) {
        if (this.inHouseOrOutsourcedToggle.getSelectedToggle().equals(this.modifyPartOutsourcedRadioButton))
            modifyPartMachIDCompNameLabel.setText("Company Name");
    }


    /**
     * This method handles the save button and saves the modified part.
     * The method checks to ensure accuracy of the fields being entered.
     * The method shows an error message if there are empty fields or invalid values entered.
     * It will display a success message if the product is modified successfully,then takes the user back to the main form.
     * RUNTIME ERROR: When the product was modified and added to the inventory shown on the main screen, the old version of the product was not replaced.
     * I forgot remove the old version of the product and then add in the new modified version.
     * Thus, I deleted the old one by using Inventory.deletePart() and use addPart() to add the new version to the inventory.
     */
    @FXML
    public void modifyPartSaveButtonClicked(ActionEvent actionEvent) {

        int id = partId;
        //Retrieve inputs from text fields
        String name = modifyPartNameTextField.getText();
        String invString = modifyPartInvTextField.getText();
        String priceString = modifyPartPriceTextField.getText();
        String maxString = modifyPartMaxTextField.getText();
        String minString = modifyPartMinTextField.getText();
        String companyString = modifyPartMachIDCompNameField.getText();


        //The method checks if text entry fields are empty and give error message if they are.
        empFieldErrMessage = Inventory.textFieldsValidationCheck(name, invString, priceString, maxString, minString, companyString, empFieldErrMessage);
        if (empFieldErrMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Addition Warning");
            alert.setContentText(empFieldErrMessage);
            alert.showAndWait();
            empFieldErrMessage = "";

        } else {
            int inv;
            try {
                inv = Integer.parseInt(invString);
            } catch (NumberFormatException e) {
               displayErrAlert(3);
                return;
            }
            double price;
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
               displayErrAlert(4);
                return;
            }
            int max;
            try {
                max = Integer.parseInt(maxString);
            } catch (NumberFormatException e) {
               displayErrAlert(5);
                return;
            }
            int min;
            try {
                min = Integer.parseInt(minString);
            } catch (NumberFormatException e) {
                displayErrAlert(6);
                return;
            }

            try {
                //The method checks inputs of text fields and displays error messages if the values don't meet the stated criteria.
                invalidInputMessage = Inventory.partValidationCheck(
                        Integer.parseInt(invString),
                        Double.parseDouble(priceString),
                        Integer.parseInt(maxString),
                        Integer.parseInt(minString),
                        invalidInputMessage
                );
                if (invalidInputMessage.length() > 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Addition Warning");
                    alert.setHeaderText("The part was NOT added!");
                    alert.setContentText(invalidInputMessage);
                    alert.showAndWait();
                    invalidInputMessage = "";

                }
                else {
                    //if the outsourced radio button is selected, then delete the part that was selected to be modified and save
                    //the new modified part to the inventory.
                    if (this.modifyPartOutsourcedRadioButton.isSelected()) {
                        OutsourcedPart modifiedOutPart = new OutsourcedPart(id,name,price, inv, max, min, companyString);
                        Inventory.deletePart(partToModify);
                        Inventory.addPart(modifiedOutPart);
                    }
                    else {
                        int machineIdNumber;
                        try {
                            machineIdNumber = Integer.parseInt(companyString);
                        } catch (NumberFormatException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Machine ID ERROR");
                            alert.setHeaderText("Machine ID must be a number!");
                            alert.setContentText(invalidInputMessage);
                            alert.showAndWait();
                            return;
                        }

                        //if the in-house radio button is selected, then delete the part that was selected to be modified and save
                        //the new modified part to the inventory.
                        InHousePart modifiedInPart = new InHousePart(id, name, price, inv, max, min, machineIdNumber);
                        Inventory.deletePart(partToModify);
                        Inventory.addPart(modifiedInPart);
                    }
                    backToMainScreen(actionEvent);
                }
            }catch(IOException e){
                displayErrAlert(1);
            }
        }
    }



    /**
     * this method shows a confirmation message when the cancel button is clicked.
     * if the user selects OK, they're redirected to the main form window.
     * if the user clicks the cancel button, then they'll stay on the current page.
     * @param event modify part cancel button clicked.
     * @throws IOException IOException
     */
    @FXML public void modifyPartCancelButtonClicked(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancellation");
        alert.setContentText("Please confirm you wish to cancel modifying the part.");
        Optional<ButtonType> response = alert.showAndWait();

        if(response.isPresent() && response.get() == ButtonType.OK){
            backToMainScreen(event);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }



    /**
     *This method takes the user back to the main form window.
     * @param event redirects user back to main form window.
     */
    public void backToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    /**
     * Initializes the controller and displays all information about selected part in the appropriate text fields
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyPartIDTextField.setDisable(true);
        partToModify = getSelectedPart();

        if(partToModify instanceof InHousePart){
            modifyPartInHouseRadioButton.setSelected(true);
            modifyPartMachIDCompNameLabel.setText("Machine ID");
            modifyPartMachIDCompNameField.setText(String.valueOf(((InHousePart) partToModify).getMachineID()));

        }

        if(partToModify instanceof OutsourcedPart){
            modifyPartOutsourcedRadioButton.setSelected(true);
            modifyPartMachIDCompNameLabel.setText("Company Name");
            modifyPartMachIDCompNameField.setText(((OutsourcedPart) partToModify).getCompanyName());
        }

        modifyPartIDTextField.setText(String.valueOf(partToModify.getId()));
        modifyPartNameTextField.setText(partToModify.getName());
        modifyPartPriceTextField.setText(String.valueOf(partToModify.getPrice()));
        modifyPartInvTextField.setText(String.valueOf(partToModify.getStock()));
        modifyPartMaxTextField.setText(String.valueOf(partToModify.getMax()));
        modifyPartMinTextField.setText(String.valueOf(partToModify.getMin()));


    }

    /**
     * This method displays different error messages depending on the error.
     * @param alertType alerts and shows different error messages.
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
                alert.setContentText("Please fix the Inventory value.");
                alert.showAndWait();
            }
            case 4 -> {
                alert.setTitle("Price Error");
                alert.setHeaderText("The Price value must be a number!");
                alert.setContentText("Please fix the Price value.");
                alert.showAndWait();
            }
            case 5 ->{
                alert.setTitle("Max Value Error");
                alert.setHeaderText("The max value must be a number!");
                alert.setContentText("Please fix the Max value");
                alert.showAndWait();
            }
            case 6 ->{
                alert.setTitle("Min Value Error");
                alert.setHeaderText("The min value must be a number!");
                alert.setContentText("Please fix the Min value");
                alert.showAndWait();
            }
        }
    }

}
