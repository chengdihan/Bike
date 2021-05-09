package com.newengen.starterservice.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Tag("Slow")
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:sql/test-data.sql")
public class PeopleDataSourceIntTest {

    // This is the primary datasource for the service
    // @Qualifier annotation is not necessary to resolve the primary DataSource instance.
    @Autowired
    DataSource greetingDataSource;

    @Test
    public void canConnectToDefaultDataSource() throws SQLException {
        final Connection connection = greetingDataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute("SELECT id FROM people LIMIT 1");
        final ResultSet resultSet = statement.getResultSet();
        assertTrue(resultSet.next());
        final int value = resultSet.getInt(1);
        assertFalse(resultSet.next());
        connection.close();
        assertEquals(1, value);
    }
}
