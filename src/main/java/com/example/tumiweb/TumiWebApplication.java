package com.example.tumiweb;

import com.example.tumiweb.application.constants.AuthenticationProvider;
import com.example.tumiweb.application.constants.RoleConstant;
import com.example.tumiweb.application.dai.RoleRepository;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.storage.IStorageService;
import com.example.tumiweb.application.storage.StorageProperties;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
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

import java.util.Set;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class TumiWebApplication {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;

  public static void main(String[] args) {
    SpringApplication.run(TumiWebApplication.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public Slugify slugify() {
    return new Slugify();
  }

  @Bean
  CommandLineRunner init(IStorageService storageService) {
    return (args) -> {
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      storageService.init();

      if (roleRepository.count() == 0) {
        roleRepository.save(new Role(RoleConstant.ADMIN_NAME, RoleConstant.DES_ADMIN_ROLE, null));
        roleRepository.save(new Role(RoleConstant.TEACHER_NAME, RoleConstant.DES_TEACHER_ROLE, null));
        roleRepository.save(new Role(RoleConstant.STUDENT_NAME, RoleConstant.DES_STUDENT_ROLE, null));
      }

      if (userRepository.count() == 0) {
        User user = new User(null, "admin", passwordEncoder.encode("admin"), "Admin",
            "huannd0101@gmail.com", "0375417808", null, 0L, "01-01-2001", false);
        user.setRoles(Set.copyOf(roleRepository.findAll()));
        user.setAuthProvider(AuthenticationProvider.SYSTEM);
        userRepository.save(user);
      }
    };
  }

}
