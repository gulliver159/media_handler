package com.neuron.service.impl;

import com.neuron.dao.MediaInfoDao;
import com.neuron.service.VideoDurationCalculationService;
import com.neuron.service.VideoInfoService;
import io.swagger.model.QueryParamsForSavingMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoInfoServiceImpl implements VideoInfoService {

    private final VideoDurationCalculationService videoDurationCalculationService;
    private final MediaInfoDao mediaInfoDao;

    @Async
    public void processVideoInfo(QueryParamsForSavingMedia queryParams) {
        int duration = videoDurationCalculationService.calc(queryParams);
        mediaInfoDao.saveMediaVideo(queryParams.getId(), duration);
    }
}
