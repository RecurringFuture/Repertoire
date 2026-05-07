package com.recurringfuture.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SelectedSongDTO {

    private int id;
    private String title;
    private String composer;
    private String key;
    private String tuning;
    private int capo;
    private int tempo;
    private String genre;
    private String duration;
    private String state;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private LocalDate lastPerformedDate;
    private int count;

    public SelectedSongDTO(int id, String title, String composer, String key, String tuning, int capo, int tempo, String genre, String duration, String state, LocalDate creationDate, LocalDate modificationDate, LocalDate lastPerformedDate, int count) {
        this.id = id;
        this.title = title;
        this.composer = composer;
        this.key = key;
        this.tuning = tuning;
        this.capo = capo;
        this.tempo = tempo;
        this.genre = genre;
        this.duration = duration;
        this.state = state;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.lastPerformedDate = lastPerformedDate;
        this.count = count;
    }
}
