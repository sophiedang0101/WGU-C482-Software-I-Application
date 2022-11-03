package com.example.c482project;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *@author Sophie Dang
 *This is the inventory class.
 */
public class Inventory {
    /**
     * create allParts and allProducts lists to contain all available parts/products.
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static int incrementPartId = 0;
    private static int incrementProductId = 0;

    public Inventory() {}

    public static int getIncrementPartId(){
        return incrementPartId++;
    }

    public static int getIncrementProductId(){
        return incrementProductId++;
    }

    /** This is the parts' getter.
     * @return all the parts in the inventory
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /** This is the products' getter.
     * @return all the products in the inventory
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * Add new parts to allParts list
     * @param newPart creates a new part
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the allProducts list.
     * @param newProduct creates a new product
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }


    /** This deletes a part.
     * @param selectedPart the part to delete
     */
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }


    /** This deletes a product.
     @param selectedProduct the product to delete
     */
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }


    /**
     * This method checks to see if any text fields are empty and displays error messages to the user
     * depending on the type of error.
     * @param name name text field
     * @param stock inventory text field
     * @param price cost text field
     * @param max max inventory amount text field
     * @param min min inventory amount text field
     * @param partMachIdOrCompName either machine id or company name text field depending on whether the in-house
     * or outsourced radio button is selected.
     */
    public static String textFieldsValidationCheck(String name, String stock, String price, String max, String min,
                                        String partMachIdOrCompName, String empFieldErrMessage)
    {
        if (name.equals("")) {
            empFieldErrMessage += "The Part Name field cannot be empty. ";
        }
        if (stock.equals("")) {
            empFieldErrMessage += "\nThe Part Inventory field cannot be empty. ";
        }
        if (price.equals("")) {
            empFieldErrMessage += "\nThe Part Price field cannot be empty. ";
        }
        if (max.equals("")) {
            empFieldErrMessage += "\nThe Part Max field cannot be empty. ";
        }
        if (min.equals("")) {
            empFieldErrMessage += "\nThe Part Min field cannot be empty. ";
        }
        if (partMachIdOrCompName.equals("")) {
            empFieldErrMessage += "\nThe Part MachineID or Company Name field cannot be empty. ";
        }
        return empFieldErrMessage;
    }

    /**
     * This method checks to see if all inputs entered into the text fields are valid.
     * If the inputs are invalid, then the method displays error messages to the user.
     * @param stock part inventory
     * @param price part price
     * @param max part max inventory level
     * @param min part min inventory level
     * */
    public static String partValidationCheck(int stock, double price, int max, int min, String invalidInputMessage) {
        if (stock < 1) {
            invalidInputMessage += "\nThe Inventory cannot be less than 1. ";
        }
        if (price <= 0) {
            invalidInputMessage += "\nThe Price must be greater than $0. ";
        }
        if (max < min) {
            invalidInputMessage += "\nThe Maximum stock must be greater than the Minimum stock. ";
        }
        if (stock < min || stock > max) {
            invalidInputMessage += "\nThe Inventory must be a number equal to or between the Max and Min values. ";
        }
        return invalidInputMessage;
    }

}
