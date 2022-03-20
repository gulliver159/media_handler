package com.neuron.dao.impl;

import com.neuron.dao.MediaInfoDao;
import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
@RequiredArgsConstructor
public class MediaInfoDaoImpl implements MediaInfoDao {

    private static final String INSERT_MEDIA_SQL = "INSERT INTO MEDIA VALUES(:id, :type, :url) " +
            "ON CONFLICT (ID, TYPE, URL) DO NOTHING";
    private static final String INSERT_MEDIA_VIDEO_SQL = "INSERT INTO MEDIA_VIDEO VALUES(:id, :duration)";
    private static final String SELECT_MEDIA_INFO_SQL = "SELECT MEDIA.ID, TYPE, URL, DURATION from MEDIA " +
            "LEFT JOIN MEDIA_VIDEO ON MEDIA.ID = MEDIA_VIDEO.ID " +
            "WHERE MEDIA.ID = :id;";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public static final RowMapper<MediaInfo> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setId(rs.getString("ID"));
        mediaInfo.setType(MediaTypeEnum.fromValue(rs.getString("TYPE")));
        mediaInfo.setUrl(rs.getString("URL"));
        int duration = rs.getInt("DURATION");
        mediaInfo.setDuration(rs.wasNull() ? null : duration);
        return mediaInfo;
    };

    @Override
    public int saveMedia(QueryParamsForSavingMedia queryParamsForSavingMedia) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", queryParamsForSavingMedia.getId())
                .addValue("type", queryParamsForSavingMedia.getType().toString())
                .addValue("url", queryParamsForSavingMedia.getUrl());

        KeyHolder holder = new GeneratedKeyHolder();
        return jdbcTemplate.update(INSERT_MEDIA_SQL, params, holder);
    }

    @Override
    public void saveMediaVideo(String id, int duration) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("duration", duration);

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_MEDIA_VIDEO_SQL, params, holder);
    }

    @Override
    public MediaInfo getMedia(String id) {
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.queryForObject(SELECT_MEDIA_INFO_SQL, sqlParams, ROW_MAPPER);
    }
}
