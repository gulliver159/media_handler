package com.neuron.service;

import io.swagger.model.QueryParamsForSavingMedia;

public interface VideoDurationCalculationService {

    Integer calc(QueryParamsForSavingMedia queryParams);
}
