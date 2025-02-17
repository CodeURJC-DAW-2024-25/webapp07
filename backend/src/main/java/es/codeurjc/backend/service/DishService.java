package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;
}