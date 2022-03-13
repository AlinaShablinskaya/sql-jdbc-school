package ua.com.foxminded.school.dao;

import java.sql.Connection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.Test;

class ConnectionFactoryTest {
    
    @Test
    void testConnection() {
        String properties = "src/test/resources/sql.properties";
        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
        Connection actual = connectionFactory.connect();
        assertThat(actual, instanceOf(Connection.class));
    }  
}
