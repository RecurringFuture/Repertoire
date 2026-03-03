package com.recurringfuture.controller;

import com.recurringfuture.ProjectService;
import com.recurringfuture.entity.Project;
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
    public String getProjects(Model model) {
        logger.info("PROJECTS: ");
        List<Project> projects = projectService.getProjects();
        model.addAttribute("projects", projects);
        return ViewNames.PROJECTS;
    }

    @PostMapping("saveProject")
    public String addProject(@ModelAttribute("project") Project project, Model model) {
        logger.info("SAVE PROJECTS: {}", project.getTitle());
        projectService.saveProject(project);
        return ViewNames.PROJECTS;
    }
}
