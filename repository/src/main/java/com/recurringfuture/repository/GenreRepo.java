package com.recurringfuture.repository;

import com.recurringfuture.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre,Integer> {
}
