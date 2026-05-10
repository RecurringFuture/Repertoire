package com.recurringfuture;

import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.TuningRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
