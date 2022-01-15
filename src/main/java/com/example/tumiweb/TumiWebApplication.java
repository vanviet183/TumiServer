package com.example.tumiweb;

import com.example.tumiweb.dao.User;
import com.example.tumiweb.repository.UserRepository;
import com.example.tumiweb.storage.StorageProperties;
import com.example.tumiweb.storage.IStorageService;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class TumiWebApplication {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Slugify slugify() {
        return new Slugify();
    }

    public static void main(String[] args) {
        SpringApplication.run(TumiWebApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IStorageService storageService) {
        return (args) -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            storageService.init();
            if(userRepository.count() == 0) {
                userRepository.save(
                        new User(null,
                                "admin",
                                passwordEncoder.encode("admin"),
                                "Admin",
                                "huannd0101@gmail.com",
                                "0375417808",
                                null,
                                0L,
                                "01-01-2001",
                                true
                        ));
            }
        };
    }
}
