package es.codeurjc.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.codeurjc.backend.enums.Allergens;
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

/**
 * Entity representing a Dish in the system.
 */
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int price;

    @ElementCollection
    private List<String> ingredients;

    @ElementCollection
    private List<Allergens> allergens;

    private boolean isVegan;
    @Lob
    @JsonIgnore
    private Blob dishImagefile;
    private boolean image;

    private String dishImagePath;

    private boolean isAvailable;

    @ElementCollection
    private List<Integer> rates;

    private int rate;

    /**
     * Default constructor.
     */
    public Dish(){}

    /**
     * Constructor with parameters.
     */
    public Dish(@NotNull String name, String description, int price, List<String> ingredients, boolean isVegan, String dishImagePath, boolean image, List<Allergens> allergens, boolean isAvailable, List<Integer> rates, int rate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.isVegan = isVegan;
        this.dishImagePath = dishImagePath;
        this.image = image;
        this.isAvailable = isAvailable;
        this.rates = rates;
        this.rate = rate;
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

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Allergens> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<Allergens> allergens) {
        this.allergens = allergens;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public List<Integer> getRates(){ return rates;}

    public int getRate(){ return rate;}

    public void setRate(int rate){ this.rate = rate;}

    public String getDishImagePath() {
        return dishImagePath;
    }

    public void setDishImagePath(String imageURL) {
        this.dishImagePath = imageURL;
    }

    public boolean getImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Blob getDishImagefile() {
        return dishImagefile;
    }

    public void setDishImagefile(Blob dishImagefile) {
        this.dishImagefile = dishImagefile;
    }

    /**
     * Converts a URL to a Blob image.
     * @param webURL The URL of the image.
     * @return The corresponding Blob image.
     */
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

    /**
     * Converts a Blob image to a Base64 encoded string.
     * @param imageFile The Blob image.
     * @param dishE The Dish object.
     * @return Base64 encoded string of the image.
     * @throws SQLException if an error occurs accessing the Blob.
     */
    public String blobToString(Blob imageFile, Dish dishE) throws SQLException {
        Blob blob = dishE.getDishImagefile();
        byte[] bytes = blob.getBytes(1,(int) blob.length());
        String dishImage = Base64.getEncoder().encodeToString(bytes);
        return dishImage;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}