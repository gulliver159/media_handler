package com.neuron.service;

import com.neuron.exception.ServerException;
import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import io.swagger.model.VideoTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.neuron.common.MockObjects.*;
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
    public void saveMediaInfoNotExistedVideo() throws ServerException {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, MEDIA_URL);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService).processVideoInfo(queryParams);
    }

    @Test
    public void saveMediaInfoAlreadyExistedVideo() throws ServerException {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_ALREADY_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, MEDIA_URL);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void saveMediaInfoNotExistedNotVideo() throws ServerException {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.IMAGE, MEDIA_URL);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void saveMediaInfoAlreadyExistedEmptyUrl() throws ServerException {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, null);
        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void getMediaInfoImage() throws ServerException {
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
    public void getMediaInfoWithDuration() throws ServerException {
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
    public void getMediaInfoWithoutDuration() throws ServerException {
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
    public void getMediaInfoWithoutUrl() throws ServerException {
        mockGetMediaInfo(MediaTypeEnum.VIDEO, null, null);

        MediaInfo mediaInfo = mediaInfoService.getMediaInfo(MEDIA_ID);
        assertThat(mediaInfo.getId()).isEqualTo(MEDIA_ID);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isNull();
        assertThat(mediaInfo.getDuration()).isNull();
        assertThat(mediaInfo.getTypeVideo()).isEqualTo(VideoTypeEnum.WITHOUT_URL);

        verify(mediaInfoDao).getMedia(MEDIA_ID);
    }

    private void mockGetMediaInfo(MediaTypeEnum type, String url, Integer duration) throws ServerException {
        MediaInfo mediaInfoFromDb = new MediaInfo()
                .id(MEDIA_ID)
                .type(type)
                .url(url)
                .duration(duration);
        when(mediaInfoDao.getMedia(any())).thenReturn(mediaInfoFromDb);
    }
}