package com.recurringfuture.repository;

import com.recurringfuture.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepo extends JpaRepository<Song,Integer>, QueryByExampleExecutor<Song> {
}
