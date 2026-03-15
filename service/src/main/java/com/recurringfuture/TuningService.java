package com.recurringfuture;

import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.TuningRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TuningService {

    private static final Logger logger = LoggerFactory.getLogger(TuningService.class);
    private final TuningRepo tuningRepo;

    @Autowired
    public TuningService(TuningRepo tuningRepo) {
        this.tuningRepo = tuningRepo;
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public void addTuning(Tuning tuning) {
        tuningRepo.save(tuning);
    }

    public void deleteTuning(Tuning tuning) {
        tuningRepo.delete(tuning);
    }

    public void updateTuning(Tuning tuning) {
        tuningRepo.save(tuning);
    }
}
