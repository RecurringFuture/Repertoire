package com.recurringfuture;

import com.recurringfuture.entity.Genre;
import com.recurringfuture.repository.GenreRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);
    private final GenreRepo genreRepo;

    @Autowired
    public GenreService(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public List<Genre> findAll() {
        return genreRepo.findAll();
    }

    public void save(Genre genre) {
        genreRepo.save(genre);
    }

    public void delete(Genre genre) {
        genreRepo.delete(genre);
    }

    public void update(Genre genre) {
        genreRepo.save(genre);
    }
}
