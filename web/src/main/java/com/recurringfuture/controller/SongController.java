package com.recurringfuture.controller;

import com.recurringfuture.SongService;
import com.recurringfuture.entity.Song;
import com.recurringfuture.utils.FileUtils;
import com.recurringfuture.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("songs")
    public String getAllSongs(Model model) {
        List<Song> songs = songService.getSongs();
        Logger log = LoggerFactory.getLogger(SongController.class);
        log.info("Res: " + songs.toString());
        model.addAttribute("songs", songs);
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
    public String saveSong(@ModelAttribute("song") Song song) {
        songService.saveSong(song);
        return "redirect:" + ViewNames.SONGS;
    }

}
