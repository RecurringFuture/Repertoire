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
    private int count = 0;
}
