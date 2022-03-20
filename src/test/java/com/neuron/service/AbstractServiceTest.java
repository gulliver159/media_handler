package com.neuron.service;

import com.neuron.Application;
import com.neuron.dao.MediaInfoDao;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"unitTest", "local"})
public abstract class AbstractServiceTest {

    @MockBean
    protected MediaInfoDao mediaInfoDao;

}
