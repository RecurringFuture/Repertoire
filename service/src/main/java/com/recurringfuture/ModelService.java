package com.recurringfuture;


import com.recurringfuture.dto.FilterSongDTO;
import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.data.RepertoireData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

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
     * @return A ModelAndView containing all required attributes.
     */
    public ModelAndView populateSongDetailModel(Song song, List<Song> songs) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selectedSong", song);
        mav.addObject("genres", songService.findAll());
        mav.addObject("tunings", songService.getTunings());
        mav.addObject("capoPositions", RepertoireData.getCapoPositions());
        mav.addObject("keys", RepertoireData.getKeys());
        mav.addObject("thresholds", RepertoireData.getThresholds());
        mav.addObject("states", RepertoireData.getStates());
        mav.addObject("total", songs.size());
        return mav;
    }

    /**
     * Populates model attributes for the song filtering view.
     * @param model The model to be populated.
     */
    public void populateFilterModel(org.springframework.ui.Model model) {
        // Add "Any" options to the available filters
        List<String> filterKeys = songService.getKeysUsed();
        filterKeys.addFirst("Any");

        List<Tuning> filterTunings = songService.getTuningsUsed();
        filterTunings.addFirst(new Tuning()); // Assuming default constructor works for "Any"

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
}
