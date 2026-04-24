package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
import com.recurringfuture.PracticeSetService;
import com.recurringfuture.entity.PracticeSet;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class PracticeController {

    private static final Logger logger = LoggerFactory.getLogger(PracticeSetController.class);

    private final PracticeService practiceService;

    @Autowired

    public PracticeController(PracticeService practiceSetService) {
        this.practiceService = practiceSetService;
    }

    @GetMapping("/random")
    public String getPracticeModel(Model model) {
        logger.info("PRACTICE: ");
//        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
        String numberOfRandomSongs = "";
        model.addAttribute("numberOfRandomSongs", numberOfRandomSongs);
        return ViewNames.PRACTICE;
    }

//    @GetMapping("/practice")
//    public String getPracticeSets(Model model) {
//        logger.info("PRACTICE: GET PRACTICE SETS");
//        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
//        model.addAttribute("practiceSets", practiceSets);
//        return ViewNames.PRACTICE;
    }
}
