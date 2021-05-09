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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("Slow")
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class LothricDataSourceIntTest {

    // The lothric data source is an optional secondary data source that is resolved
    // by specifying the qualifier "lothricDataSource"
    @Autowired
    @Qualifier("lothricDataSource")
    DataSource lothricDataSource;

    @Test
    public void canReadFromLothricDatabase() throws SQLException {
        final Connection connection = lothricDataSource.getConnection();
        final Statement statement = connection.createStatement();
        statement.execute("SELECT id FROM l_account WHERE id=37");
        final ResultSet resultSet = statement.getResultSet();
        assertTrue(resultSet.next());
        final int value = resultSet.getInt(1);
        assertFalse(resultSet.next());
        connection.close();

        assertEquals(37, value);
    }
}
