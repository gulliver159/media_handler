package com.neuron.dao.impl;

import com.neuron.dao.MediaInfoDao;
import com.neuron.exception.ServerException;
import io.swagger.model.Error;
import io.swagger.model.MediaInfo;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        int duration = rs.getInt("DURATION");
        return new MediaInfo()
                .id(rs.getString("ID"))
                .type(MediaTypeEnum.fromValue(rs.getString("TYPE")))
                .url(rs.getString("URL"))
                .duration(rs.wasNull() ? null : duration);
    };

    @Override
    public int saveMedia(QueryParamsForSavingMedia queryParamsForSavingMedia) throws ServerException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", queryParamsForSavingMedia.getId())
                .addValue("type", queryParamsForSavingMedia.getType().toString())
                .addValue("url", queryParamsForSavingMedia.getUrl());

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            return jdbcTemplate.update(INSERT_MEDIA_SQL, params, holder);
        } catch (DuplicateKeyException ex) {
            Error error = new Error()
                    .code("duplicateId")
                    .message("The database already has media with this id and with other field values");
            throw new ServerException(error);
        }
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
    public MediaInfo getMedia(String id) throws ServerException {
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            return jdbcTemplate.queryForObject(SELECT_MEDIA_INFO_SQL, sqlParams, ROW_MAPPER);
        } catch (EmptyResultDataAccessException ex) {
            Error error = new Error()
                    .code("notFound")
                    .message("Media with this id was not found in the database");
            throw new ServerException(error);
        }
    }
}
