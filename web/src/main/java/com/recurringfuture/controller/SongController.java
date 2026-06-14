package com.recurringfuture.controller;

import com.recurringfuture.SongService;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
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
@SessionAttributes({"songs", "total"})
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/songs")
    public String getAllSongs(@RequestParam(required = false) Integer id, Model model) {
        List<Song> songs;
        logger.info("SONGS: " + model.containsAttribute("songs"));
        if (!model.containsAttribute("songs")) {
            songs = songService.getSongs();
            model.addAttribute("songs", songs);
            model.addAttribute("total", songs.size());
            logger.info("SONGS: " + songs.size());
        } else {
            songs = (List<Song>) model.getAttribute("songs");
        }
        setSongFilterModelAttributes(model);

        if (id != null) {
            setSongModelAttributes(id, songs, model);
        }
        return ViewNames.SONGS;
    }

    @GetMapping("/song")
    public String getSong(int id, Model model) {
        Song song = songService.getSong(id);
        logger.info("Res: " + song.toString());
        model.addAttribute("song", song);
        return ViewNames.SONGS;
    }

    @GetMapping("/importSongs")
    public String importSong() {
        logger.info("IMPORT");
        return ViewNames.IMPORT_SONGS;
    }

    @GetMapping("/addSong")
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
        logger.info("SAVE SONG");
        songService.saveSong(song);
        return "redirect:" + ViewNames.ADD_SONG;
    }

    @PostMapping("updateSong")
    public String updateSong(@ModelAttribute("selectedSong") Song selectedSong) {
        logger.info("UPDATE SONG");
        songService.updateSong(selectedSong);
        return "redirect:/songs?id=" + selectedSong.getId();
    }

    @PostMapping("deleteSong")
    public String deleteSong(@ModelAttribute("selectedSong") Song selectedSong) {
        songService.deleteSong(selectedSong.getId());
        return "redirect:/" + ViewNames.SONGS;
    }

    @PostMapping("filter")
    public String filterSongs(@ModelAttribute("filterSong") Song filterSong, Model model) {
        logger.info("FILTER: {}", filterSong.toString());
        List<Song> songs = songService.filterSongs1(filterSong);
        logger.info("FILTER: {}", songs.size());
        model.addAttribute("songs", songs);
        model.addAttribute("total", songs.size());
        setSongFilterModelAttributes(model);
        return "redirect:/" + ViewNames.SONGS;
    }

    private void setSongModelAttributes(Integer id, List<Song> songs, Model model) {
        Song selected = songService.getSong(id);
        model.addAttribute("selectedSong", selected);
        model.addAttribute("genres", songService.findAll());
        model.addAttribute("tunings", songService.getTunings());
        model.addAttribute("capoPositions", RepertoireData.getCapoPositions());
        model.addAttribute("keys", RepertoireData.getKeys());
        model.addAttribute("thresholds", RepertoireData.getThresholds());
        model.addAttribute("states", RepertoireData.getStates());
        model.addAttribute("total", songs.size());
    }

    private void setSongFilterModelAttributes(Model model) {
        List <String> filterKeys = songService.getKeysUsed();
        filterKeys.addFirst("");
        List<Tuning> filterTunings = songService.getTuningsUsed();
        filterTunings.addFirst(new Tuning());
        List<String> filterStates = songService.getStatesUsed();
        filterStates.addFirst("");
        List<String> filterCapo = songService.getCapoUsed();
        filterCapo.addFirst("");
        model.addAttribute("filterKeys", filterKeys);
        model.addAttribute("filterTunings", filterTunings);
        model.addAttribute("filterStates", filterStates);
        model.addAttribute("filterCapo", filterCapo);
        model.addAttribute("filterSong", new Song());
    }


}
