package com.neuron.controller;

import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import io.swagger.model.VideoTypeEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.neuron.common.MockObjects.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MediaControllerTest extends AbstractControllerTest {

    private static final String POST_URL = "/media_handler/v1/media";
    private static final String GET_URL = "/media_handler/v1/media/" + MEDIA_ID;

    @Test
    public void saveMediaVideoOk() throws Exception {
        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, MEDIA_URL);
        mvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(queryParams)))
                .andExpect(status().isOk());

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(mediaInfoDao).saveMediaVideo(eq(MEDIA_ID), anyInt());
    }

    @Test
    public void saveMediaImageOk() throws Exception {
        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.IMAGE, MEDIA_URL);
        mvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(queryParams)))
                .andExpect(status().isOk());

        verify(mediaInfoDao).saveMedia(queryParams);
        verify(mediaInfoDao, never()).saveMediaVideo(any(), anyInt());
    }

    @Test
    public void saveMediaVideoDuplicatedId() throws Exception {
        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.IMAGE, MEDIA_URL);
        mvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(queryParams)));

        QueryParamsForSavingMedia anotherQueryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, "another url");
        mvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(anotherQueryParams)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("duplicateId")));

        verify(mediaInfoDao).saveMedia(anotherQueryParams);
        verify(mediaInfoDao, never()).saveMediaVideo(any(), anyInt());
    }

    @Test
    public void saveMediaValidationError() throws Exception {
        QueryParamsForSavingMedia queryParams = queryParams(null, null, RandomStringUtils.randomAlphabetic(256));
        mvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(queryParams)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("validationError")))
                .andExpect(jsonPath("$.attrErrors").exists())
                .andExpect(jsonPath("$.attrErrors", hasSize(3)))
                .andExpect(jsonPath("$.attrErrors[?(@.attr == 'id')].message", hasItem("must not be null")))
                .andExpect(jsonPath("$.attrErrors[?(@.attr == 'type')].message", hasItem("field value must be from enumeration")))
                .andExpect(jsonPath("$.attrErrors[?(@.attr == 'url')].message", hasItem("size must be between 0 and 255")));


        verify(mediaInfoDao, never()).saveMedia(any());
        verify(mediaInfoDao, never()).saveMediaVideo(any(), anyInt());
    }

    @Test
    public void getMediaInfoOk() throws Exception {
        QueryParamsForSavingMedia queryParams = queryParams(MEDIA_ID, MediaTypeEnum.VIDEO, MEDIA_URL);
        mvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(queryParams)));

        MvcResult result = mvc.perform(get(GET_URL))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        MediaInfo mediaInfo = objectMapper.readValue(json, MediaInfo.class);

        assertThat(mediaInfo).isNotNull();
        assertThat(mediaInfo.getId()).isEqualTo(MEDIA_ID);
        assertThat(mediaInfo.getType()).isEqualTo(MediaTypeEnum.VIDEO);
        assertThat(mediaInfo.getUrl()).isEqualTo(MEDIA_URL);
        assertThat(mediaInfo.getTypeVideo()).isEqualTo(VideoTypeEnum.WITH_DURATION);
        assertThat(mediaInfo.getDuration()).isNotNull();
    }

    @Test
    public void getMediaInfoNotFound() throws Exception {
        mvc.perform(get(GET_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("notFound")));
    }
}