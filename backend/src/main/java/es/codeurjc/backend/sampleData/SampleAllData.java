package es.codeurjc.backend.sampleData;

import es.codeurjc.backend.model.*;
import es.codeurjc.backend.repository.*;
import es.codeurjc.backend.enums.Allergens;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Service
public class SampleAllData {

    @Autowired
    private DishRepository dishRepository;
   //@Autowired
   //private UserRepository userRepository;

    //@Autowired
    //private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() throws SQLException {


        //userCreation
        //userRepository.save(new User("user", passwordEncoder.encode("pass"), "USER"));
        //userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));

        // dishCreation
        Dish croquetasJamon = new Dish(
                "Croquetas de jamón",
                "Croquetas caseras de nuestra cocinera. Preparadas con el ingrediente secreto de la casa",
                8,
                Arrays.asList("Leche", "harina", "pan rallado", "jamón", "huevo"),
                true,
                "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain",
                List.of(Allergens.GLUTEN, Allergens.EGGS, Allergens.DAIRY)
        );
        croquetasJamon.setDishImagefile(croquetasJamon.URLtoBlob(croquetasJamon.getDishImagePath()));
        dishRepository.save(croquetasJamon);

        Dish tortillaEspanola = new Dish(
                "Tortilla Española",
                "Tortilla de patatas tradicional con cebolla caramelizada",
                6,
                Arrays.asList("Patatas", "huevos", "cebolla", "aceite de oliva", "sal"),
                true,
                "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain",
                List.of(Allergens.EGGS)
        );
        tortillaEspanola.setDishImagefile(tortillaEspanola.URLtoBlob(tortillaEspanola.getDishImagePath()));
        dishRepository.save(tortillaEspanola);

        Dish paellaMariscos = new Dish(
                "Paella de Mariscos",
                "Paella tradicional con una mezcla de mariscos frescos",
                15,
                Arrays.asList("Arroz", "mariscos", "caldo de pescado", "azafrán", "pimientos"),
                false,
                "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain",
                List.of(Allergens.FISH)
        );
        paellaMariscos.setDishImagefile(paellaMariscos.URLtoBlob(paellaMariscos.getDishImagePath()));
        dishRepository.save(paellaMariscos);

        Dish gazpacho = new Dish(
                "Gazpacho",
                "Sopa fría de tomate y vegetales frescos",
                5,
                Arrays.asList("Tomates", "pepino", "pimiento", "ajo", "aceite de oliva", "vinagre"),
                true,
                "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain",
                List.of(Allergens.NONE)
        );
        gazpacho.setDishImagefile(gazpacho.URLtoBlob(gazpacho.getDishImagePath()));
        dishRepository.save(gazpacho);

        Dish pulpoGallega = new Dish(
                "Pulpo a la Gallega",
                "Pulpo cocido con pimentón y aceite de oliva",
                12,
                Arrays.asList("Pulpo", "patatas", "pimentón", "aceite de oliva", "sal"),
                false,
                "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain",
                List.of(Allergens.FISH)
        );
        pulpoGallega.setDishImagefile(pulpoGallega.URLtoBlob(pulpoGallega.getDishImagePath()));
        dishRepository.save(pulpoGallega);

        Dish ensaladaMixta = new Dish(
                "Ensalada Mixta",
                "Ensalada fresca con una variedad de vegetales",
                7,
                Arrays.asList("Lechuga", "tomate", "cebolla", "aceitunas", "atún", "huevo"),
                true,
                "https://th.bing.com/th/id/OIP.5.jpg",
                List.of(Allergens.FISH, Allergens.EGGS)
        );
        ensaladaMixta.setDishImagefile(ensaladaMixta.URLtoBlob(ensaladaMixta.getDishImagePath()));
        dishRepository.save(ensaladaMixta);

        Dish calamaresFritos = new Dish(
                "Calamares Fritos",
                "Calamares rebozados y fritos, servidos con limón",
                10,
                Arrays.asList("Calamares", "harina", "aceite de oliva", "limón", "sal"),
                false,
                "https://pixabay.com/es/images/search/calamares%20fritos/",
                List.of(Allergens.FISH, Allergens.GLUTEN)
        );
        calamaresFritos.setDishImagefile(calamaresFritos.URLtoBlob(calamaresFritos.getDishImagePath()));
        dishRepository.save(calamaresFritos);

        Dish albondigas = new Dish(
                "Albóndigas",
                "Albóndigas de carne en salsa de tomate",
                9,
                Arrays.asList("Carne picada", "pan rallado", "huevo", "tomate", "cebolla", "ajo"),
                false,
                "https://unsplash.com/es/s/fotos/albondigas",
                List.of(Allergens.GLUTEN, Allergens.EGGS)
        );
        albondigas.setDishImagefile(albondigas.URLtoBlob(albondigas.getDishImagePath()));
        dishRepository.save(albondigas);

        Dish pimientosPadron = new Dish(
                "Pimientos de Padrón",
                "Pimientos fritos con sal gruesa",
                5,
                Arrays.asList("Pimientos de Padrón", "aceite de oliva", "sal"),
                true,
                "https://www.shutterstock.com/search/pimientos-de-padr%C3%B3n",
                List.of(Allergens.NONE)
        );
        pimientosPadron.setDishImagefile(pimientosPadron.URLtoBlob(pimientosPadron.getDishImagePath()));
        dishRepository.save(pimientosPadron);

        Dish chorizoSidra = new Dish(
                "Chorizo a la Sidra",
                "Chorizo cocido en sidra asturiana",
                8,
                Arrays.asList("Chorizo", "sidra", "laurel"),
                false,
                "https://www.shutterstock.com/search/chorizo-la-sidra",
                List.of(Allergens.NONE)
        );
        chorizoSidra.setDishImagefile(chorizoSidra.URLtoBlob(chorizoSidra.getDishImagePath()));
        dishRepository.save(chorizoSidra);

        Dish patatasBravas = new Dish(
                "Patatas Bravas",
                "Patatas fritas con salsa brava picante",
                6,
                Arrays.asList("Patatas", "tomate", "pimentón", "ajo", "aceite de oliva"),
                true,
                "https://www.shutterstock.com/search/patatas-bravas",
                List.of(Allergens.NONE)
        );
        patatasBravas.setDishImagefile(patatasBravas.URLtoBlob(patatasBravas.getDishImagePath()));
        dishRepository.save(patatasBravas);

        Dish fabadaAsturiana = new Dish(
                "Fabada Asturiana",
                "Guiso de fabes con chorizo y morcilla",
                14,
                Arrays.asList("Fabes", "chorizo", "morcilla", "panceta", "laurel"),
                false,
                "https://www.gettyimages.com/photos/gazpacho",
                List.of(Allergens.NONE)
        );
        fabadaAsturiana.setDishImagefile(fabadaAsturiana.URLtoBlob(fabadaAsturiana.getDishImagePath()));
        dishRepository.save(fabadaAsturiana);

        Dish bacalaoVizcaina = new Dish(
                "Bacalao a la Vizcaína",
                "Bacalao en salsa de pimientos y tomate",
                13,
                Arrays.asList("Bacalao", "pimientos", "tomate", "cebolla", "ajo"),
                false,
                "https://www.gettyimages.com/photos/gazpacho",
                List.of(Allergens.FISH)
        );
        bacalaoVizcaina.setDishImagefile(bacalaoVizcaina.URLtoBlob(bacalaoVizcaina.getDishImagePath()));
        dishRepository.save(bacalaoVizcaina);

        Dish empanadaGallega = new Dish(
                "Empanada Gallega","Empanada rellena de atún y pimientos",7,
                Arrays.asList("Harina", "atún", "pimientos", "cebolla", "tomate"),
                true,"https://www.gettyimages.com/photos/gazpacho",
                List.of(Allergens.FISH)
        );
        empanadaGallega.setDishImagefile(empanadaGallega.URLtoBlob(empanadaGallega.getDishImagePath()));
        dishRepository.save(empanadaGallega);
    }

}
