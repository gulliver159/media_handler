package com.neuron.service;

import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.neuron.common.MockObjects.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VideoInfoServiceTest extends AbstractServiceTest {

    @Autowired
    VideoInfoService videoInfoService;

    @MockBean
    VideoDurationCalculationService videoDurationCalculationService;

    @Test
    public void processVideoInfo() {
        when(videoDurationCalculationService.calc(any())).thenReturn(MEDIA_DURATION);

        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, MEDIA_URL);
        videoInfoService.processVideoInfo(queryParams);

        verify(videoDurationCalculationService).calc(queryParams);
        verify(mediaInfoDao).saveMediaVideo(queryParams.getId(), MEDIA_DURATION);
    }
}