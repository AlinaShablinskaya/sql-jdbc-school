package ua.com.foxminded.school;

import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;
import ua.com.foxminded.school.generate.DBGenerator;
import ua.com.foxminded.school.generate.SqlRunner;
import ua.com.foxminded.school.menu.MenuExecutor;
import ua.com.foxminded.school.validator.DataBaseRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class SchoolApplicationFacade {
    private static final String SCRIPT_FILE = "src/main/resources/school.sql";
    
    private final CourseDao coursesDao;
    private final GroupDao groupsDao;
    private final StudentDao studentsDao;
    private final SqlRunner sqlRunner;
    private final DBGenerator generator;
    private final MenuExecutor menuExecutor;   
    
    public SchoolApplicationFacade(CourseDao coursesDao, GroupDao groupsDao, StudentDao studentsDao,
            SqlRunner sqlRunner, DBGenerator generator,
            MenuExecutor menuExecutor) {
        this.coursesDao = coursesDao;
        this.groupsDao = groupsDao;
        this.studentsDao = studentsDao;
        this.sqlRunner = sqlRunner;
        this.generator = generator;
        this.menuExecutor = menuExecutor;
    }

    public void run() {
        sqlRunner.runScript(SCRIPT_FILE);
        
        List<Course> courses = new ArrayList<>(generator.generateCourses());
        List<Group> groups = new ArrayList<>(generator.generateGroups());
        List<Student> students = new ArrayList<>(generator.generateStudents(groups, courses));
                
        try {
            groupsDao.saveAll(groups);
            coursesDao.saveAll(courses);
            studentsDao.saveAll(students);
            studentsDao.assignStudentsToCourses(students);
        } catch (DataBaseRuntimeException throwables) {
            throwables.printStackTrace();
        }
        menuExecutor.startMenu();
    }
}
