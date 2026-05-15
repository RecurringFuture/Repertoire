package com.recurringfuture.controller;

import com.recurringfuture.SettingsService;
import com.recurringfuture.entity.Genre;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Controller
@SessionAttributes({"editMode"})
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
        model.addAttribute("editMode", "Tunings");
        model.addAttribute("itemsToManage", settingsService.getTunings());
        model.addAttribute("add", "");
        return ViewNames.SETTINGS;
    }

    @GetMapping("/manageGenres")
    public String manageGenres(Model model) {
        logger.info("GET GENRES TO MANAGE");
        model.addAttribute("editMode", "Genres");
        model.addAttribute("itemsToManage", settingsService.getGenres());
        model.addAttribute("add", "");
        return ViewNames.SETTINGS;
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id, Model model) {
        logger.info("DELETE ITEM: " + model.getAttribute("editMode") + " ID: " + id);
        if (Objects.equals(model.getAttribute("editMode"), "Tunings")) {
            settingsService.deleteTuning(id);
            model.addAttribute("itemsToManage", settingsService.getTunings());
        } else if (Objects.equals(model.getAttribute("editMode"), "Genres")) {
            settingsService.deleteGenre(id);
            model.addAttribute("itemsToManage", settingsService.getGenres());
        }
        return ViewNames.SETTINGS;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("add") String add, Model model) {
        logger.info("ADD ITEM: " + model.getAttribute("add"));
        if (Objects.equals(model.getAttribute("editMode"), "Tunings")) {
            Tuning tuning = new Tuning();
            tuning.setTitle(add);
            settingsService.saveTuning(tuning);
            model.addAttribute("itemsToManage", settingsService.getTunings());
        } else if (Objects.equals(model.getAttribute("editMode"), "Genres")) {
            Genre genre = new Genre();
            genre.setTitle(add);
            settingsService.saveGenre(genre);
            model.addAttribute("itemsToManage", settingsService.getGenres());
        }
        model.addAttribute("add", "");
        return ViewNames.SETTINGS;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        logger.info("EDIT ITEM: " );
        if (Objects.equals(model.getAttribute("editMode"), "Tunings")) {
            Tuning tuning = settingsService.getTuning(id);
            model.addAttribute("itemToEdit", tuning);
            model.addAttribute("itemsToManage", settingsService.getTunings());
        } else if (Objects.equals(model.getAttribute("editMode"), "Genres")) {
            Genre genre = settingsService.getGenre(id);
            model.addAttribute("itemToEdit", genre);
            model.addAttribute("itemsToManage", settingsService.getGenres());
        }
        return ViewNames.SETTINGS;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("itemToEdit") Object item, Model model) {
        logger.info("UPDATE ITEM: " );
        if (Objects.equals(model.getAttribute("editMode"), "Tunings")) {
            settingsService.updateTuning((Tuning) item);
        } else if (Objects.equals(model.getAttribute("editMode"), "Genres")) {
            settingsService.updateGenre((Genre) item);
        }
        return ViewNames.SETTINGS;
    }

}
