package com.recurringfuture.dto;

import lombok.Data;

@Data
public class FilterSongDTO {

    private String key;
    private Integer tuning;
    private Integer state;
    private Integer capo;
}
