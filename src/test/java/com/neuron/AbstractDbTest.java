package com.neuron;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Connection;
import java.sql.Statement;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"unitTest", "local"})
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class AbstractDbTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static EmbeddedPostgres embeddedPostgres;

    @BeforeClass
    public static void initialise() throws Exception {
        if (embeddedPostgres == null) {
            embeddedPostgres = EmbeddedPostgres.builder()
                    .setPort(5433).start();

            try (Connection conn = embeddedPostgres.getPostgresDatabase().getConnection()) {
                Statement statement = conn.createStatement();
                statement.execute("CREATE DATABASE testdb");
            }
        }
    }

    @After
    public void after() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "MEDIA", "MEDIA_VIDEO");
    }

}
