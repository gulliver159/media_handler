package com.neuron.service;

import com.neuron.Application;
import com.neuron.dao.MediaInfoDao;
import io.swagger.model.MediaTypeEnum;
import io.swagger.model.QueryParamsForSavingMedia;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"unitTest", "local"})
public abstract class AbstractServiceTest {

    protected static final String MEDIA_ID = "id";
    protected static final String MEDIA_URL = "url";
    protected static final Integer MEDIA_DURATION = 30;

    @MockBean
    protected MediaInfoDao mediaInfoDao;

    protected QueryParamsForSavingMedia queryParams(MediaTypeEnum type, String url) {
        QueryParamsForSavingMedia queryParams = new QueryParamsForSavingMedia();
        queryParams.setId(MEDIA_ID);
        queryParams.setType(type);
        queryParams.setUrl(url);
        return queryParams;
    }

}
