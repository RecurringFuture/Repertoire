package com.recurringfuture;

import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.SongRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service("songService")
public class SongService {

    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    private final SongRepo songRepo;

    @Autowired
    public SongService(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public List<Song> getSongs() {
        return songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Song getSong(int id) {
        return songRepo.getReferenceById(id);
    }

    public void saveCsvFile(File file) throws IOException {
        Collection<Song> songs = new ArrayList<>();
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
        for (CSVRecord record : records) {
            Song song = new Song();
            song.setTitle(record.get(0));
            setCreationAndModificationDate(song);
            songs.add(song);
        }
        in.close();
        songRepo.saveAll(songs);
    }

    public void saveSong(Song song) {
        setCreationAndModificationDate(song);
        logger.info("Saving song: " + song.getTitle());
        songRepo.save(song);
    }

    private void setCreationAndModificationDate(Song song) {
        LocalDate localDate = LocalDate.now();
        song.setCreationDate(localDate);
        song.setModificationDate(localDate);
    }

}
