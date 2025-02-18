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
    }

}
