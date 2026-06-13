package com.recurringfuture.dto;

import com.recurringfuture.entity.Tuning;
import lombok.Data;

@Data
public class FilterSongDTO {

    private int key;
    private Tuning tuning;
    private int state;
    private int capo;
}
