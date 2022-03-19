package com.neuron.service.impl;

import com.neuron.service.VideoDurationCalculationService;
import io.swagger.model.QueryParamsForSavingMedia;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VideoDurationCalculationServiceImpl implements VideoDurationCalculationService {

    private final Random rand = new Random();

    @SneakyThrows
    @Override
    public Integer calc(QueryParamsForSavingMedia queryParams) {
        int maxSecondWaiting = 10;
        int minSecondWaiting = 8;
        int timeSleep = (rand.nextInt(maxSecondWaiting - minSecondWaiting) + minSecondWaiting);
        Thread.sleep(timeSleep * 1000L);

        int maxResponse = 60;
        return rand.nextInt(maxResponse);
    }
}
