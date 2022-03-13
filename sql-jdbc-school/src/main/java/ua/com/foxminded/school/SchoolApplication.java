package ua.com.foxminded.school;

import java.sql.SQLException;
import java.util.Scanner;

import ua.com.foxminded.school.dao.ConnectionFactory;
import ua.com.foxminded.school.dao.impl.CourseDaoImpl;
import ua.com.foxminded.school.dao.impl.GroupDaoImpl;
import ua.com.foxminded.school.dao.impl.StudentDaoImpl;
import ua.com.foxminded.school.generate.DBGeneratorImpl;
import ua.com.foxminded.school.generate.SqlRunner;
import ua.com.foxminded.school.menu.MenuExecutor;
import ua.com.foxminded.school.menu.MenuExecutorImpl;
import ua.com.foxminded.school.viewprovider.ViewProvider;

public class SchoolApplication {
    private static final String PROPERTIES = "src/main/resources/sql.properties";
    private static final String COURSES_FILE = "src/main/resources/courses.txt";
    private static final String FIRST_NAME_FILE = "src/main/resources/first_name.txt";
    private static final String LAST_NAME_FILE = "src/main/resources/last_name.txt";
            
    public static void main( String[] args) throws SQLException {
        DBGeneratorImpl generator = new DBGeneratorImpl(COURSES_FILE, FIRST_NAME_FILE, LAST_NAME_FILE);
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES);
        SqlRunner sqlRunner = new SqlRunner(connectionFactory);
        CourseDaoImpl coursesDao = new CourseDaoImpl(connectionFactory);
        GroupDaoImpl groupsDao = new GroupDaoImpl(connectionFactory);
        StudentDaoImpl studentsDao = new StudentDaoImpl(connectionFactory);
        Scanner scanner = new Scanner(System.in);
        ViewProvider viewProvider = new ViewProvider(scanner);
        MenuExecutor menuExecutor = new MenuExecutorImpl(studentsDao, groupsDao, viewProvider);
              
        SchoolApplicationFacade schoolApplicationFacade = 
                new SchoolApplicationFacade(coursesDao, groupsDao, 
                        studentsDao, sqlRunner, generator, menuExecutor);
        
        schoolApplicationFacade.run();
    }
}
