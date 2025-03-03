package es.codeurjc.backend.enums;

public enum Allergens {
    ALTRAMUZ("/img/allergen_symbols/allergen_symbol_altramuz.png"),
    //APIO("../static/img/allergen_symbols/allergen_symbol_apio.png"),
    GLUTEN("/img/allergen_symbols/allergen_symbol_gluten.png"),
    CRUSTACEAN("/img/allergen_symbols/allergen_symbol_crustacean.png"),
    DAIRY("/img/allergen_symbols/allergen_symbol_dairy.png"),
    EGGS("/img/allergen_symbols/allergen_symbol_crustacean.png"),
    FISH("/img/allergen_symbols/allergen_symbol_fish.png"),
    MOLLUSKS("/img/allergen_symbols/allergen_symbol_mollusks.png"),
    MUSTARD("/img/allergen_symbols/allergen_symbol_mustard.png"),
    NUTS("/img/allergen_symbols/allergen_symbol_nuts.png"),
    PEANUT(("/img/allergen_symbols/allergen_symbol_peanut.png")),
    SESAME("/img/allergen_symbols/allergen_symbol_sesame.png"),
    SOYBEANS("/img/allergen_symbols/allergen_symbol_soybeans.png"),
    SULFITES("/img/allergen_symbols/allergen_symbol_sulfites.png"),
    NONE("/img/allergen_symbols/none-icon-23.jpg");

    private String imageUrl;

    Allergens(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
