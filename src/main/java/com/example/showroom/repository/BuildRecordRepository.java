package com.example.showroom.repository;

import com.example.showroom.entity.BuildRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRecordRepository extends JpaRepository<BuildRecord, Long> {
}