package es.codeurjc.backend.sampleData;

import es.codeurjc.backend.enums.Allergens;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.DishRepository;
import es.codeurjc.backend.repository.UserRepository;
import es.codeurjc.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Service
public class SampleAllData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DishRepository dishRepository;


    @PostConstruct
    public void init() throws SQLException {


        // UserCreation
        userRepository.save(new User("user", passwordEncoder.encode("pass"), false,"USER"));
        userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), false, "USER", "ADMIN"));
        userRepository.save(new User("banned", passwordEncoder.encode("pass"), true, "USER"));

        // Dish creation
        Dish croquetasJamon = new Dish(
                "Croquetas de jamón",
                "Croquetas caseras de nuestra cocinera. Preparadas con el ingrediente secreto de la casa",
                8,
                Arrays.asList("Leche", "harina", "pan rallado", "jamón", "huevo"),
                true,
                "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.GLUTEN, Allergens.EGGS, Allergens.DAIRY, Allergens.ALTRAMUZ),true
        );
        croquetasJamon.setDishImagefile(croquetasJamon.URLtoBlob(croquetasJamon.getDishImagePath()));
        dishRepository.save(croquetasJamon);

        Dish tortillaEspanola = new Dish(
                "Tortilla Española",
                "Tortilla de patatas tradicional con cebolla caramelizada",
                6,
                Arrays.asList("Patatas", "huevos", "cebolla", "aceite de oliva", "sal"),
                true,
                "https://th.bing.com/th/id/OIP.m6-FnJ8zbiBuhoqBAsQZbwAAAA?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.EGGS),true
        );
        tortillaEspanola.setDishImagefile(tortillaEspanola.URLtoBlob(tortillaEspanola.getDishImagePath()));
        dishRepository.save(tortillaEspanola);

        Dish paellaMariscos = new Dish(
                "Paella de Mariscos",
                "Paella tradicional con una mezcla de mariscos frescos",
                15,
                Arrays.asList("Arroz", "mariscos", "caldo de pescado", "azafrán", "pimientos"),
                false,
                "https://recetinas.com/wp-content/uploads/2022/06/paella-de-marisco.jpg",true,
                List.of(Allergens.FISH),true
        );
        paellaMariscos.setDishImagefile(paellaMariscos.URLtoBlob(paellaMariscos.getDishImagePath()));
        dishRepository.save(paellaMariscos);

        Dish gazpacho = new Dish(
                "Gazpacho",
                "Sopa fría de tomate y vegetales frescos",
                5,
                Arrays.asList("Tomates", "pepino", "pimiento", "ajo", "aceite de oliva", "vinagre"),
                true,
                "https://animalgourmet.com/wp-content/uploads/2023/05/Gazpacho3-scaled.jpeg",true,
                List.of(Allergens.NONE),true
        );
        gazpacho.setDishImagefile(gazpacho.URLtoBlob(gazpacho.getDishImagePath()));
        dishRepository.save(gazpacho);

        Dish pulpoGallega = new Dish(
                "Pulpo a la Gallega",
                "Pulpo cocido con pimentón y aceite de oliva",
                12,
                Arrays.asList("Pulpo", "patatas", "pimentón", "aceite de oliva", "sal"),
                false,
                "https://th.bing.com/th/id/OIP.DV3DDF4KJ4pofQ061akwwgHaE7?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.FISH),true
        );
        pulpoGallega.setDishImagefile(pulpoGallega.URLtoBlob(pulpoGallega.getDishImagePath()));
        dishRepository.save(pulpoGallega);

        Dish ensaladaMixta = new Dish(
                "Ensalada Mixta",
                "Ensalada fresca con una variedad de vegetales",
                7,
                Arrays.asList("Lechuga", "tomate", "cebolla", "aceitunas", "atún", "huevo"),
                true,
                "https://th.bing.com/th/id/R.fb84e8df3891046bc50627f9147d7a73?rik=LMk7IexCc%2bZ3UQ&pid=ImgRaw&r=0",true,
                List.of(Allergens.FISH, Allergens.EGGS),true
        );
        ensaladaMixta.setDishImagefile(ensaladaMixta.URLtoBlob(ensaladaMixta.getDishImagePath()));
        dishRepository.save(ensaladaMixta);

        Dish calamaresFritos = new Dish(
                "Calamares Fritos",
                "Calamares rebozados y fritos, servidos con limón",
                10,
                Arrays.asList("Calamares", "harina", "aceite de oliva", "limón", "sal"),
                false,
                "https://content-cocina.lecturas.com/medio/2022/05/15/calamares_12ab6b0e_1200x1200.jpg",true,
                List.of(Allergens.FISH, Allergens.GLUTEN),true
        );
        calamaresFritos.setDishImagefile(calamaresFritos.URLtoBlob(calamaresFritos.getDishImagePath()));
        dishRepository.save(calamaresFritos);

        Dish albondigas = new Dish(
                "Albóndigas",
                "Albóndigas de carne en salsa de tomate",
                9,
                Arrays.asList("Carne picada", "pan rallado", "huevo", "tomate", "cebolla", "ajo"),
                false,
                "https://th.bing.com/th/id/OIP.ybJY43zS0AwqOuguVREnQgHaE8?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.GLUTEN, Allergens.EGGS),true
        );
        albondigas.setDishImagefile(albondigas.URLtoBlob(albondigas.getDishImagePath()));
        dishRepository.save(albondigas);

        Dish pimientosPadron = new Dish(
                "Pimientos de Padrón",
                "Pimientos fritos con sal gruesa",
                5,
                Arrays.asList("Pimientos de Padrón", "aceite de oliva", "sal"),
                true,
                "https://th.bing.com/th/id/R.d0cc3cbb524003be3398c5cca6cde047?rik=cSfcMlUmM2XdUg&pid=ImgRaw&r=0",true,
                List.of(Allergens.NONE),true
        );
        pimientosPadron.setDishImagefile(pimientosPadron.URLtoBlob(pimientosPadron.getDishImagePath()));
        dishRepository.save(pimientosPadron);

        Dish chorizoSidra = new Dish(
                "Chorizo a la Sidra",
                "Chorizo cocido en sidra asturiana",
                8,
                Arrays.asList("Chorizo", "sidra", "laurel"),
                false,
                "https://th.bing.com/th/id/R.d3ed939910203c3ab7657b0290070db0?rik=YXRr8oRIRWSpAA&pid=ImgRaw&r=0",true,
                List.of(Allergens.NONE),true
        );
        chorizoSidra.setDishImagefile(chorizoSidra.URLtoBlob(chorizoSidra.getDishImagePath()));
        dishRepository.save(chorizoSidra);

        Dish patatasBravas = new Dish(
                "Patatas Bravas",
                "Patatas fritas con salsa brava picante",
                6,
                Arrays.asList("Patatas", "tomate", "pimentón", "ajo", "aceite de oliva"),
                true,
                "https://th.bing.com/th/id/OIP.2c6Gy8jW9Vp8qzdqiln8WwHaE8?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.NONE),true
        );
        patatasBravas.setDishImagefile(patatasBravas.URLtoBlob(patatasBravas.getDishImagePath()));
        dishRepository.save(patatasBravas);

        Dish fabadaAsturiana = new Dish(
                "Fabada Asturiana",
                "Guiso de fabes con chorizo y morcilla",
                14,
                Arrays.asList("Fabes", "chorizo", "morcilla", "panceta", "laurel"),
                false,
                "https://th.bing.com/th/id/OIP.zizD368b0P6bLEnUtGYY6gHaFj?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.NONE),true
        );
        fabadaAsturiana.setDishImagefile(fabadaAsturiana.URLtoBlob(fabadaAsturiana.getDishImagePath()));
        dishRepository.save(fabadaAsturiana);

        Dish bacalaoVizcaina = new Dish(
                "Bacalao a la Vizcaína",
                "Bacalao en salsa de pimientos y tomate",
                13,
                Arrays.asList("Bacalao", "pimientos", "tomate", "cebolla", "ajo"),
                false,
                "https://th.bing.com/th/id/OIP.zajHKCzQXbM5TJcuYvTFeAHaFj?rs=1&pid=ImgDetMain",true,
                List.of(Allergens.FISH),true
        );
        bacalaoVizcaina.setDishImagefile(bacalaoVizcaina.URLtoBlob(bacalaoVizcaina.getDishImagePath()));
        dishRepository.save(bacalaoVizcaina);

        Dish empanadaGallega = new Dish(
                "Empanada Gallega","Empanada rellena de atún y pimientos",7,
                Arrays.asList("Harina", "atún", "pimientos", "cebolla", "tomate"),
                true,"https://th.bing.com/th/id/R.252c45b238d61ac2e6910011a51299b2?rik=IE7cr2Mv4SpotQ&riu=http%3a%2f%2fwww.galiciatips.com%2fnl%2ffiles%2f2019%2f12%2fempanada-tonijn.jpg&ehk=Mm7LyQByYU785%2bHZ1TZefcy8h5rxV3O1fO7S60r%2bn34%3d&risl=&pid=ImgRaw&r=0", true,
                List.of(Allergens.FISH),true
        );
        empanadaGallega.setDishImagefile(empanadaGallega.URLtoBlob(empanadaGallega.getDishImagePath()));
        dishRepository.save(empanadaGallega);
    }


}

