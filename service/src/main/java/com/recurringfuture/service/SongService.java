package com.recurringfuture.service;

import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.SongRepo;
import com.recurringfuture.repository.TuningRepo;
import com.recurringfuture.data.RepertoireData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
        if (song == null || song.getTitle() == null || song.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Song title must not be empty or null.");
        }
        LocalDate localDate = LocalDate.now();
        song.setModificationDate(localDate);
        song.setCreationDate(localDate);
        logger.info("Saving song: {}", song.getTitle());
        songRepo.save(song);
    }

    public boolean songExists(String title) {
        if (title == null || title.isBlank()) {
            return false;
        }
        return songRepo.findByTitleIgnoreCase(title.trim()) != null;
    }

    public List<Genre> findAll() {
        return genreRepo.findAll();
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public List<String> getKeysUsed() {
        List<Song> songs = songRepo.findAll();
        List<String> keyIndex = songs.stream()
                .map(Song::getKey)
                .filter(k -> k != null && !k.isBlank())
                .distinct()
                .toList();
        List<String> keysUsed = new ArrayList<>();
        for (String key : keyIndex) {
            try {
                int index = Integer.parseInt(key);
                if (index > 0 && index < RepertoireData.getKeys().size()) {
                    String keyName = RepertoireData.getKeys().get(index);
                    if (!keysUsed.contains(keyName)) {
                        keysUsed.add(keyName);
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return keysUsed;
    }

    public List<Tuning> getTuningsUsed() {
        List<Song> songs = songRepo.findAll();
        List<String> tuningIndex = songs.stream()
                .map(Song::getTuning)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .toList();
        List<Integer> listOfInteger = new ArrayList<>();
        for (String tuning : tuningIndex) {
            try {
                listOfInteger.add(Integer.parseInt(tuning));
            } catch (NumberFormatException ignored) {
            }
        }
        return tuningRepo.findAllById(listOfInteger);
    }

    public List<String> getStatesUsed() {
        List<Song> songs = songRepo.findAll();
        List<Integer> stateIndex = songs.stream()
                .map(Song::getState)
                .filter(s -> s > 0 && s < RepertoireData.getStates().size())
                .distinct()
                .toList();
        List<String> statesUsed = new ArrayList<>();
        for (Integer index : stateIndex) {
            String stateName = RepertoireData.getStates().get(index);
            if (!statesUsed.contains(stateName)) {
                statesUsed.add(stateName);
            }
        }
        return statesUsed;
    }

    public List<String> getCapoUsed() {
        List<Song> songs = songRepo.findAll();
        List<Integer> capoIndex = songs.stream()
                .map(Song::getCapo)
                .filter(c -> c > 0 && c < RepertoireData.getCapoPositions().size())
                .distinct()
                .toList();
        List<String> capoUsed = new ArrayList<>();
        for (Integer index : capoIndex) {
            String capoPos = RepertoireData.getCapoPositions().get(index);
            if (!capoUsed.contains(capoPos)) {
                capoUsed.add(capoPos);
            }
        }
        return capoUsed;
    }

    public List<Song> filterSongs(Song filterSong) {
        Example<Song> example = Example.of(filterSong, getExampleMatcher(filterSong));
        logger.info("FILTERING SONGS with Example: {}", example);
        return songRepo.findAll(example, Sort.by(Sort.Direction.ASC, "title"));
    }

    public ExampleMatcher getExampleMatcher(Song filterSong) {
        if (filterSong.getKey() != null && filterSong.getKey().isBlank()) {
            filterSong.setKey(null);
        }
        if (filterSong.getTuning() != null && filterSong.getTuning().isBlank()) {
            filterSong.setTuning(null);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withMatcher("key", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("tuning", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("capo", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("state", ExampleMatcher.GenericPropertyMatchers.exact());

        List<String> ignorePaths = new ArrayList<>(List.of(
                "id", "title", "composer", "genre", "duration", "tempo",
                "threshold", "alert", "count", "creationDate", "modificationDate", "lastPerformedDate"
        ));
        if (filterSong.getCapo() < 0) {
            ignorePaths.add("capo");
        }
        if (filterSong.getState() < 0) {
            ignorePaths.add("state");
        }

        return matcher.withIgnorePaths(ignorePaths.toArray(new String[0]));
    }
}
