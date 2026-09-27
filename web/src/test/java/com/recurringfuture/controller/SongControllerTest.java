package com.recurringfuture.controller;

import com.recurringfuture.ModelService;
import com.recurringfuture.SongService;
import com.recurringfuture.dto.FilterSongDTO;
import com.recurringfuture.entity.Song;
import com.recurringfuture.utils.ViewNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class SongControllerTest {

    @Mock
    private SongService songService;

    @Mock
    private ModelService modelService;

    @Mock
    private Model model;

    @InjectMocks
    private SongController songController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
    }

    @Test
    void shouldMapFilterSongToSongCorrectly() {
        FilterSongDTO dto = new FilterSongDTO();
        dto.setKey("1");
        dto.setTuning(2);
        dto.setState(3);
        dto.setCapo(0);

        Song probe = songController.mapFilterSongToSong(dto);

        assertThat(probe.getKey()).isEqualTo("1");
        assertThat(probe.getTuning()).isEqualTo("2");
        assertThat(probe.getState()).isEqualTo(3);
        assertThat(probe.getCapo()).isEqualTo(0);
    }

    @Test
    void shouldMapEmptyFilterSongToSentinelsAndNulls() {
        FilterSongDTO dto = new FilterSongDTO();
        dto.setKey("");
        dto.setTuning(null);
        dto.setState(null);
        dto.setCapo(null);

        Song probe = songController.mapFilterSongToSong(dto);

        assertThat(probe.getKey()).isNull();
        assertThat(probe.getTuning()).isNull();
        assertThat(probe.getState()).isEqualTo(-1);
        assertThat(probe.getCapo()).isEqualTo(-1);
    }

    @Test
    void shouldMapFilterDtoAndDelegateToService() {
        FilterSongDTO filterSong = new FilterSongDTO();
        filterSong.setKey("4");
        filterSong.setTuning(1);
        filterSong.setState(2);
        filterSong.setCapo(1);

        Song returnedSong = new Song();
        returnedSong.setId(10);
        returnedSong.setTitle("Song 10");

        when(songService.filterSongs(any(Song.class))).thenReturn(List.of(returnedSong));

        String viewName = songController.filterSongs(filterSong, model);

        assertThat(viewName).isEqualTo(ViewNames.SONGS);

        ArgumentCaptor<Song> probeCaptor = ArgumentCaptor.forClass(Song.class);
        verify(songService).filterSongs(probeCaptor.capture());
        Song capturedProbe = probeCaptor.getValue();
        assertThat(capturedProbe.getKey()).isEqualTo("4");
        assertThat(capturedProbe.getTuning()).isEqualTo("1");
        assertThat(capturedProbe.getState()).isEqualTo(2);
        assertThat(capturedProbe.getCapo()).isEqualTo(1);

        verify(modelService).populateFilterModel(model);
        verify(modelService).addFilterData(List.of(returnedSong), filterSong, model);
    }

    @Test
    void shouldRenderSongsViewWithFilterData() throws Exception {
        Song song = new Song();
        song.setId(1);
        song.setTitle("Autumn Leaves");

        when(songService.filterSongs(any(Song.class))).thenReturn(List.of(song));

        mockMvc.perform(post("/filter")
                        .param("key", "1")
                        .param("tuning", "1")
                        .param("state", "3")
                        .param("capo", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.SONGS))
                .andExpect(model().attributeExists("filterSong"));
    }

    @Test
    void shouldRedirectWithErrorMessageWhenSongExists() throws Exception {
        // Given
        String duplicateTitle = "Autumn Leaves";
        when(songService.songExists(duplicateTitle)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/saveSong")
                        .param("title", duplicateTitle))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + ViewNames.ADD_SONG + "?error=songExists"))
                .andExpect(flash().attribute("errorMessage",
                        "A song with the title 'Autumn Leaves' already exists. Please choose a different title."));

        verify(songService, never()).saveSong(any(Song.class));
    }

    @Test
    void shouldRedirectWithErrorMessageWhenTitleIsEmpty() throws Exception {
        mockMvc.perform(post("/saveSong")
                        .param("title", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + ViewNames.ADD_SONG + "?error=emptyTitle"))
                .andExpect(flash().attribute("errorMessage", "Song title is mandatory and cannot be empty."));

        verify(songService, never()).saveSong(any(Song.class));
    }

    @Test
    void shouldRedirectWithErrorMessageWhenTitleIsWhitespaceOnly() throws Exception {
        mockMvc.perform(post("/saveSong")
                        .param("title", "   "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + ViewNames.ADD_SONG + "?error=emptyTitle"))
                .andExpect(flash().attribute("errorMessage", "Song title is mandatory and cannot be empty."));

        verify(songService, never()).saveSong(any(Song.class));
    }

    @Test
    void shouldSaveSongAndRedirectWhenSongDoesNotExist() throws Exception {
        // Given
        String newTitle = "Take Five";
        when(songService.songExists(newTitle)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/saveSong")
                        .param("title", newTitle))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + ViewNames.ADD_SONG));

        ArgumentCaptor<Song> songCaptor = ArgumentCaptor.forClass(Song.class);
        verify(songService).saveSong(songCaptor.capture());
        assertThat(songCaptor.getValue().getTitle()).isEqualTo(newTitle);
    }
}
