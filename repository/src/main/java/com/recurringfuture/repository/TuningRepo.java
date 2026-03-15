package com.recurringfuture.repository;

import com.recurringfuture.entity.Tuning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuningRepo extends JpaRepository<Tuning,Integer> {
}
