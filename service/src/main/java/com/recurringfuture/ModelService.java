package com.recurringfuture;


import com.recurringfuture.dto.FilterSongDTO;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.data.RepertoireData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Service dedicated to preparing and populating model attributes
 * for various views, ensuring the Controller remains clean.
 */
@Service
public class ModelService {

    private final SongService songService;

    @Autowired
    public ModelService(SongService songService) {
        this.songService = songService;
    }


    /**
     * Populates model attributes for viewing a single song detail.
     * @param song The selected song object.
     * @param songs List of songs, potentially filtered.
     */
    public void populateSongDetailModel(Song song, List<Song> songs, Model model) {
        model.addAttribute("selectedSong", song);
        model.addAttribute("genres", songService.findAll());
        model.addAttribute("tunings", songService.getTunings());
        model.addAttribute("capoPositions", RepertoireData.getCapoPositions());
        model.addAttribute("keys", RepertoireData.getKeys());
        model.addAttribute("thresholds", RepertoireData.getThresholds());
        model.addAttribute("states", RepertoireData.getStates());
        model.addAttribute("total", songs.size());
    }

    public void populateAddSongModel(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("tunings", songService.getTunings());
        model.addAttribute("keys", RepertoireData.getKeys());
    }

    /**
     * Populates model attributes for the song filtering view.
     * @param model The model to be populated.
     */
    public void populateFilterModel(Model model) {
        // Add "Any" options to the available filters
        List<String> filterKeys = songService.getKeysUsed();
        filterKeys.addFirst("Any");

        List<Tuning> filterTunings = songService.getTuningsUsed();
        if (filterTunings != null && !filterTunings.isEmpty()) {
            filterTunings.addFirst(new Tuning());
        }

        List<String> filterStates = songService.getStatesUsed();
        filterStates.addFirst("Any");

        List<String> filterCapo = songService.getCapoUsed();
        filterCapo.addFirst("Any");

        // Add the default empty filter object
        model.addAttribute("filterSong", new FilterSongDTO());

        // Add all filter lists
        model.addAttribute("filterKeys", filterKeys);
        model.addAttribute("filterTunings", filterTunings);
        model.addAttribute("filterStates", filterStates);
        model.addAttribute("filterCapo", filterCapo);
    }

    public void addFilterData(List<Song> songs, FilterSongDTO filterSong, Model model) {
        model.addAttribute("songs", songs);
        model.addAttribute("total", songs.size());
        model.addAttribute("filterSong", filterSong);
    }
}
