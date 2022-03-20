package com.neuron.dao;

import com.neuron.exception.ServerException;
import io.swagger.model.MediaInfo;
import io.swagger.model.QueryParamsForSavingMedia;

public interface MediaInfoDao {

    int saveMedia(QueryParamsForSavingMedia queryParamsForSavingMedia) throws ServerException;

    void saveMediaVideo(String id, int duration);

    MediaInfo getMedia(String id) throws ServerException;
}
