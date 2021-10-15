package com.example.tumiweb.repository;

import com.example.tumiweb.model.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {
}
