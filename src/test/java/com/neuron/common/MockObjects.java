package com.neuron.common;

import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;

public class MockObjects {

    public static final String MEDIA_ID = "id";
    public static final String MEDIA_URL = "url";
    public static final Integer MEDIA_DURATION = 30;

    public static QueryParamsForSavingMedia queryParams(String id, MediaTypeEnum type, String url) {
        return new QueryParamsForSavingMedia()
                .id(id)
                .type(type)
                .url(url);
    }

}
