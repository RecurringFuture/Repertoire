package com.recurringfuture;

import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.TuningRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SettingsService {

    private static final Logger logger = LoggerFactory.getLogger(SettingsService.class);

    private final TuningRepo tuningRepo;
    private final GenreRepo genreRepo;

    @Autowired
    public SettingsService(TuningRepo tuningRepo, GenreRepo genreRepo) {
        this.tuningRepo = tuningRepo;
        this.genreRepo = genreRepo;
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public Tuning getTuning(Integer id) {
        return tuningRepo.findById(id).orElse(null);
    }

    public List<Genre> getGenres() {
        return genreRepo.findAll();
    }

    public Genre getGenre(Integer id) {
        return genreRepo.findById(id).orElse(null);
    }

    public void deleteTuning(Integer id) {
        tuningRepo.deleteById(id);
    }

    public void deleteGenre(Integer id) {
        genreRepo.deleteById(id);
    }

    public void saveTuning(Tuning tuning) {
        tuningRepo.save(tuning);
    }

    public void saveGenre(Genre genre) {
        genreRepo.save(genre);
    }

    public void updateTuning(Tuning tuning) {
        tuningRepo.save(tuning);
    }

    public void updateGenre(Genre genre) {
        genreRepo.save(genre);
    }
}
