package com.recurringfuture;

import com.recurringfuture.entity.PracticeSet;
import com.recurringfuture.repository.PracticeSetRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PracticeService {

    private static final Logger logger = LoggerFactory.getLogger(PracticeService.class);

    private final PracticeSetRepo practiceSetRepo;

    @Autowired
    public PracticeService(PracticeSetRepo practiceSetRepo) {
        this.practiceSetRepo = practiceSetRepo;
    }

    public List<PracticeSet> getPracticeSets() {
        return practiceSetRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public PracticeSet getPracticeSet(int id) {
        return practiceSetRepo.getReferenceById(id);
    }

    public void savePracticeSet(PracticeSet practiceSet) {
        logger.info("Saving project: {}", practiceSet.getTitle());
        practiceSetRepo.save(practiceSet);
    }

    public void deletePracticeSet(int id) {
        practiceSetRepo.deleteById(id);
    }

    public void updatePracticeSet(PracticeSet practiceSet) {
        logger.info("Updating practiceSet: " + practiceSet.getTitle());
        practiceSetRepo.save(practiceSet);
    }

}
