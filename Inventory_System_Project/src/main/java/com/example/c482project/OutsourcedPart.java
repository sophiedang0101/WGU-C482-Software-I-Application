package com.example.c482project;

/**
 * @author Sophie Dang
 *This is the outsourced part class.
 */
public class OutsourcedPart extends Part{
    /**
     * OutSourced uses companyName instead of machineID
     * variable for the companyName field
     */
    private String companyName;

    /**
     * Constructor to create a new outsourced part
     * @param id part id
     * @param partName part name
     * @param partPrice part price
     * @param partStock inventory in stock
     * @param partMin minimum inventory
     * @param partMax max inventory
     * @param companyName company name
     */
    public OutsourcedPart(int id, String partName, double partPrice, int partStock,
                          int partMax, int partMin,String companyName) {
        super(id, partName, partPrice, partStock, partMax, partMin);
        this.companyName = companyName;
       // setCompanyName(companyName);
    }

    /**
     * Sets the companyName
     * @param companyName sets companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the companyName when called
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

}
