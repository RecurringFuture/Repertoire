package com.recurringfuture.controller;

import com.recurringfuture.SongService;
import com.recurringfuture.entity.Song;
import com.recurringfuture.utils.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SongController {

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

//    @GetMapping("songs")
//    public List<Song> getAllSongs() {
//        List<Song> res = songService.getSongs();
//        Logger log = LoggerFactory.getLogger(SongController.class);
//        log.info("Res: " + res.toString());
//        return res;
//    }

    @GetMapping("song/{id}")
    public Song getSong(@RequestParam int id) {
        Song res = songService.getSong(id);
        Logger log = LoggerFactory.getLogger(SongController.class);
        log.info("Res1: " + res.toString());
        return res;
    }

}
