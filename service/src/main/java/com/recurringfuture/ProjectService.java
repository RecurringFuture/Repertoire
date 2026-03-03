package com.recurringfuture;

import com.recurringfuture.entity.Project;
import com.recurringfuture.repository.ProjectRepo;
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

    @Autowired
    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
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

}
