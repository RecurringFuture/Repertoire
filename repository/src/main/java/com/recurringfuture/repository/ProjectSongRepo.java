package com.recurringfuture.repository;

import com.recurringfuture.entity.ProjectSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSongRepo extends JpaRepository<ProjectSong, Integer> {
    List<ProjectSong> findByProjectId(int projectId);

    void deleteByProjectIdAndSongId(int projectId, int songId);
}
