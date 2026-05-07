package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
import com.recurringfuture.dto.SelectedSongDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/practice")
public class PracticeController {

    private static final Logger logger = LoggerFactory.getLogger(PracticeController.class);

    private final PracticeService practiceService;

    @Autowired
    public PracticeController(PracticeService practiceSetService) {
        this.practiceService = practiceSetService;
    }

    @GetMapping("/")
    public String getPracticeModel(Model model) {
        logger.info("PRACTICE: ");
        model.addAttribute("numberOfRandomSongs", "");
        model.addAttribute("songs", Collections.emptyList());
        model.addAttribute("total", 0);
        logger.info("PRACTICE /: " + model.getAttribute("songs"));
        return ViewNames.PRACTICE;
    }

    @GetMapping("/songs")
    public String getAllSongs(@RequestParam(required = false) Integer id,Model model) {
        List<Song> songs = practiceService.getSongs();
        logger.info("PRACTICE /SONGS: " + songs.size());
        model.addAttribute("songs", songs);
        model.addAttribute("total", songs.size());

        if (id != null) {
            SelectedSongDTO selected = practiceService.selectedSongToDto(id);

            model.addAttribute("selectedSong", selected);
            model.addAttribute("genre", selected.getGenre());
            model.addAttribute("tuning", selected.getTuning());
            model.addAttribute("capoPosition", selected.getCapo());
            model.addAttribute("key", selected.getKey());
            model.addAttribute("state", selected.getState());
            logger.info("PRACTICE /SONGS/ID: " + selected.toString());
        }
        return ViewNames.PRACTICE;
    }

    @PostMapping("/random")
    public String getRandomNumberOfSongs(@RequestParam(required = false) String numberOfRandomSongs, Model model) {
        logger.info("PRACTICE: GET RANDOM NUMER OF SONGS: " + numberOfRandomSongs);
        int randomSongs = Integer.parseInt(numberOfRandomSongs);
        if (randomSongs > 0 && randomSongs < practiceService.getTotalNumberOfSongs()) {
            model.addAttribute("songs", practiceService.getRandomSongs(randomSongs));
        }

        return ViewNames.PRACTICE;
    }

    @GetMapping("/practiceSets")
    public String getPracticeSets(Model model) {
        logger.info("PRACTICE SETS: ");
        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
        if (practiceSets.isEmpty()) {
            return ViewNames.PRACTICE_SETS;
        } else {
            model.addAttribute("practiceSets", practiceSets);
            model.addAttribute("practiceSet", new PracticeSet());
            return ViewNames.PRACTICE;
        }
    }


}
