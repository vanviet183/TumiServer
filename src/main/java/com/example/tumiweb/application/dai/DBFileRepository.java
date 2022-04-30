package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBFileRepository extends JpaRepository<DBFile, String> {
}
