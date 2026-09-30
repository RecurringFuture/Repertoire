package com.recurringfuture;

import com.recurringfuture.entity.Song;
import com.recurringfuture.entity.Tuning;
import com.recurringfuture.repository.SongRepo;
import com.recurringfuture.repository.TuningRepo;
import com.recurringfuture.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class SongServiceTest {

    @Mock
    private SongRepo songRepo;

    @Mock
    private TuningRepo tuningRepo;

    @InjectMocks
    private SongService songService;

    private Song song1;
    private Song song2;

    @BeforeEach
    void setUp() {
        song1 = new Song();
        song1.setId(1);
        song1.setTitle("Autumn Leaves");
        song1.setKey("1");
        song1.setTuning("1");
        song1.setState(3);
        song1.setCapo(2);
        song1.setTempo(120);

        song2 = new Song();
        song2.setId(2);
        song2.setTitle("Blue Bossa");
        song2.setKey("4");
        song2.setTuning("2");
        song2.setState(1);
        song2.setCapo(1);
        song2.setTempo(140);
    }

    @Test
    void shouldFilterSongsByKeyCorrectly() {
        Song probe = new Song();
        probe.setKey("1");
        probe.setCapo(-1);
        probe.setState(-1);

        when(songRepo.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(song1));

        List<Song> results = songService.filterSongs(probe);

        assertThat(results).containsExactly(song1);
        ArgumentCaptor<Example<Song>> captor = ArgumentCaptor.forClass(Example.class);
        verify(songRepo).findAll(captor.capture(), eq(Sort.by(Sort.Direction.ASC, "title")));

        Example<Song> capturedExample = captor.getValue();
        assertThat(capturedExample.getProbe().getKey()).isEqualTo("1");
        assertThat(capturedExample.getMatcher().getIgnoredPaths()).contains("tempo", "capo", "state", "id", "title");
    }

    @Test
    void shouldFilterSongsByTuningCorrectly() {
        Song probe = new Song();
        probe.setTuning("2");
        probe.setCapo(-1);
        probe.setState(-1);

        when(songRepo.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(song2));

        List<Song> results = songService.filterSongs(probe);

        assertThat(results).containsExactly(song2);
        ArgumentCaptor<Example<Song>> captor = ArgumentCaptor.forClass(Example.class);
        verify(songRepo).findAll(captor.capture(), eq(Sort.by(Sort.Direction.ASC, "title")));

        Example<Song> capturedExample = captor.getValue();
        assertThat(capturedExample.getProbe().getTuning()).isEqualTo("2");
    }

    @Test
    void shouldFilterSongsByStateCorrectly() {
        Song probe = new Song();
        probe.setState(3);
        probe.setCapo(-1);

        when(songRepo.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(song1));

        List<Song> results = songService.filterSongs(probe);

        assertThat(results).containsExactly(song1);
        ArgumentCaptor<Example<Song>> captor = ArgumentCaptor.forClass(Example.class);
        verify(songRepo).findAll(captor.capture(), eq(Sort.by(Sort.Direction.ASC, "title")));

        Example<Song> capturedExample = captor.getValue();
        assertThat(capturedExample.getProbe().getState()).isEqualTo(3);
        assertThat(capturedExample.getMatcher().getIgnoredPaths()).doesNotContain("state");
        assertThat(capturedExample.getMatcher().getIgnoredPaths()).contains("capo");
    }

    @Test
    void shouldFilterSongsByCapoCorrectly() {
        Song probe = new Song();
        probe.setCapo(2);
        probe.setState(-1);

        when(songRepo.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(song1));

        List<Song> results = songService.filterSongs(probe);

        assertThat(results).containsExactly(song1);
        ArgumentCaptor<Example<Song>> captor = ArgumentCaptor.forClass(Example.class);
        verify(songRepo).findAll(captor.capture(), eq(Sort.by(Sort.Direction.ASC, "title")));

        Example<Song> capturedExample = captor.getValue();
        assertThat(capturedExample.getProbe().getCapo()).isEqualTo(2);
        assertThat(capturedExample.getMatcher().getIgnoredPaths()).doesNotContain("capo");
        assertThat(capturedExample.getMatcher().getIgnoredPaths()).contains("state");
    }

    @Test
    void shouldFilterSongsByCombinedCriteria() {
        Song probe = new Song();
        probe.setKey("1");
        probe.setTuning("1");
        probe.setState(3);
        probe.setCapo(2);

        when(songRepo.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(song1));

        List<Song> results = songService.filterSongs(probe);

        assertThat(results).containsExactly(song1);
        ArgumentCaptor<Example<Song>> captor = ArgumentCaptor.forClass(Example.class);
        verify(songRepo).findAll(captor.capture(), eq(Sort.by(Sort.Direction.ASC, "title")));

        Example<Song> capturedExample = captor.getValue();
        assertThat(capturedExample.getProbe().getKey()).isEqualTo("1");
        assertThat(capturedExample.getProbe().getTuning()).isEqualTo("1");
        assertThat(capturedExample.getProbe().getState()).isEqualTo(3);
        assertThat(capturedExample.getProbe().getCapo()).isEqualTo(2);
        assertThat(capturedExample.getMatcher().getIgnoredPaths()).doesNotContain("key", "tuning", "state", "capo");
    }

    @Test
    void shouldIgnoreUnspecifiedAndNonFilterFieldsWhenFiltering() {
        Song probe = new Song();
        probe.setCapo(-1);
        probe.setState(-1);
        probe.setKey("");
        probe.setTuning("");

        when(songRepo.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(song1, song2));

        List<Song> results = songService.filterSongs(probe);

        assertThat(results).containsExactly(song1, song2);

        ExampleMatcher matcher = songService.getExampleMatcher(probe);
        Set<String> ignored = matcher.getIgnoredPaths();

        assertThat(ignored).contains(
                "id", "title", "composer", "genre", "duration", "tempo",
                "threshold", "alert", "count", "creationDate", "modificationDate",
                "lastPerformedDate", "capo", "state"
        );
        assertThat(probe.getKey()).isNull();
        assertThat(probe.getTuning()).isNull();
    }

    @Test
    void shouldGetKeysUsedCorrectly() {
        when(songRepo.findAll()).thenReturn(List.of(song1, song2));

        List<String> keysUsed = songService.getKeysUsed();

        assertThat(keysUsed).containsExactly("C", "D");
    }

    @Test
    void shouldGetTuningsUsedCorrectly() {
        Tuning standard = new Tuning();
        standard.setId(1);
        standard.setTitle("Standard");

        Tuning dropD = new Tuning();
        dropD.setId(2);
        dropD.setTitle("Drop D");

        when(songRepo.findAll()).thenReturn(List.of(song1, song2));
        when(tuningRepo.findAllById(List.of(1, 2))).thenReturn(List.of(standard, dropD));

        List<Tuning> tuningsUsed = songService.getTuningsUsed();

        assertThat(tuningsUsed).containsExactly(standard, dropD);
    }

    @Test
    void shouldGetStatesUsedCorrectly() {
        when(songRepo.findAll()).thenReturn(List.of(song1, song2));

        List<String> statesUsed = songService.getStatesUsed();

        assertThat(statesUsed).containsExactly("Repertoire", "ToDo");
    }

    @Test
    void shouldGetCapoUsedCorrectly() {
        when(songRepo.findAll()).thenReturn(List.of(song1, song2));

        List<String> capoUsed = songService.getCapoUsed();

        assertThat(capoUsed).containsExactly("2", "1");
    }

    @Test
    void shouldReturnTrueWhenSongExistsCaseInsensitive() {
        when(songRepo.findByTitleIgnoreCase("autumn leaves")).thenReturn(song1);

        boolean exists = songService.songExists("autumn leaves");

        assertThat(exists).isTrue();
        verify(songRepo).findByTitleIgnoreCase("autumn leaves");
    }

    @Test
    void shouldReturnFalseWhenSongDoesNotExist() {
        when(songRepo.findByTitleIgnoreCase("Stairway to Heaven")).thenReturn(null);

        boolean exists = songService.songExists("Stairway to Heaven");

        assertThat(exists).isFalse();
        verify(songRepo).findByTitleIgnoreCase("Stairway to Heaven");
    }

    @Test
    void shouldReturnFalseWhenSongTitleIsNullOrBlank() {
        assertThat(songService.songExists(null)).isFalse();
        assertThat(songService.songExists("")).isFalse();
        assertThat(songService.songExists("   ")).isFalse();
        verify(songRepo, never()).findByTitleIgnoreCase(any());
    }

    @Test
    void shouldThrowExceptionWhenSavingSongWithNullTitle() {
        Song song = new Song();

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> songService.saveSong(song)
        );

        verify(songRepo, never()).save(any(Song.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingSongWithBlankTitle() {
        Song song = new Song();
        song.setTitle("   ");

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> songService.saveSong(song)
        );

        verify(songRepo, never()).save(any(Song.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullSong() {
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> songService.saveSong(null)
        );

        verify(songRepo, never()).save(any(Song.class));
    }

    @Test
    void shouldSaveSongSuccessfullyWhenTitleIsValid() {
        Song song = new Song();
        song.setTitle("Autumn Leaves");

        songService.saveSong(song);

        verify(songRepo).save(song);
        assertThat(song.getCreationDate()).isNotNull();
        assertThat(song.getModificationDate()).isNotNull();
    }
}
