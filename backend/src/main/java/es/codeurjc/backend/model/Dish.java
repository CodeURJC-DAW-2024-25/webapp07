@Entitie
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int price;
    private String ingredients;
    private Array<String> Allergens;
    private boolean isVegan;
    private String imageURL;

    public Dish(Long id, @NotNull @Size(min = 3, max = 50) String name, @Size(min = 0, max = 1024) String description, int price, @Size(min = 0, max = 1024) String ingredients, Array<String> allergens, boolean isVegan, String imageURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.isVegan = isVegan;
        this.imageURL = imageURL;
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

    public Array<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(Array<String> allergens) {
        this.allergens = allergens;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}


}