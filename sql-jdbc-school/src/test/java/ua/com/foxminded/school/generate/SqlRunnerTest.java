package ua.com.foxminded.school.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import ua.com.foxminded.school.dao.ConnectionFactory;

class SqlRunnerTest {
    private final static String PROPERTIES = "src/test/resources/sql.properties";
    private final static String SCRIPT_SQL = "src/test/resources/school.sql";
    
    public static final String SELECT_ALL_GROUPS = "SELECT * FROM school.groups";
    public static final String SELECT_ALL_COURSES = "SELECT * FROM school.courses";
    public static final String SELECT_ALL_STUDENTS = "SELECT * FROM school.students";
    public static final String SELECT_ALL_STUDENTS_COURSES = "SELECT * FROM school.students_courses";
   
    private static SqlRunner sqlRunner;
    private static ConnectionFactory connectionFactory;
    
    @BeforeAll
    static void setup() {
        connectionFactory = new ConnectionFactory(PROPERTIES);
        sqlRunner = new SqlRunner(connectionFactory);
        sqlRunner.runScript(SCRIPT_SQL);
    }

    @Test
    void shouldCompareDataFromTheGroupTableWithExpectedGroup() {
        try {
            Connection connection = connectionFactory.connect();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_GROUPS);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            assertEquals("GROUP_ID", metaData.getColumnName(1));
            assertEquals("GROUP_NAME", metaData.getColumnName(2));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    @Test
    void shouldCompareDataFromTheCourseTableWithCourseList() {
        try {
            Connection connection = connectionFactory.connect();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_COURSES);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            assertEquals("COURSE_ID", metaData.getColumnName(1));
            assertEquals("COURSE_NAME", metaData.getColumnName(2));
            assertEquals("COURSE_DESCRIPTION", metaData.getColumnName(3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    @Test
    void shouldCompareDataFromTheStudentsTableWithStudentsList() {
        try {
            Connection connection = connectionFactory.connect();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STUDENTS);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            assertEquals("STUDENT_ID", metaData.getColumnName(1));
            assertEquals("GROUP_ID", metaData.getColumnName(2));
            assertEquals("FIRST_NAME", metaData.getColumnName(3));
            assertEquals("LAST_NAME", metaData.getColumnName(4));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } 
    }
    
    @Test
    void shouldCompareDataFromTheStudentsCoursesTableWithStudentsList() {
        try {
            Connection connection = connectionFactory.connect();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STUDENTS_COURSES);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            assertEquals("STUDENT_ID", metaData.getColumnName(1));
            assertEquals("COURSE_ID", metaData.getColumnName(2));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } 
    }
}
