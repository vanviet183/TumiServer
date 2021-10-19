package com.example.tumiweb.repository;

import com.example.tumiweb.model.Gift;
import com.example.tumiweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    Gift findByName(String title);
    Set<Gift> findAllByStatus(boolean status);
    //test 1 trong 2
//    Set<Gift> findAllByUsersIn(Set<User> users);

}
