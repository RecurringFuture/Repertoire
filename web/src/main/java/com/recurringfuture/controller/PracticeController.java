package com.recurringfuture.controller;

import com.recurringfuture.PracticeService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class PracticeController {

    private static final Logger logger = LoggerFactory.getLogger(PracticeSetController.class);

    private final PracticeService practiceService;

    @Autowired
    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }


}
