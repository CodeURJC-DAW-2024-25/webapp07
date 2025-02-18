package es.codeurjc.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;



@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int price;
    private String ingredients;
    //private List<String> Allergens;
    private boolean isVegan;
    @Lob
    @JsonIgnore
    private Blob dishImagefile;

    private String dishImagePath;

    public Dish(){}
    public Dish(@NotNull String name, String description, int price, String ingredients, boolean isVegan, String dishImagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        //this.allergens = allergens;
        this.isVegan = isVegan;
        this.dishImagePath = dishImagePath;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    //public Array<String> getAllergens() {
    //    return allergens;
    //}

    //public void setAllergens(Array<String> allergens) {
      //  this.allergens = allergens;
    //}

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public String getDishImagePath() {
        return dishImagePath;
    }

    public void setDishImagePath(String imageURL) {
        this.dishImagePath = imageURL;
    }

    public Blob getDishImagefile() {
        return dishImagefile;
    }

    public void setDishImagefile(Blob dishImagefile) {
        this.dishImagefile = dishImagefile;
    }

    public Blob URLtoBlob(String webURL){
        try {
            URL url = new URL(webURL);
            InputStream in = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Read the image data into a byte array
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            in.close();
            // Convert the ByteArrayOutputStream to a byte array
            byte[] imageBytes = baos.toByteArray();
            Blob imageBlob = new SerialBlob(imageBytes);
            return imageBlob;
        } catch (IOException | SQLException e) {
            System.out.println("Error");
            return null;
        }
    }

    public String blobToString(Blob imageFile, Dish dishE) throws SQLException {
        Blob blob = dishE.getDishImagefile();
        byte[] bytes = blob.getBytes(1,(int) blob.length());
        String dishImage = Base64.getEncoder().encodeToString(bytes);
        return dishImage;
    }

}