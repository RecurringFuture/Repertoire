package com.recurringfuture;

import com.recurringfuture.entity.Project;
import com.recurringfuture.entity.ProjectSong;
import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.ProjectRepo;
import com.recurringfuture.repository.ProjectSongRepo;
import com.recurringfuture.repository.SongRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepo projectRepo;
    private final ProjectSongRepo projectSongRepo;
    private final SongRepo songRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo, ProjectSongRepo projectSongRepo, SongRepo songRepo) {
        this.projectRepo = projectRepo;
        this.projectSongRepo = projectSongRepo;
        this.songRepo = songRepo;
    }

    public List<Project> getProjects() {
        return projectRepo.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Project getProject(int id) {
        return projectRepo.getReferenceById(id);
    }

    public void saveProject(Project project) {
        logger.info("Saving project: {}", project.getTitle());
        projectRepo.save(project);
    }

    public void deleteProject(int id) {
        projectRepo.deleteById(id);
    }

    public void updateProject(Project project) {
        logger.info("Updating project: {}", project.getTitle());
        projectRepo.save(project);
    }

    public List<Song> getSongsForProject(int projectId) {
        List<ProjectSong> projectSongs = projectSongRepo.findByProjectId(projectId);
        List<Integer> songIds = projectSongs.stream()
                .map(ProjectSong::getSongId)
                .toList();
        return songRepo.findAllById(songIds);
    }

}
