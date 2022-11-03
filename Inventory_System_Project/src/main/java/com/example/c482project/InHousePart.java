package com.example.c482project;

/**
 * This is the in-house class.
 * @author Sophie Dang
 */
public class InHousePart extends Part{
    private int machineID;

    /**
     * Constructor used to create an InHouse Part
     * @param id part id number
     * @param name part name
     * @param price part price
     * @param stock inventory in stock
     * @param min minimum inventory
     * @param max max inventory
     * @param machineID machine id
     */
    public InHousePart(int id, String name, double price, int stock, int max, int min, int machineID) {
        super(id, name, price, stock, max, min);
        setMachineID(machineID);
    }

    /**
     * Sets machine id of the In-House Part.
     * @param machineID the partID of the InHouse Part.
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * Gets machine id of the In-House Part.
     * @return the machineId of the In-House Part.
     */
    public int getMachineID() {
        return machineID;
    }
}
