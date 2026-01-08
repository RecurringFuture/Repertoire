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

//    @GetMapping("songs")
//    public String getAllSongs(Model model,
//                              @RequestParam("page") Optional<Integer> page,
//                              @RequestParam("size") Optional<Integer> size) {
//        int currentPage = page.orElse(1);
//        int pageSize = size.orElse(5);
//
//        Page<Song> songPage = songService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//
//        model.addAttribute("songPage", songPage);
//
////        List<Song> songs = songService.getSongs();
////        Logger log = LoggerFactory.getLogger(SongController.class);
////        log.info("Res: " + songs.toString());
////
////        model.addAttribute("songs", songs);
//        return ViewNames.SONGS;
//    }

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

    @PostMapping("process")
    public String processSong(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("PROCESS: " + file.getOriginalFilename());
        songService.saveCsvFile(FileUtils.multipartToFile(file, "songs.csv"));
        return "redirect:" + ViewNames.SONGS;
    }

//    @GetMapping("song/{id}")
//    public Song getSong(@RequestParam int id) {
//        Song res = songService.getSong(id);
//        Logger log = LoggerFactory.getLogger(SongController.class);
//        log.info("Res1: " + res.toString());
//        return res;
//    }

}
