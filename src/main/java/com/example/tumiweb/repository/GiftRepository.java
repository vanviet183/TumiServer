package com.example.tumiweb.repository;

import com.example.tumiweb.dao.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    Gift findByName(String title);
    Set<Gift> findAllByStatus(boolean status);

    List<Gift> findAllByNameContainingOrMarkContaining(String key, Long key2);

}
