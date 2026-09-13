package com.recurringfuture.controller;


import com.recurringfuture.ModelService;
import com.recurringfuture.SongService;
import com.recurringfuture.dto.FilterSongDTO;
import com.recurringfuture.entity.Song;
import com.recurringfuture.exceptions.ResourceNotFoundException;
import com.recurringfuture.repository.data.RepertoireData;
import com.recurringfuture.utils.FileUtils;
import com.recurringfuture.utils.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    private final SongService songService;
    private final ModelService modelService;

    @Autowired
    public SongController(SongService songService, ModelService modelService) {
        this.songService = songService;
        this.modelService = modelService;
    }

    @GetMapping("/songs")
    public String getAllSongs(@RequestParam(required = false) Integer id, Model model) {
        List<Song> songs;
        logger.info("SONGS: {}", model.containsAttribute("songs"));
        if (!model.containsAttribute("songs")) {
            songs = songService.getSongs();
            model.addAttribute("songs", songs);
            model.addAttribute("total", songs.size());
            logger.info("SONGS: " + songs.size());
        } else {
            songs = (List<Song>) model.getAttribute("songs");
        }
        modelService.populateFilterModel(model);

        if (id != null) {
            Song selectedSong = songService.getSong(id);
            modelService.populateSongDetailModel(selectedSong, songs, model);
        }
        return ViewNames.SONGS;
    }

    /**
     * Handles display of a specific song detail.
     * Implements basic resource handling for robustness.
     */
    @GetMapping("/song")
    public String getSong(@PathVariable("id") int id, Model model) {
        try {
            Song song = songService.getSong(id);
            model.addAttribute("song", song);
            // Use model service for populating common attributes
            // We pass an empty list here since this is a detail view, not a list view.
            modelService.populateSongDetailModel(song, List.of(), model);
            return ViewNames.SONGS;
        } catch (ResourceNotFoundException e) {
            // Improved error handling: Return a dedicated 404 view
            model.addAttribute("error", "Song not found.");
            return "error/404";
        }
    }

    @GetMapping("/importSongs")
    public String importSong() {
        // Minimal change, assuming no complex model population is needed
        return "importSongs";
    }

    @GetMapping("/addSong")
    public String addSong(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("tunings", songService.getTunings());
        model.addAttribute("keys", RepertoireData.getKeys());
        return "addSong";
    }

    @PostMapping("process")
    public String processSong(@RequestParam("file") MultipartFile file) throws IOException {
        // Improved logging and error handling could be added here
        songService.saveCsvFile(FileUtils.multipartToFile(file, file.getOriginalFilename()));
        return "redirect:/songs";
    }

    @PostMapping("saveSong")
    public String saveSong(@ModelAttribute("song") Song song) {
        if (songService.songExists(song.getTitle())) {
            return "redirect:/addSong?error=songExists";
        }
        songService.saveSong(song);
        return "redirect:/addSong";
    }

    @PostMapping("updateSong")
    public String updateSong(@ModelAttribute("selectedSong") Song selectedSong) {
        songService.updateSong(selectedSong);
        return "redirect:/songs?id=" + selectedSong.getId();
    }

    @PostMapping("deleteSong")
    public String deleteSong(@ModelAttribute("selectedSong") Song selectedSong) {
        songService.deleteSong(selectedSong.getId());
        return "redirect:/songs";
    }

    @PostMapping("filter")
    public String filterSongs(@ModelAttribute("filterSong") FilterSongDTO filterSong, Model model) {
        // 1. Business Logic
        Song songFilterCriteria = mapFilterSongToSong(filterSong);
        List<Song> songs = songService.filterSongs(songFilterCriteria);

        // 2. Model Population (Delegated to Service)
        modelService.populateFilterModel(model);

        // 3. Add Filter-specific data
        model.addAttribute("songs", songs);
        model.addAttribute("total", songs.size());
        model.addAttribute("filterSong", filterSong);

        return "songs";
    }

    /**
     * Helper method to map DTO to Song criteria object.
     * (Remains in controller as it's a mapping concern, but could be moved to a dedicated mapper class.)
     */
    private Song mapFilterSongToSong(FilterSongDTO filterSong) {
        Song song = new Song();
        song.setCapo(filterSong.getCapo());
        song.setKey(String.valueOf(RepertoireData.getKeys().indexOf(filterSong.getKey())));
        song.setState(filterSong.getState());
        if (filterSong.getTuning() != null) {
            song.setTuning(filterSong.getTuning().toString());
        }
        return song;
    }
}
