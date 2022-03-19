package com.neuron.service;

import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import io.swagger.model.VideoTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MediaInfoServiceITest extends AbstractServiceTest {

    private static final Integer INSERTS_NUMBER_ALREADY_EXISTED = 0;
    private static final Integer INSERTS_NUMBER_NOT_EXISTED = 1;

    @Autowired
    MediaInfoService mediaInfoService;

    @MockBean
    VideoInfoService videoInfoService;

    @Test
    public void saveMediaInfoNotExistedVideo() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MediaTypeEnum.VIDEO, MEDIA_URL);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService).processVideoInfo(queryParams);
    }

    @Test
    public void saveMediaInfoAlreadyExistedVideo() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_ALREADY_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MediaTypeEnum.VIDEO, MEDIA_URL);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void saveMediaInfoNotExistedNotVideo() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MediaTypeEnum.IMAGE, MEDIA_URL);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void saveMediaInfoAlreadyExistedEmptyUrl() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MediaTypeEnum.VIDEO, null);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void getMediaInfoImage() {
        mockGetMediaInfo(MediaTypeEnum.IMAGE, MEDIA_URL, MEDIA_DURATION);

        MediaInfo mediaInfo = mediaInfoService.getMediaInfo(MEDIA_ID);
        assertThat(mediaInfo.getId()).isEqualTo(MEDIA_ID);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.IMAGE);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
        assertThat(mediaInfo.getDuration()).isEqualTo(MEDIA_DURATION);
        assertThat(mediaInfo.getTypeVideo()).isNull();

        verify(mediaInfoDao).getMedia(MEDIA_ID);
    }

    @Test
    public void getMediaInfoWithDuration() {
        mockGetMediaInfo(MediaTypeEnum.VIDEO, MEDIA_URL, MEDIA_DURATION);

        MediaInfo mediaInfo = mediaInfoService.getMediaInfo(MEDIA_ID);
        assertThat(mediaInfo.getId()).isEqualTo(MEDIA_ID);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
        assertThat(mediaInfo.getDuration()).isEqualTo(MEDIA_DURATION);
        assertThat(mediaInfo.getTypeVideo()).isEqualTo(VideoTypeEnum.WITH_DURATION);

        verify(mediaInfoDao).getMedia(MEDIA_ID);
    }

    @Test
    public void getMediaInfoWithoutDuration() {
        mockGetMediaInfo(MediaTypeEnum.VIDEO, MEDIA_URL, null);

        MediaInfo mediaInfo = mediaInfoService.getMediaInfo(MEDIA_ID);
        assertThat(mediaInfo.getId()).isEqualTo(MEDIA_ID);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
        assertThat(mediaInfo.getDuration()).isNull();
        assertThat(mediaInfo.getTypeVideo()).isEqualTo(VideoTypeEnum.WITHOUT_DURATION_YET);

        verify(mediaInfoDao).getMedia(MEDIA_ID);
    }

    @Test
    public void getMediaInfoWithoutUrl() {
        mockGetMediaInfo(MediaTypeEnum.VIDEO, null, null);

        MediaInfo mediaInfo = mediaInfoService.getMediaInfo(MEDIA_ID);
        assertThat(mediaInfo.getId()).isEqualTo(MEDIA_ID);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isNull();
        assertThat(mediaInfo.getDuration()).isNull();
        assertThat(mediaInfo.getTypeVideo()).isEqualTo(VideoTypeEnum.WITHOUT_URL);

        verify(mediaInfoDao).getMedia(MEDIA_ID);
    }

    private void mockGetMediaInfo(MediaTypeEnum type, String url, Integer duration) {
        MediaInfo mediaInfoFromDb = new MediaInfo();
        mediaInfoFromDb.setId(MEDIA_ID);
        mediaInfoFromDb.setType(type);
        mediaInfoFromDb.setUrl(url);
        mediaInfoFromDb.setDuration(duration);
        when(mediaInfoDao.getMedia(any())).thenReturn(mediaInfoFromDb);
    }
}