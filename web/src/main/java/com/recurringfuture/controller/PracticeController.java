package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
import com.recurringfuture.PracticeSetService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/practice")
@SessionAttributes({"songs", "practiceSet", "total"})
public class PracticeController {

    private static final Logger logger = LoggerFactory.getLogger(PracticeController.class);

    private final PracticeService practiceService;
    private final PracticeSetService practiceSetService;

    @Autowired
    public PracticeController(PracticeService practiceService, PracticeSetService practiceSetService) {
        this.practiceService = practiceService;
        this.practiceSetService = practiceSetService;
    }

    @GetMapping("/songs")
    public String getAllSongs(Model model) {
        List<Song> songs = practiceService.getSongs();
        logger.info("PRACTICE /SONGS: " + songs.size());
        model.addAttribute("songs", songs);
        model.addAttribute("total", songs.size());
        return ViewNames.PRACTICE;
    }

    @PostMapping("/random")
    public String getRandomNumberOfSongs(@RequestParam(required = false) String numberOfRandomSongs, Model model) {
        logger.info("PRACTICE: GET RANDOM NUMER OF SONGS: " + numberOfRandomSongs);
        int randomSongs = Integer.parseInt(numberOfRandomSongs);
        if (randomSongs > 0 && randomSongs < practiceService.getTotalNumberOfSongs()) {
            model.addAttribute("songs", practiceService.getRandomSongs(randomSongs));
            model.addAttribute("total", numberOfRandomSongs);
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
            model.addAttribute("songs", Collections.EMPTY_LIST);
            model.addAttribute("practiceSets", practiceSets);
            model.addAttribute("practiceSet", new PracticeSet());
            return ViewNames.PRACTICE;
        }
    }

    @GetMapping("/practiceSet/{id}")
    public String getPracticeSet(@PathVariable Integer id, Model model) {
        logger.info("PRACTICE SET: " + id);
        List<Song> practiceSetSongs = practiceSetService.getSongsToPractice(id);

        model.addAttribute("songs", practiceSetSongs);
        model.addAttribute("total", practiceSetSongs.size());

        return ViewNames.PRACTICE;
    }

    @GetMapping("/song/{id}")
    public String getSong(@PathVariable Integer id, @ModelAttribute("songs") List<Song> songs, Model model) {
        logger.info("PRACTICE /SONG/ID: " + songs);
        if (id != null) {
            mapSelectedSong(id, model);
        }
        return ViewNames.PRACTICE;
    }

    private void mapSelectedSong(Integer id, Model model) {
        SelectedSongDTO selected = practiceService.selectedSongToDto(id);

        model.addAttribute("selectedSong", selected);
        model.addAttribute("genre", selected.getGenre());
        model.addAttribute("tuning", selected.getTuning());
        model.addAttribute("capoPosition", selected.getCapo());
        model.addAttribute("key", selected.getKey());
        model.addAttribute("state", selected.getState());
        logger.info("PRACTICE /SelectedSongDTO/ID: " + selected);
    }

    @PostMapping("updateSong")
    public String updateSong(@ModelAttribute("selectedSong") Song selectedSong) {
        logger.info("UPDATE SONG");
        practiceService.updateSong(selectedSong);
        return "redirect:/practice/songs?id=" + selectedSong.getId();
    }


}
