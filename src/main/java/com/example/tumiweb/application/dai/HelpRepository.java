package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {
}
