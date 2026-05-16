package com.recurringfuture;

import com.recurringfuture.dto.TitleDTO;
import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.TuningRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public SettingsService(TuningRepo tuningRepo, GenreRepo genreRepo, ModelMapper modelMapper) {
        this.tuningRepo = tuningRepo;
        this.genreRepo = genreRepo;
        this.modelMapper = modelMapper;
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public TitleDTO getTuning(Integer id) {

        return modelMapper.map(tuningRepo.findById(id).orElse(null), TitleDTO.class);
    }

    public List<Genre> getGenres() {
        return genreRepo.findAll();
    }

    public TitleDTO getGenre(Integer id) {

        return modelMapper.map(genreRepo.findById(id).orElse(null), TitleDTO.class);
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

    public void updateTuning(TitleDTO titleDTO) {
        Tuning tuning = modelMapper.map(titleDTO, Tuning.class);
        tuningRepo.save(tuning);
    }

    public void updateGenre(TitleDTO titleDTO) {
        Genre genre = modelMapper.map(titleDTO, Genre.class);
        logger.info("Genre: " + genre);
        genreRepo.save(genre);
    }
}
