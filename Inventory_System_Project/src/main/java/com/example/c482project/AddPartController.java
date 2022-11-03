package com.example.c482project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * this class controls the Add Part UI.
 * @author Sophie Dang
 */
public class AddPartController implements Initializable {
    @FXML String invalidInputMessage = "";
    @FXML String empFieldsErrMessage = "";
    @FXML private TextField addPartNameTextField;
    @FXML private TextField addPartInvTextField;
    @FXML private TextField addPartPriceTextField;
    @FXML private TextField addPartMaxTextField;
    @FXML private TextField addPartMinTextField;
    @FXML private TextField machineIdOrCompName;
    @FXML private Label machineCompanyIDLabel;
    @FXML private RadioButton addPartInHouseRadioButton;
    @FXML private RadioButton addPartOutSourcedRadioButton;
    @FXML private ToggleGroup addPartToggleGroup;

    public AddPartController() {
    }

    /**
     * This method handles the In-House radio button.
     * The label is set to "Machine ID" when the in-house button is clicked.
     */
    @FXML public void addPartInHouseRadioButtonClicked() {
        if (this.addPartToggleGroup.getSelectedToggle().equals(this.addPartInHouseRadioButton)) {
            machineCompanyIDLabel.setText("Machine ID");
        }
    }


    /**
     * This method handles the outsourced radio button.
     * The Label is set to "Company Name" when the outsourced button is clicked.
     */
    @FXML public void addPartOutSourcedRadioButtonClicked() {
        if (this.addPartToggleGroup.getSelectedToggle().equals(this.addPartOutSourcedRadioButton)) {
            machineCompanyIDLabel.setText("Company Name");
        }
    }


    /**
     * This method alerts the user to confirm that they would like to cancel adding a part.
     * The method takes the user back to Main Form window.
     * @param actionEvent cancels adding part
     * @throws IOException FXMLLoader
     */
    public void addPartCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setContentText("Please confirm that you wish to void these changes and return to the main screen?");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            backToMainScreen(actionEvent);
        }
    }


    /**
     * This method handles the save button and adds the new part to the inventory.
     * Returns user to the main screen after part has been successfully added.
     * This method shows error messages if any of the text fields are empty or contains invalid inputs.
     * @param actionEvent
     *
     * RUNTIME ERROR: the ids for idOrName and minString text fields were switched by accident.
     * This error kept generating errors and prohibited new parts from being added.
     * Re-checked the add part fxml file and found and fixed the error.
     */
    public void saveButtonClicked(ActionEvent actionEvent) throws IOException {
        int autoId = 0;
        //retrieve all the inputs from text fields.
        String name = addPartNameTextField.getText();
        String invString = addPartInvTextField.getText();
        String priceString = addPartPriceTextField.getText();
        String maxString = addPartMaxTextField.getText();
        String minString = addPartMinTextField.getText();
        String idOrName = machineIdOrCompName.getText();

        //The textFieldsValidationCheck() method checks if any text fields are empty and give error messages if they are.
        empFieldsErrMessage = Inventory.textFieldsValidationCheck(name,invString,priceString,
                            maxString,minString,idOrName,empFieldsErrMessage);
        if(empFieldsErrMessage.length() > 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Addition Warning");
            alert.setContentText(empFieldsErrMessage);
            alert.showAndWait();
            empFieldsErrMessage = "";

        }else {
            int inv;
            try{
                inv = Integer.parseInt(invString);//Get integer for inv
            }catch(NumberFormatException e){//Give error if the inventory entry is not a number
                displayErrAlert(3);
                return;
            }
            double price;
            try{
                price = Double.parseDouble(priceString);//Get double for price
            }catch(NumberFormatException e){//Give error if the price entry is not a number
                displayErrAlert(4);
                return;
            }
            int max;
            try{
                max = Integer.parseInt(maxString);//Get integer for max
            }catch(NumberFormatException e){//Give error if max entry is not a number
                displayErrAlert(5);
                return;

            }
            int min;
            try{
                min= Integer.parseInt(minString);//Get integer for min
            }catch(NumberFormatException e){//Give error if min entry is not a number
                displayErrAlert(6);
                return;

            }
            try{
                //the partValidationCheck() method checks text fields to ensure inputs are valid and follow stated criteria.
                invalidInputMessage = Inventory.partValidationCheck(
                        Integer.parseInt(invString),
                        Double.parseDouble(priceString),
                        Integer.parseInt(maxString),
                        Integer.parseInt(minString),
                        invalidInputMessage
                );

                if(invalidInputMessage.length() > 0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Addition Warning");
                    alert.setHeaderText("The Part was NOT added!");
                    alert.setContentText(invalidInputMessage);
                    alert.showAndWait();
                    invalidInputMessage = "";
                }
                //If the Outsourced toggle button is selected, then a new Outsourced part is saved to the inventory.
                else {
                    if(this.addPartOutSourcedRadioButton.isSelected()){
                        OutsourcedPart newOutPart = new OutsourcedPart(
                                autoId,name,price,inv,max,min,idOrName
                        );
                        newOutPart.setId(Inventory.getIncrementPartId());
                        Inventory.addPart(newOutPart);
                    }
                    else{
                        int machineIDNumber;
                        try{
                            machineIDNumber = Integer.parseInt(idOrName);//Get integer for machineId
                        }catch(NumberFormatException e){//Give error if id entry is not a number
                            displayErrAlert(2);
                            return;
                        }
                        //If the InHouse toggle button is selected, then a new InHouse part is saved to the inventory.
                        InHousePart newInPart = new InHousePart(
                                autoId,name,price,inv,max,min,machineIDNumber
                        );

                        newInPart.setId(Inventory.getIncrementPartId());
                        Inventory.addPart(newInPart);
                    }
                    backToMainScreen(actionEvent);

                }
            }
            catch(IOException e){
                displayErrAlert(1);
            }
        }

    }

    /**
     * This method initializes the controller and sets the in-house radio button to be selected
     * when the add part form is launched.
     * @param url
     * @param resourceBundle
     *
     * RUNTIME ERROR: the in-house radio button was not selected by default when the add part window opens.
     * So I set the in-house radio button to selected inside the initialize method to have it be selected already
     * when the add part form loads.
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        addPartInHouseRadioButton.setSelected(true);
//        partId = Inventory.getIncrementPartId();
    }


    /**
     * This method takes the user back to the main form window.
     * @param actionEvent taking the user back to the main screen.
     * @throws IOException
     */
        public void backToMainScreen (ActionEvent actionEvent) throws IOException {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainForm.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    /**
     * This method displays different error messages depending on the error.
     * @param alertType alerts different error messages to the user.
     */
    private void displayErrAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1 -> {
                alert.setTitle("Empty/Invalid Fields Error");
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


