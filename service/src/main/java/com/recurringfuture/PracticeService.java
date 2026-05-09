package com.recurringfuture;

import com.recurringfuture.dto.SelectedSongDTO;
import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.PracticeSet;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.GenreRepo;
import com.recurringfuture.repository.PracticeSetRepo;
import com.recurringfuture.repository.SongRepo;
import com.recurringfuture.repository.TuningRepo;
import com.recurringfuture.repository.data.RepertoireData;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PracticeService {

    private final SongRepo songRepo;
    private final PracticeSetRepo practiceSetRepo;
    private final GenreRepo genreRepo;
    private final TuningRepo tuningRepo;
    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(PracticeService.class);

    @Autowired
    public PracticeService(SongRepo songRepo, PracticeSetRepo practiceSetRepo, GenreRepo genreRepo, TuningRepo tuningRepo, ModelMapper modelMapper) {
        this.songRepo = songRepo;
        this.practiceSetRepo = practiceSetRepo;
        this.genreRepo = genreRepo;
        this.tuningRepo = tuningRepo;
        this.modelMapper = modelMapper;
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

    public List<Genre> getGenres() {
        return genreRepo.findAll();
    }

    public List<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public SelectedSongDTO selectedSongToDto(int id) {
        Song song = songRepo.getReferenceById(id);
        SelectedSongDTO selectedSongDTO = modelMapper.map(song, SelectedSongDTO.class);
        selectedSongDTO.setCreationDate(song.getCreationDate());
        selectedSongDTO.setModificationDate(song.getModificationDate());
        selectedSongDTO.setLastPerformedDate(song.getLastPerformedDate());
        String songTuning = (song.getTuning() != null) ? song.getTuning() : "1";
        Tuning t = tuningRepo.getReferenceById(Integer.parseInt(songTuning));
        selectedSongDTO.setTuning(t.getTuning());
        String songGenre = (song.getGenre() != null) ? song.getGenre() : "1";
        Genre g = genreRepo.getReferenceById(Integer.parseInt(songGenre));
        selectedSongDTO.setGenre(g.getTitle());
        String songKey = song.getKey() != null ? song.getKey() : "1";
        String key = RepertoireData.getKeys().get(Integer.parseInt(songKey));
        selectedSongDTO.setKey(key);

        logger.info("PRACTICE / CONVERT: " + selectedSongDTO);
        return selectedSongDTO;
    }

    public void updateSong(Song song) {
        Song dbSong = songRepo.getReferenceById(song.getId());
        LocalDate localDate = LocalDate.now();
        dbSong.setModificationDate(localDate);
        dbSong.setLastPerformedDate(localDate);
        int lastCount = songRepo.getReferenceById(song.getId()).getCount();
        dbSong.setCount(dbSong.getCount() + 1);
        songRepo.save(dbSong);
    }
}
