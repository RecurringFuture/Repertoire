package com.recurringfuture.controller;

import com.recurringfuture.ProjectService;
import com.recurringfuture.SongService;
import com.recurringfuture.entity.Project;
import com.recurringfuture.entity.Song;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String getProjects(@RequestParam(required = false) Integer id, Model model) {
        logger.info("PROJECTS: ");
        List<Project> projects = projectService.getProjects();
        model.addAttribute("projects", projects);

        if (id != null) {
            logger.info("PROJECTS: ID PRESENT");
            Project project = projectService.getProject(id);
            List<Song> projectSongs = projectService.getSongsForProject(id);
            model.addAttribute("projectSongs", projectSongs);
            model.addAttribute("selectedProjectId", id);
            model.addAttribute("selectedProjectTitle", project.getTitle());
            List<Song> availableSongs = projectService.getAvailableSongsForProject(id);
            model.addAttribute("availableSongs", availableSongs);
        } else if (!projects.isEmpty()) {
            logger.info("PROJECTS: NO ID");
            int firstProjectId = projects.getFirst().getId();
            String projectTitle = projects.getFirst().getTitle();
            List<Song> projectSongs = projectService.getSongsForProject(firstProjectId);
            model.addAttribute("projectSongs", projectSongs);
            model.addAttribute("selectedProjectId", firstProjectId);
            model.addAttribute("selectedProjectTitle", projectTitle);
            List<Song> availableSongs = projectService.getAvailableSongsForProject(firstProjectId);
            model.addAttribute("availableSongs", availableSongs);
        } else {
            logger.info("PROJECTS: EMPTY");
            model.addAttribute("songs", new ArrayList<Song>());
        }

        model.addAttribute("project", new Project());
        return ViewNames.PROJECTS;
    }

    @PostMapping("saveProject")
    public String addProject(@ModelAttribute("project") Project project, Model model) {
        logger.info("SAVE PROJECTS: {}", project.getTitle());
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @PostMapping("/projects/addSong")
    public String addSongToProject(@RequestParam int projectId, @RequestParam int songId) {
        projectService.addSongToProject(projectId, songId);
        return "redirect:/projects?id=" + projectId;
    }

    @PostMapping("/projects/removeSong")
    public String removeSongFromProject(@RequestParam int projectId, @RequestParam int songId) {
        projectService.removeSongFromProject(projectId, songId);
        return "redirect:/projects?id=" + projectId;
    }
}
