package com.recurringfuture;

import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.SongRepo;
import com.recurringfuture.repository.TuningRepo;
import com.recurringfuture.repository.data.RepertoireData;
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
import java.util.Objects;

@Slf4j
@Service("songService")
public class SongService {

    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    private final SongRepo songRepo;
    private final GenreRepo genreRepo;
    private final TuningRepo tuningRepo;

    @Autowired
    public SongService(SongRepo songRepo, GenreRepo genreRepo, TuningRepo tuningRepo) {
        this.songRepo = songRepo;
        this.genreRepo = genreRepo;
        this.tuningRepo = tuningRepo;
    }

    public List<Song> getSongs() {
        return songRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Song getSong(int id) {
        return songRepo.getReferenceById(id);
    }

    public void deleteSong(int id) {
        songRepo.deleteById(id);
    }

    public void updateSong(Song song) {
        LocalDate localDate = LocalDate.now();
        song.setModificationDate(localDate);
        songRepo.save(song);
    }

    public void saveCsvFile(File file) throws IOException {
        Collection<Song> songs = new ArrayList<>();
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
        for (CSVRecord record : records) {
            Song song = new Song();
            song.setTitle(record.get(0));
            LocalDate localDate = LocalDate.now();
            song.setCreationDate(localDate);
            song.setModificationDate(localDate);
            songs.add(song);
        }
        in.close();
        songRepo.saveAll(songs);
    }

    public void saveSong(Song song) {
        LocalDate localDate = LocalDate.now();
        song.setModificationDate(localDate);
        song.setCreationDate(localDate);
        logger.info("Saving song: " + song.getTitle());
        songRepo.save(song);
    }

    public List<Genre> findAll() {
        return genreRepo.findAll();
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public List<String> getKeysUsed() {
        List<Song> songs = songRepo.findAll();
        List<String> keyIndex = songs.stream().map(Song::getKey).distinct().filter(Objects::nonNull).toList();
        List<Integer> listOfInteger = keyIndex.stream().map(Integer::valueOf).toList();
        List<String> keysUsed = new ArrayList<>();
        for (int i = 0; i < listOfInteger.size(); i++) {
            keysUsed.add(RepertoireData.getKeys().get(listOfInteger.get(i)));
        }
        return keysUsed;
    }

    public List<Tuning> getTuningsUsed() {
        List<Song> songs = songRepo.findAll();
        List<String> tuningIndex = songs.stream().map(Song::getTuning).distinct().filter(Objects::nonNull).toList();
        List<Integer> listOfInteger = tuningIndex.stream().map(Integer::valueOf).toList();
        return tuningRepo.findAllById(listOfInteger);
    }

}
