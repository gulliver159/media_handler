package com.neuron.service;

import com.neuron.exception.ServerException;
import io.swagger.model.MediaInfo;
import io.swagger.model.QueryParamsForSavingMedia;

public interface MediaInfoService {

    void saveMediaInfo(QueryParamsForSavingMedia queryParams) throws ServerException;

    MediaInfo getMediaInfo(String id) throws ServerException;

}
