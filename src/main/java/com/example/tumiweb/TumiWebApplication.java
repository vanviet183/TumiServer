package com.example.tumiweb;

import com.example.tumiweb.storage.StorageProperties;
import com.example.tumiweb.storage.IStorageService;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class TumiWebApplication {

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
            storageService.init();
        };
    }
}
