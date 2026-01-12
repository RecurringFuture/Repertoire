package com.recurringfuture;

import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.SongRepo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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

    public Page<Song> findPaginated(Pageable pageable) {
        List<Song> songs = getSongs();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Song> list;

        if (songs.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, songs.size());
            list = songs.subList(startItem, toIndex);
        }

        Page<Song> songPage
                = new PageImpl<Song>(list, PageRequest.of(currentPage, pageSize), songs.size());

        return songPage;
    }

    public void saveCsvFile(File file) throws IOException {
        Collection<Song> songs = new ArrayList<>();
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
        for (CSVRecord record : records) {
            Song song = new Song();
            song.setTitle(record.get(0));
            songs.add(song);
        }
        in.close();
        songRepo.saveAll(songs);
    }

    public void saveSong(Song song) {
        LocalDate localDate = LocalDate.now();
        song.setCreationDate(localDate);
        song.setModificationDate(localDate);
        songRepo.save(song);
    }

}
