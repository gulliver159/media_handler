package com.neuron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuron.AbstractDbTest;
import com.neuron.dao.MediaInfoDao;
import lombok.SneakyThrows;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebAppConfiguration
public abstract class AbstractControllerTest extends AbstractDbTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @SpyBean
    protected MediaInfoDao mediaInfoDao;


    protected MockMvc mvc;

    @Before
    public final void before() {
        if (this.mvc == null) {
            initializeMvc();
        }
    }

    private void initializeMvc() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @SneakyThrows
    protected String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }
}
