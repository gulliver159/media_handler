package com.neuron.dao;

import com.neuron.AbstractDbTest;
import com.neuron.exception.ServerException;
import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.neuron.common.MockObjects.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MediaInfoDaoTest extends AbstractDbTest {

    private final String ID_FOR_DB = RandomStringUtils.randomAlphabetic(36);

    @Autowired
    MediaInfoDao mediaInfoDao;

    @Test
    public void saveMediaNotExisted() throws ServerException {
        int insertsNumber = mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.VIDEO, MEDIA_URL));
        assertThat(insertsNumber).isOne();

        MediaInfo mediaInfo = mediaInfoDao.getMedia(ID_FOR_DB);
        assertThat(mediaInfo).isNotNull();
        assertThat(mediaInfo.getId()).isEqualTo(ID_FOR_DB);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
    }

    @Test
    public void saveMediaAlreadyExisted() throws ServerException {
        mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.VIDEO, MEDIA_URL));

        int insertsNumber = mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.VIDEO, MEDIA_URL));
        assertThat(insertsNumber).isZero();
    }

    @Test
    public void saveMediaDuplicatedId() throws ServerException {
        mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.IMAGE, MEDIA_URL));

        ServerException exception = assertThrows(
                ServerException.class,
                () -> mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.IMAGE, "another url"))
        );
        assertThat(exception.getError()).isNotNull();
        assertThat(exception.getError().getCode()).isEqualTo("duplicateId");
   }

    @Test
    public void saveVideoMedia() throws ServerException {
        mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.VIDEO, MEDIA_URL));
        mediaInfoDao.saveMediaVideo(ID_FOR_DB, MEDIA_DURATION);

        MediaInfo mediaInfo = mediaInfoDao.getMedia(ID_FOR_DB);
        assertThat(mediaInfo).isNotNull();
        assertThat(mediaInfo.getId()).isEqualTo(ID_FOR_DB);
        assertThat(mediaInfo.getDuration()).isEqualTo(MEDIA_DURATION);
    }

    @Test
    public void getVideoMedia() throws ServerException {
        mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.VIDEO, MEDIA_URL));
        mediaInfoDao.saveMediaVideo(ID_FOR_DB, MEDIA_DURATION);

        MediaInfo mediaInfo = mediaInfoDao.getMedia(ID_FOR_DB);
        assertThat(mediaInfo).isNotNull();
        assertThat(mediaInfo.getId()).isEqualTo(ID_FOR_DB);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
        assertThat(mediaInfo.getDuration()).isEqualTo(MEDIA_DURATION);
    }

    @Test
    public void getImageMedia() throws ServerException {
        mediaInfoDao.saveMedia(queryParams(ID_FOR_DB, MediaTypeEnum.IMAGE, MEDIA_URL));

        MediaInfo mediaInfo = mediaInfoDao.getMedia(ID_FOR_DB);
        assertThat(mediaInfo).isNotNull();
        assertThat(mediaInfo.getId()).isEqualTo(ID_FOR_DB);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.IMAGE);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
        assertThat(mediaInfo.getDuration()).isNull();
    }

    @Test
    public void getNotFound() {
        ServerException exception = assertThrows(
                ServerException.class,
                () -> mediaInfoDao.getMedia(ID_FOR_DB)
        );
        assertThat(exception.getError()).isNotNull();
        assertThat(exception.getError().getCode()).isEqualTo("notFound");
    }

}