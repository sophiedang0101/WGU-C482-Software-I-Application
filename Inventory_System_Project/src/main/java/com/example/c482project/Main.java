package com.example.c482project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * @author Sophie Dang
 * This is the main class.
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        sampleData();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This method provides sample data.
     * The user can choose to comment out the method call if they want an empty inventory.
     */
    void sampleData()
    {
        int autoPartId = Inventory.getIncrementPartId();
        InHousePart brake = new InHousePart(autoPartId, "Brake", 15.99, 15, 100, 1, 1001);

        autoPartId = Inventory.getIncrementPartId();
        InHousePart tire = new InHousePart(autoPartId, "Tire", 59.99, 15, 100, 1, 1002);

        autoPartId = Inventory.getIncrementPartId();
        InHousePart rim = new InHousePart (autoPartId, "Rim", 29.99, 10, 100, 1, 1003);

        Inventory.addPart(brake);
        Inventory.addPart(tire);
        Inventory.addPart(rim);

        int autoProductId = Inventory.getIncrementProductId();
        Product gtBike = new Product(autoProductId, "GT Bike", 399.99, 10, 100, 1);
        Inventory.addProduct(gtBike);


        autoProductId = Inventory.getIncrementProductId();
        Product dirtBike = new Product (autoProductId, "Dirt Bike", 759.99, 10, 100, 1);
        Inventory.addProduct(dirtBike);

        autoProductId = Inventory.getIncrementProductId();
        Product mountainBike = new Product (autoProductId, "Mountain Bike", 759.99, 10, 100, 1);
        Inventory.addProduct(mountainBike);

    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*                                              REFERENCES

    Buchalka,Tim. Java Programming Masterclass Covering Java 11 & Java 17.Udemy.com,
    https://wgu.udemy.com/course/java-the-complete-java-developer-course/learn/lecture/15386014#overview

    Lysecky,Roman. C482: Software I - Java. zyBooks.com,
    https://learn.zybooks.com/zybook/WGUC482v7/chapter/14/section/1

    C482 Webinar Blast Archive. visual.force, https://srm--c.na127.visual.force.com/apex/coursearticle?Id=kA03x000000yIOQCA2#

    C482 Application Video Demonstration.Panopto,https://wgu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e43cbfc8-f79e-431c-ae7c-abe901465598



*/