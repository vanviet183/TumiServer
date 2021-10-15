package com.example.tumiweb;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TumiWebApplication {

    @Value("${cloudinary.url}")
    private String CLOUDINARY_URL;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(CLOUDINARY_URL);
    }

    public static void main(String[] args) {
        SpringApplication.run(TumiWebApplication.class, args);
    }

}
