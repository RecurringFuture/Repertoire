package com.recurringfuture;

import com.recurringfuture.entity.PracticeSet;
import com.recurringfuture.entity.PracticeSetSong;
import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.PracticeSetRepo;
import com.recurringfuture.repository.PracticeSongRepo;
import com.recurringfuture.repository.SongRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PracticeSetService {

    private static final Logger logger = LoggerFactory.getLogger(PracticeSetService.class);

    private final PracticeSetRepo practiceSetRepo;
    private final PracticeSongRepo practiceSongRepo;
    private final SongRepo songRepo;

    @Autowired
    public PracticeSetService(PracticeSetRepo practiceSetRepo, PracticeSongRepo practiceSongRepo, SongRepo songRepo) {
        this.practiceSetRepo = practiceSetRepo;
        this.practiceSongRepo = practiceSongRepo;
        this.songRepo = songRepo;
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

    public List<Song> getSongsToPractice(int practiceSetId) {
        List<PracticeSetSong> practiceSetSongs = practiceSongRepo.findByPracticeSetId(practiceSetId);
        List<Integer> songIds = practiceSetSongs.stream()
                .map(PracticeSetSong::getSongId)
                .toList();
        return songRepo.findAllById(songIds);
    }

    public List<Song> getAvailableSongsToPractice(int practiceSetId) {
        List<Song> practiceSetSongs = getSongsToPractice(practiceSetId);
        List<Integer> practiceSetSongIds = practiceSetSongs.stream().map(Song::getId).toList();
        List<Song> allSongs = songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
        return allSongs.stream()
                .filter(s -> !practiceSetSongIds.contains(s.getId()))
                .collect(Collectors.toList());
    }

    public void addSongToPracticeSet(int practiceSetId, int songId) {
        PracticeSetSong practiceSetSong = new PracticeSetSong();
        practiceSetSong.setPracticeSetId(practiceSetId);
        practiceSetSong.setSongId(songId);
        practiceSongRepo.save(practiceSetSong);
    }

    @Transactional
    public void removeSongFromPracticeSet(int practiceSetId, int songId) {
        practiceSongRepo.deleteByPracticeSetIdAndSongId(practiceSetId, songId);
    }
}
