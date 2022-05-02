package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {

  List<GiftOrder> findAllByDeleteFlag(boolean flag);

}
