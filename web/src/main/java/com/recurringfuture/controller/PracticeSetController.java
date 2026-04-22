package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
import com.recurringfuture.entity.PracticeSet;
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
public class PracticeSetController {

    private static final Logger logger = LoggerFactory.getLogger(PracticeSetController.class);

    private final PracticeService practiceService;

    @Autowired
    public PracticeSetController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @GetMapping("/practice")
    public String getPracticeSets(Model model) {
        logger.info("PRACTICE: ");
        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
        model.addAttribute("practiceSets", practiceSets);
        return ViewNames.PRACTICE;
    }

    @GetMapping("/practiceSets")
    public String getPracticeSets(@RequestParam(required = false) Integer id, Model model) {
        logger.info("PRACTICE SETS: ");
        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
        model.addAttribute("practiceSets", practiceSets);

        if (id != null) {
            logger.info("PRACTICE: ID PRESENT");
            PracticeSet practiceSet = practiceService.getPracticeSet(id);
            List<Song> practiceSetSongs = practiceService.getSongsToPractice(id);
            model.addAttribute("practiceSetSongs", practiceSetSongs);
            model.addAttribute("selectedPracticeSetId", id);
            model.addAttribute("selectedPracticeSetTitle", practiceSet.getTitle());
            List<Song> availableSongs = practiceService.getAvailableSongsToPractice(id);
            model.addAttribute("availableSongs", availableSongs);
        } else if (!practiceSets.isEmpty()) {
            logger.info("PRACTICE: NO ID");
            int firstPracticeSettId = practiceSets.getFirst().getId();
            String practiceSetTitle = practiceSets.getFirst().getTitle();
            List<Song> practiceSetSongs = practiceService.getSongsToPractice(firstPracticeSettId);
            model.addAttribute("practiceSetSongs", practiceSetSongs);
            model.addAttribute("selectedPracticeSetId", firstPracticeSettId);
            model.addAttribute("selectedPracticeSetTitle", practiceSetTitle);
            List<Song> availableSongs = practiceService.getAvailableSongsToPractice(firstPracticeSettId);
            model.addAttribute("availableSongs", availableSongs);
        } else {
            logger.info("PRACTICE: EMPTY");
            model.addAttribute("songs", new ArrayList<Song>());
        }

        model.addAttribute("practiceSet", new PracticeSet());
        return ViewNames.PRACTICE_SETS;
    }

    @PostMapping("/savePracticeSet")
    public String addPracticeSet(@ModelAttribute("practiceSet") PracticeSet practiceSet, Model model) {
        logger.info("SAVE PRACTICESET: {}", practiceSet.getTitle());
        practiceService.savePracticeSet(practiceSet);
        return "redirect:/practiceSets";
    }

    @PostMapping("/practiceSet/addSong")
    public String addSongToPracticeSet(@RequestParam int practiceSetId, @RequestParam int songId) {
        practiceService.addSongToPracticeSet(practiceSetId, songId);
        return "redirect:/practiceSets?id=" + practiceSetId;
    }

    @PostMapping("/practiceSet/removeSong")
    public String removeSongFromProject(@RequestParam int projectId, @RequestParam int songId) {
        practiceService.removeSongFromPracticeSet(projectId, songId);
        return "redirect:/practiceSets?id=" + projectId;
    }
}
