package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
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

    private static final Logger logger = LoggerFactory.getLogger(PracticeController.class);

    private final PracticeService practiceService;

    @Autowired
    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @GetMapping("/practice")
    public String getPracticeSets(Model model) {
        logger.info("PRACTICE SETS: ");
        List<PracticeSet> practiceSets = practiceService.getPracticeSets();
        model.addAttribute("practiceSets", practiceSets);
        return ViewNames.PRACTICE;
    }
}
