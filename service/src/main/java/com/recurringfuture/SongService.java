package com.recurringfuture;

import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("songService")
public class SongService {

    private final SongRepo songRepo;

    @Autowired
    public SongService(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public List<Song> getSongs() {
        return songRepo.findAll();
    }

    public Song getSong(int id) {
        return songRepo.getReferenceById(id);
    }
}
