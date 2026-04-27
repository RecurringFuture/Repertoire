package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
import com.recurringfuture.entity.Song;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/practice")
public class PracticeController {

    private static final Logger logger = LoggerFactory.getLogger(PracticeSetController.class);

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
        return ViewNames.PRACTICE;
    }

    @GetMapping("/songs")
    public String getAllSongs(Model model) {
        List<Song> songs = practiceService.getSongs();
        logger.info("PRACTICE SONGS: " + songs.size());
        model.addAttribute("songs", songs);
        model.addAttribute("total", songs.size());
        return ViewNames.PRACTICE;
    }

//    @GetMapping("/practice")
//    public String getPracticeSets(Model model) {
//        logger.info("PRACTICE: GET PRACTICE SETS");
//        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
//        model.addAttribute("practiceSets", practiceSets);
//        return ViewNames.PRACTICE;
//    }
}
