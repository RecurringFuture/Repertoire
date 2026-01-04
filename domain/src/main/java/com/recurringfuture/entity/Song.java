package com.recurringfuture.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Entity(name = "song")
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String title;
    private String composer;
    private String key;
    private String tuning;
    @Column(columnDefinition = "integer default 0")
    private int capo;
    @Column(columnDefinition = "integer default 0")
    private int tempo = 0;
    private String genre;
    private String duration;
    @Column(columnDefinition = "integer default 0")
    private int state = 0;
    @Column(columnDefinition = "integer default 0")
    private int threshold = 0;
    private Boolean alert;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private LocalDate lastPerformedDate;
    @Column(columnDefinition = "integer default 0")
    private int count = 0;

    public Song() {

    }
}
