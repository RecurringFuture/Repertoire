package com.recurringfuture.controller;


import com.recurringfuture.ModelService;
import com.recurringfuture.SongService;
import com.recurringfuture.dto.FilterSongDTO;
import com.recurringfuture.entity.Song;
import com.recurringfuture.utils.FileUtils;
import com.recurringfuture.utils.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/importSongs")
    public String importSong() {
        // Minimal change, assuming no complex model population is needed
        return "importSongs";
    }

    @GetMapping("/addSong")
    public String addSong(Model model) {
        modelService.populateAddSongModel(model);
        return ViewNames.ADD_SONG;
    }

    @PostMapping("process")
    public String processSong(@RequestParam("file") MultipartFile file) throws IOException {
        // Improved logging and error handling could be added here
        songService.saveCsvFile(FileUtils.multipartToFile(file, file.getOriginalFilename()));
        return "redirect:/songs";
    }

    @PostMapping("saveSong")
    public String saveSong(@ModelAttribute("song") Song song, RedirectAttributes redirectAttributes) {
        if (songService.songExists(song.getTitle())) {
            String errorMsg = (song.getTitle() != null && !song.getTitle().isBlank())
                    ? "A song with the title '" + song.getTitle() + "' already exists. Please choose a different title."
                    : "A song with this title already exists.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
            return "redirect:/" + ViewNames.ADD_SONG + "?error=songExists";
        }
        songService.saveSong(song);
        return "redirect:/" + ViewNames.ADD_SONG;
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
        modelService.addFilterData(songs, filterSong, model);

        return ViewNames.SONGS;
    }

    /**
     * Helper method to map DTO to Song criteria object.
     */
    Song mapFilterSongToSong(FilterSongDTO filterSong) {
        Song song = new Song();
        if (filterSong.getCapo() != null && filterSong.getCapo() >= 0) {
            song.setCapo(filterSong.getCapo());
        } else {
            song.setCapo(-1);
        }
        if (filterSong.getKey() != null && !filterSong.getKey().isBlank()) {
            song.setKey(filterSong.getKey());
        } else {
            song.setKey(null);
        }
        if (filterSong.getState() != null && filterSong.getState() >= 0) {
            song.setState(filterSong.getState());
        } else {
            song.setState(-1);
        }
        if (filterSong.getTuning() != null) {
            song.setTuning(String.valueOf(filterSong.getTuning()));
        } else {
            song.setTuning(null);
        }
        return song;
    }
}
