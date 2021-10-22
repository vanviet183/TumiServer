package com.example.tumiweb.repository;

import com.example.tumiweb.dao.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {
    Set<GiftOrder> findAllByStatus(boolean status);
}
