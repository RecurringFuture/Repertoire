package com.recurringfuture.controller;

import com.recurringfuture.GenreService;
import com.recurringfuture.SongService;
import com.recurringfuture.TuningService;
import com.recurringfuture.entity.Song;
import com.recurringfuture.repository.data.RepertoireData;
import com.recurringfuture.utils.FileUtils;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    private final SongService songService;
    private final GenreService genreService;
    private final TuningService tuningService;

    @Autowired
    public SongController(SongService songService, GenreService genreService, TuningService tuningService) {
        this.songService = songService;
        this.genreService = genreService;
        this.tuningService = tuningService;
    }

    @GetMapping("songs")
    public String getAllSongs(@RequestParam(required = false) Integer id, Model model) {
        List<Song> songs = songService.getSongs();
        logger.info("SONGS: " + songs.size());
        model.addAttribute("songs", songs);

        if (id != null) {
            Song selected = songService.getSong(id); // handle not found how you prefer
            model.addAttribute("selectedSong", selected);
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("tunings", tuningService.getTunings());
            model.addAttribute("capoPositions", RepertoireData.getCapoPositions());
            model.addAttribute("keys", RepertoireData.getKeys());
            model.addAttribute("thresholds", RepertoireData.getThresholds());
            model.addAttribute("states", RepertoireData.getStates());
            model.addAttribute("total", songs.size());
        }
        return ViewNames.SONGS;
    }

    @GetMapping("song")
    public String getSong(int id, Model model) {
        Song song = songService.getSong(id);
        Logger log = LoggerFactory.getLogger(SongController.class);
        log.info("Res: " + song.toString());
        model.addAttribute("song", song);
        return ViewNames.SONGS;
    }

    @GetMapping("importSongs")
    public String importSong() {
        logger.info("IMPORT");
        return ViewNames.IMPORT_SONGS;
    }

    @GetMapping("addSong")
    public String addSong(Model model) {
        model.addAttribute("song", new Song());
        logger.info("ADD SONG");
        return ViewNames.ADD_SONG;
    }

    @PostMapping("process")
    public String processSong(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("PROCESS: " + file.getOriginalFilename());
        songService.saveCsvFile(FileUtils.multipartToFile(file, file.getOriginalFilename()));
        return "redirect:" + ViewNames.SONGS;
    }

    @PostMapping("saveSong")
    public String saveSong(@ModelAttribute("song") Song song, Model model) {
        logger.info("SAVE SONG");
        songService.saveSong(song);
        return "redirect:" + ViewNames.ADD_SONG;
    }

    @PostMapping("updateSong")
    public String updateSong(@ModelAttribute("selectedSong") Song selectedSong, Model model) {
        logger.info("UPDATE SONG");
        songService.updateSong(selectedSong);
        return "redirect:/" + ViewNames.SONGS;
    }

    @PostMapping("deleteSong")
    public String deleteSong(@ModelAttribute("selectedSong") Song selectedSong, Model model) {
        songService.deleteSong(selectedSong.getId());
        return "redirect:/" + ViewNames.SONGS;
    }


}
