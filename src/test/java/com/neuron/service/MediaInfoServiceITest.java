package com.neuron.service;

import com.neuron.Application;
import com.neuron.dao.MediaInfoDao;
import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import io.swagger.model.VideoTypeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"unitTest", "local"})
public class MediaInfoServiceITest {

    private static final String MEDIA_ID = "id";
    private static final String MEDIA_URL = "url";
    private static final Integer MEDIA_DURATION = 30;
    private static final Integer INSERTS_NUMBER_ALREADY_EXISTED = 0;
    private static final Integer INSERTS_NUMBER_NOT_EXISTED = 1;

    @Autowired
    MediaInfoService mediaInfoService;

    @MockBean
    VideoInfoService videoInfoService;

    @MockBean
    MediaInfoDao mediaInfoDao;

    @Before
    public void before() {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setId("qwe");
        when(mediaInfoDao.getMedia(any())).thenReturn(mediaInfo);
    }

    @Test
    public void saveMediaInfoNotExistedVideo() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = new QueryParamsForSavingMedia();
        queryParams.setId(MEDIA_ID);
        queryParams.setType(MediaTypeEnum.VIDEO);
        queryParams.setUrl(MEDIA_URL);

        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService).processVideoInfo(queryParams);
    }

    @Test
    public void saveMediaInfoAlreadyExistedVideo() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_ALREADY_EXISTED);

        QueryParamsForSavingMedia queryParams = new QueryParamsForSavingMedia();
        queryParams.setId(MEDIA_ID);
        queryParams.setType(MediaTypeEnum.VIDEO);
        queryParams.setUrl(MEDIA_URL);

        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void saveMediaInfoNotExistedNotVideo() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = new QueryParamsForSavingMedia();
        queryParams.setId(MEDIA_ID);
        queryParams.setType(MediaTypeEnum.IMAGE);
        queryParams.setUrl(MEDIA_URL);

        mediaInfoService.saveMediaInfo(queryParams);

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(videoInfoService, never()).processVideoInfo(any());
    }

    @Test
    public void saveMediaInfoAlreadyExistedEmptyUrl() {
        when(mediaInfoDao.saveMedia(any())).thenReturn(INSERTS_NUMBER_NOT_EXISTED);

        QueryParamsForSavingMedia queryParams = new QueryParamsForSavingMedia();
        queryParams.setId(MEDIA_ID);
        queryParams.setType(MediaTypeEnum.VIDEO);

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