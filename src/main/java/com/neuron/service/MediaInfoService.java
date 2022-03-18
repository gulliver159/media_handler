package com.neuron.service;

import io.swagger.model.MediaInfo;
import io.swagger.model.QueryParamsForSavingMedia;

public interface MediaInfoService {

    void saveMediaInfo(QueryParamsForSavingMedia queryParams);

    MediaInfo getMediaInfo(String id);

}
