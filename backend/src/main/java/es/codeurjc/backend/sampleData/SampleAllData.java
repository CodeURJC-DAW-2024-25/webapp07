package es.codeurjc.backend.sampleData;

import es.codeurjc.backend.model.*;
import es.codeurjc.backend.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

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

        //dishCreation
        Dish croquetasJamon = new Dish("Croquetas de jamón", "Croquetas caseras de nuestra cocinera. Preparadas con el ingrediente secreto de la casa", 8, "Leche, harina, pan rallado, jamón, huevo", true, "https://th.bing.com/th/id/OIP.hVsuwAU-55Z_Qk649qroVAHaE4?rs=1&pid=ImgDetMain");
        croquetasJamon.setDishImagefile(croquetasJamon.URLtoBlob(croquetasJamon.getDishImagePath()));
        dishRepository.save(croquetasJamon);
        Dish tortillaEspanola = new Dish("Tortilla Española", "Tortilla de patatas tradicional con cebolla caramelizada", 6, "Patatas, huevos, cebolla, aceite de oliva, sal", true, "https://th.bing.com/th/id/OIP.1.jpg");
        tortillaEspanola.setDishImagefile(tortillaEspanola.URLtoBlob(tortillaEspanola.getDishImagePath()));
        dishRepository.save(tortillaEspanola);

        Dish paellaMariscos = new Dish("Paella de Mariscos", "Paella tradicional con una mezcla de mariscos frescos", 15, "Arroz, mariscos, caldo de pescado, azafrán, pimientos", false, "https://th.bing.com/th/id/OIP.2.jpg");
        paellaMariscos.setDishImagefile(paellaMariscos.URLtoBlob(paellaMariscos.getDishImagePath()));
        dishRepository.save(paellaMariscos);

        Dish gazpacho = new Dish("Gazpacho", "Sopa fría de tomate y vegetales frescos", 5, "Tomates, pepino, pimiento, ajo, aceite de oliva, vinagre", true, "https://th.bing.com/th/id/OIP.3.jpg");
        gazpacho.setDishImagefile(gazpacho.URLtoBlob(gazpacho.getDishImagePath()));
        dishRepository.save(gazpacho);

        Dish pulpoGallega = new Dish("Pulpo a la Gallega", "Pulpo cocido con pimentón y aceite de oliva", 12, "Pulpo, patatas, pimentón, aceite de oliva, sal", false, "https://th.bing.com/th/id/OIP.4.jpg");
        pulpoGallega.setDishImagefile(pulpoGallega.URLtoBlob(pulpoGallega.getDishImagePath()));
        dishRepository.save(pulpoGallega);

        Dish ensaladaMixta = new Dish("Ensalada Mixta", "Ensalada fresca con una variedad de vegetales", 7, "Lechuga, tomate, cebolla, aceitunas, atún, huevo", true, "https://th.bing.com/th/id/OIP.5.jpg");
        ensaladaMixta.setDishImagefile(ensaladaMixta.URLtoBlob(ensaladaMixta.getDishImagePath()));
        dishRepository.save(ensaladaMixta);

        Dish calamaresFritos = new Dish("Calamares Fritos", "Calamares rebozados y fritos, servidos con limón", 10, "Calamares, harina, aceite de oliva, limón, sal", false, "https://th.bing.com/th/id/OIP.6.jpg");
        calamaresFritos.setDishImagefile(calamaresFritos.URLtoBlob(calamaresFritos.getDishImagePath()));
        dishRepository.save(calamaresFritos);

        Dish albondigas = new Dish("Albóndigas", "Albóndigas de carne en salsa de tomate", 9, "Carne picada, pan rallado, huevo, tomate, cebolla, ajo", false, "https://th.bing.com/th/id/OIP.7.jpg");
        albondigas.setDishImagefile(albondigas.URLtoBlob(albondigas.getDishImagePath()));
        dishRepository.save(albondigas);

        Dish pimientosPadron = new Dish("Pimientos de Padrón", "Pimientos fritos con sal gruesa", 5, "Pimientos de Padrón, aceite de oliva, sal", true, "https://th.bing.com/th/id/OIP.8.jpg");
        pimientosPadron.setDishImagefile(pimientosPadron.URLtoBlob(pimientosPadron.getDishImagePath()));
        dishRepository.save(pimientosPadron);

        Dish chorizoSidra = new Dish("Chorizo a la Sidra", "Chorizo cocido en sidra asturiana", 8, "Chorizo, sidra, laurel", false, "https://th.bing.com/th/id/OIP.9.jpg");
        chorizoSidra.setDishImagefile(chorizoSidra.URLtoBlob(chorizoSidra.getDishImagePath()));
        dishRepository.save(chorizoSidra);

        Dish patatasBravas = new Dish("Patatas Bravas", "Patatas fritas con salsa brava picante", 6, "Patatas, tomate, pimentón, ajo, aceite de oliva", true, "https://th.bing.com/th/id/OIP.10.jpg");
        patatasBravas.setDishImagefile(patatasBravas.URLtoBlob(patatasBravas.getDishImagePath()));
        dishRepository.save(patatasBravas);

        Dish fabadaAsturiana = new Dish("Fabada Asturiana", "Guiso de fabes con chorizo y morcilla", 14, "Fabes, chorizo, morcilla, panceta, laurel", false, "https://th.bing.com/th/id/OIP.11.jpg");
        fabadaAsturiana.setDishImagefile(fabadaAsturiana.URLtoBlob(fabadaAsturiana.getDishImagePath()));
        dishRepository.save(fabadaAsturiana);

        Dish bacalaoVizcaina = new Dish("Bacalao a la Vizcaína", "Bacalao en salsa de pimientos y tomate", 13, "Bacalao, pimientos, tomate, cebolla, ajo", false, "https://th.bing.com/th/id/OIP.12.jpg");
        bacalaoVizcaina.setDishImagefile(bacalaoVizcaina.URLtoBlob(bacalaoVizcaina.getDishImagePath()));
        dishRepository.save(bacalaoVizcaina);

        Dish empanadaGallega = new Dish("Empanada Gallega", "Empanada rellena de atún y pimientos", 7, "Harina, atún, pimientos, cebolla, tomate", true, "https://th.bing.com/th/id/OIP.13.jpg");
        empanadaGallega.setDishImagefile(empanadaGallega.URLtoBlob(empanadaGallega.getDishImagePath()));
        dishRepository.save(empanadaGallega);

        Dish gambasAjillo = new Dish("Gambas al Ajillo", "Gambas salteadas con ajo y guindilla", 11, "Gambas, ajo, guindilla, aceite de oliva, perejil", false, "https://th.bing.com/th/id/OIP.14.jpg");
        gambasAjillo.setDishImagefile(gambasAjillo.URLtoBlob(gambasAjillo.getDishImagePath()));
        dishRepository.save(gambasAjillo);

        Dish flanCasero = new Dish("Flan Casero", "Flan de huevo con caramelo", 5, "Huevos, leche, azúcar, vainilla", true, "https://th.bing.com/th/id/OIP.15.jpg");
        flanCasero.setDishImagefile(flanCasero.URLtoBlob(flanCasero.getDishImagePath()));
        dishRepository.save(flanCasero);
    }

}
