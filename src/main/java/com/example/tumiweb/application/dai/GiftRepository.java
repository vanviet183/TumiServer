package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {

  Gift findByName(String title);

  Set<Gift> findAllByDeleteFlag(boolean flag);

  List<Gift> findAllByNameContainingOrMarkContaining(@NotBlank String name, @Min(0) @Max(100) Long mark);


}
