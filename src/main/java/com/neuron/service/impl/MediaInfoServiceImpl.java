package com.neuron.service.impl;

import com.neuron.dao.MediaInfoDao;
import com.neuron.service.MediaInfoService;
import com.neuron.service.VideoInfoService;
import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import io.swagger.model.VideoTypeEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MediaInfoServiceImpl implements MediaInfoService {

    private final MediaInfoDao mediaInfoDao;
    private final VideoInfoService videoInfoService;

    @Override
    public void saveMediaInfo(QueryParamsForSavingMedia queryParams) {
        int insertsNumber = mediaInfoDao.saveMedia(queryParams);
        boolean notExisted = insertsNumber == 1;

        if (notExisted && queryParams.getUrl() != null && MediaTypeEnum.VIDEO.equals(queryParams.getType())) {
            videoInfoService.processVideoInfo(queryParams);
        }
    }

    @Override
    public MediaInfo getMediaInfo(String id) {
        MediaInfo mediaInfo = mediaInfoDao.getMedia(id);

        if (!MediaTypeEnum.VIDEO.equals(mediaInfo.getType())) {
            return mediaInfo;
        }

        VideoTypeEnum videoType = videoTypeChoiceStream(mediaInfo)
                .filter(Pair::getKey)
                .map(Pair::getRight)
                .findFirst()
                .orElse(null);

        mediaInfo.setTypeVideo(videoType);
        return mediaInfo;
    }

    private Stream<Pair<Boolean, VideoTypeEnum>> videoTypeChoiceStream(MediaInfo mediaInfo) {
        return Stream.of(
                Pair.of(mediaInfo.getDuration() != null, VideoTypeEnum.WITH_DURATION),
                Pair.of(mediaInfo.getUrl() != null, VideoTypeEnum.WITHOUT_DURATION_YET),
                Pair.of(mediaInfo.getUrl() == null, VideoTypeEnum.WITHOUT_URL)
        );
    }
}
