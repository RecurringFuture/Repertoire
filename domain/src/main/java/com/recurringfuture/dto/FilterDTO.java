package com.recurringfuture.dto;

import com.recurringfuture.entity.Tuning;
import lombok.Data;

@Data
public class FilterDTO {

    private int filterKey;
    private Tuning filterTuning;
    private int filterState;
    private int filterCapo;
}
