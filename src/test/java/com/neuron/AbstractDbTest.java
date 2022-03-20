package com.neuron;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.Statement;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"unitTest", "local"})
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class AbstractDbTest {

    private static EmbeddedPostgres embeddedPostgres;

    @BeforeClass
    public static void initialise() throws Exception{
        if(embeddedPostgres == null) {
            //Create an instance of embedded postgress
            embeddedPostgres = EmbeddedPostgres.builder()
                    .setPort(5433).start();

            try (Connection conn = embeddedPostgres.getPostgresDatabase().getConnection()) {
                Statement statement = conn.createStatement();
                statement.execute("CREATE DATABASE testdb");
            }
        }
    }

}
