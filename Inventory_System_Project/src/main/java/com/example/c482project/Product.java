package com.example.c482project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Sophie Dang
 *This is the product class.
 */
public class Product {

    /**
     * associatedParts is the list that holds the product parts
     */
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();

    /**
     * Product fields
     */
    private int productID;
    private String productName;
    private double productPrice;
    private int productStock;
    private int productMin;
    private int productMax;

    /**
     * Empty Default constructor
     */
    public Product(){}

    /**
     * Constructor used to create a new product object
     * @param productID the product id.
     * @param productName the product name.
     * @param productPrice products price.
     * @param productStock inventory in stock.
     * @param productMin minimum inventory.
     * @param productMax max inventory.
     */
    public Product(int productID, String productName, double productPrice, int productStock, int productMax, int productMin) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productMax = productMax;
        this.productMin = productMin;
    }

    // Setters
    /** Sets the product ID. */
    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    // Getters
    /** Gets the product ID. */
    public int getProductID()
    {
        return productID;
    }

    /** Gets the product name. */
    public String getProductName()
    {
        return productName;
    }

    /** Gets the product price. */
    public double getProductPrice()
    {
        return productPrice;
    }

    /** Gets the product stock. */
    public int getProductStock()
    {
        return productStock;
    }

    /** Gets the product min. */
    public int getProductMin()
    {
        return productMin;
    }

    /** Gets the product max. */
    public int getProductMax()
    {
        return productMax;
    }

    /**
     * Gets all parts in associated parts list
     * @return associatedParts list
     */
    public ObservableList<Part> getAllAssociatedParts()
    {
        return associatedPartsList;
    }

    /**
     * Adds the Part to the associated list of Parts that make up the Product
     *
     * @param part the part of the Product
     */
    public void addAssociatedPart(Part part)
    {
        associatedPartsList.add(part);
    }

    /**
     * This method checks to see if any text fields are empty and displays error messages to the user depending
     * on the type of error.
     * @param name name text field
     * @param stock inventory text field
     * @param price cost text field
     * @param max max inventory amount text field
     * @param min min inventory amount text field
     */
    public static String textFieldsValidation(String name, String price,String stock, String max, String min, String empFieldErrMessage)
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
        return empFieldErrMessage;
    }

    /**
     * This method checks to see if all inputs entered into the text fields are valid.
     * If the inputs are invalid, then the method displays error messages to the user.
     * @param stock product inventory
     * @param price product price
     * @param max product max inventory level
     * @param min product min inventory level
     * */
    public static String getProductValidation(double price, int stock, int max, int min, String invalidInputMessage) {
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