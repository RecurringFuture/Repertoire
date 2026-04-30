package com.recurringfuture;

import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.SongRepo;
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

    @Autowired
    public PracticeService(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public List<Song> getSongs() {
        return songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Song> getRandomSongs(int numberOfSongs) {
        List<Song> allSongs = songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
        Collections.shuffle(allSongs);
        return allSongs.subList(0, Math.min(allSongs.size(), numberOfSongs));
    }

    public int getTotalNumberOfSongs() {
        return Math.toIntExact(songRepo.count());
    }
}
