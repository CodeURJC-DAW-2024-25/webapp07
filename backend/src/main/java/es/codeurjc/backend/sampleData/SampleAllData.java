package es.codeurjc.backend.sampleData;

//import es.codeurjc.backend.model.User;
//import es.codeurjc.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

@Service
public class SampleAllData {

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws SQLException {


        //userCreation
        userRepository.save(new User("user", passwordEncoder.encode("pass"), "USER"));
        userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
        //userRepository.save(new User("username", passwordEncoder.encode("pass"), "name", "", 2025-03-05, "", "", email, "USER"));



    }
}