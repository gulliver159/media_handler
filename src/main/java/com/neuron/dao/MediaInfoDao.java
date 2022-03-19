package com.neuron.dao;

import io.swagger.model.MediaInfo;
import io.swagger.model.QueryParamsForSavingMedia;

public interface MediaInfoDao {

    int saveMedia(QueryParamsForSavingMedia queryParamsForSavingMedia);

    void saveMediaVideo(String id, int duration);

    MediaInfo getMedia(String id);
}
