package com.recurringfuture;

import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

}
