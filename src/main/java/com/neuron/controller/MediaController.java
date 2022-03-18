package com.neuron.controller;

import com.neuron.service.MediaInfoService;
import io.swagger.model.MediaInfo;
import io.swagger.model.QueryParamsForSavingMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/media_handler/v1")
public class MediaController {

    private final MediaInfoService mediaInfoService;

    @PostMapping(value = "/media", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveMediaInfo(@RequestBody QueryParamsForSavingMedia queryParams) {
        mediaInfoService.saveMediaInfo(queryParams);
    }

    @GetMapping(value = "/media/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MediaInfo getMediaInfo(@PathVariable("id") String id) {
        return mediaInfoService.getMediaInfo(id);
    }

}
