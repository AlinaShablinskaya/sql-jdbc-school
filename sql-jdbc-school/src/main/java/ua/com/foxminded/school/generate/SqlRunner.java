package ua.com.foxminded.school.generate;

import java.sql.Connection;
import org.hsqldb.cmdline.SqlFile;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ua.com.foxminded.school.dao.ConnectionFactory;

import java.io.File;

public class SqlRunner {
    private static final Logger LOGGER = LogManager.getLogger(SqlRunner.class);
    
    private final ConnectionFactory connectionFactory;
 
    public SqlRunner(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void runScript(String scriptFile) {
  
        try (Connection connection = connectionFactory.connect()) {
            SqlFile file = new SqlFile(new File(scriptFile));
            file.setConnection(connection);
            file.execute();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}
