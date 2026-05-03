package com.recurringfuture;

import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.PracticeSet;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.PracticeSetRepo;
import com.recurringfuture.repository.SongRepo;
import com.recurringfuture.repository.TuningRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PracticeService {

    private final SongRepo songRepo;
    private final PracticeSetRepo practiceSetRepo;
    private final GenreRepo genreRepo;
    private final TuningRepo tuningRepo;

    @Autowired
    public PracticeService(SongRepo songRepo, PracticeSetRepo practiceSetRepo, GenreRepo genreRepo, TuningRepo tuningRepo) {
        this.songRepo = songRepo;
        this.practiceSetRepo = practiceSetRepo;
        this.genreRepo = genreRepo;
        this.tuningRepo = tuningRepo;
    }

    public List<Song> getSongs() {
        return songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Song getSong(int id) {
        return songRepo.getReferenceById(id);
    }

    public List<Song> getRandomSongs(int numberOfSongs) {
        List<Song> allSongs = songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
        Collections.shuffle(allSongs);
        return allSongs.subList(0, Math.min(allSongs.size(), numberOfSongs));
    }

    public int getTotalNumberOfSongs() {
        return Math.toIntExact(songRepo.count());
    }

    public List<PracticeSet> getPracticeSets() {
        return practiceSetRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Genre> findAll() {
        return genreRepo.findAll();
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

}
