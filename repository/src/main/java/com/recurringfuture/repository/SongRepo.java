package com.recurringfuture.repository;

import com.recurringfuture.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepo extends JpaRepository<Song,Integer>, QueryByExampleExecutor<Song> {

    @Query("SELECT u FROM song u WHERE u.capo = ?1")
    List<Song> findSongsByFilter(Integer capo);

//    u.key = ?1 and u.tuning = ?2 and and u.state = ?4
}

