package ua.com.foxminded.school.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.school.dao.impl.CourseDaoImpl;
import ua.com.foxminded.school.dao.impl.GroupDaoImpl;
import ua.com.foxminded.school.dao.impl.StudentDaoImpl;
import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;
import ua.com.foxminded.school.generate.SqlRunner;
import ua.com.foxminded.school.validator.DataBaseRuntimeException;

class CoursesDaoImplTest {

    private final static String PROPERTIES = "src/test/resources/sql.properties";
    private final static String SCRIPT_SQL = "src/test/resources/school.sql";
    
    private static List<Course> courses = new ArrayList<>();
    private final static List<Group> groups = new ArrayList<>();
    private final static List<Student> students = new ArrayList<>();
    
    private static SqlRunner sqlRunner;
    private static ConnectionFactory connectionFactory;
    private static CourseDao coursesDao;
    private static GroupDaoImpl groupsDao;
    private static StudentDao studentsDao;
    
    @BeforeAll
    static void setup() {
        connectionFactory = new ConnectionFactory(PROPERTIES);
        sqlRunner = new SqlRunner(connectionFactory);
        coursesDao = new CourseDaoImpl(connectionFactory);
        groupsDao = new GroupDaoImpl(connectionFactory);
        studentsDao = new StudentDaoImpl(connectionFactory);
        
        createTestData();
        insertTestDataToDB();
    }
    
    @Test
    void saveShouldSaveCourseWhenGetCourse() {
        Course expected = new Course.Builder()
                .withCourseId(4)
                .withCourseName("Name")
                .withCourseDescription("Description")
                .build();
        
        coursesDao.save(expected);
        Course actual = coursesDao.findById(4).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void saveAllShouldSaveCoursesWhenGetListOfCourses() {
        List<Course> courses = new ArrayList<>();
        
        Course firstCourse = new Course.Builder()
                .withCourseId(4)
                .withCourseName("firstName")
                .withCourseDescription("firstDescription")
                .build();
        
        Course secondCourse = new Course.Builder()
                .withCourseId(5)
                .withCourseName("secondName")
                .withCourseDescription("secondDescription")
                .build();     
        
        courses.add(firstCourse);
        courses.add(secondCourse);
        coursesDao.saveAll(courses);  
        List<Course> actual = coursesDao.findAll(0, 5);
        
        assertThat(actual).contains(firstCourse).contains(secondCourse);
    }
    
    @Test
    void findByIdShouldReturnCourseWhenGetId() {
        Course expected = new Course.Builder()
                .withCourseId(1)
                .withCourseName("Mathematics")
                .withCourseDescription("Description")
                .build(); 

        Course actual = coursesDao.findById(1).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void updateShouldUpdateCourseWhenGetCourse() {
        Course expected = new Course.Builder()
                .withCourseId(3)
                .withCourseName("History")
                .withCourseDescription("Description")
                .build();
        
        coursesDao.update(expected);
        Course actual = coursesDao.findById(3).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void findAllShouldReturnListOfCoursesWhenGetParameters() {
        List<Course> expected = new ArrayList<>();
        expected.add(coursesDao.findById(1).orElse(null));
        expected.add(coursesDao.findById(2).orElse(null));
        expected.add(coursesDao.findById(3).orElse(null));
        
        List<Course> actual = coursesDao.findAll(0, 3);
 
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void deleteByIdShouldDeleteCourseWhenGetId() {
        coursesDao.deleteById(4);
        Course actual = coursesDao.findById(4).orElse(null);
         
        assertThat(actual).isNull();
    }
    
    private static void createTestData() {
        sqlRunner.runScript(SCRIPT_SQL);
        
        groups.add(new Group.Builder().withGroupId(1).withGroupName("BM-24").build());
        
        
        courses.add(new Course.Builder()
                .withCourseId(1)
                .withCourseName("Mathematics")
                .withCourseDescription("Description")
                .build());
        courses.add(new Course.Builder().withCourseId(2)
                .withCourseName("Biology")
                .withCourseDescription("Description")
                .build());
        courses.add(new Course.Builder()
                .withCourseId(3)
                .withCourseName("Phics")
                .withCourseDescription("Description")
                .build());
        
        students.add(new Student.Builder().withStudentId(1).withGroupId(1)
                .withFirstName("Luke").withLastName("Skywalker").withCourses(courses).build());  
    }
    
    private static void insertTestDataToDB() throws DataBaseRuntimeException {
        groupsDao.saveAll(groups);
        coursesDao.saveAll(courses);
        studentsDao.saveAll(students);
        studentsDao.assignStudentsToCourses(students);
    }
}
