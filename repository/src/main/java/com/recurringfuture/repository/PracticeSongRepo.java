package com.recurringfuture.repository;

import com.recurringfuture.entity.PracticeSetSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeSongRepo extends JpaRepository<PracticeSetSong, Integer> {
    List<PracticeSetSong> findByPracticeSetId(int practiceSetId);

    void deleteByPracticeSetIdAndSongId(int practiceSetId, int songId);
}
