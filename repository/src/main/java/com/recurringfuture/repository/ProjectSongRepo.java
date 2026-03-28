package com.recurringfuture.repository;

import com.recurringfuture.entity.ProjectSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSongRepo extends JpaRepository<ProjectSong, Integer> {
    @Query(value = "SELECT * FROM project_song WHERE project_id = ?1", nativeQuery = true)
    List<ProjectSong> findByProjectId(int projectId);
}
