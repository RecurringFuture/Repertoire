package com.recurringfuture.controller;

import com.recurringfuture.SettingsService;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/settings")
public class SettingsController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/")
    public String showSettingsPage() {
        return ViewNames.SETTINGS;
    }

    @GetMapping("/manageTunings")
    public String manageTunings(Model model) {
        logger.info("GET TUNINGS TO MANAGE");
        model.addAttribute("tunings", settingsService.getTunings());
        return "redirect:/settings";
    }
}
