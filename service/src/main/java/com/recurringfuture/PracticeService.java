package com.recurringfuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PracticeService {

    SongService songService;

    @Autowired
    public PracticeService(SongService songService) {
        this.songService = songService;
    }
}
