package ua.com.foxminded.school.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.school.dao.impl.CourseDaoImpl;
import ua.com.foxminded.school.dao.impl.GroupDaoImpl;
import ua.com.foxminded.school.dao.impl.StudentDaoImpl;
import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;
import ua.com.foxminded.school.generate.SqlRunner;
import ua.com.foxminded.school.validator.DataBaseRuntimeException;

class GroupsDaoImplTest {
    
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
    void saveShouldSaveGroupWhenGetCroup() {
        Group expected = new Group.Builder()
                .withGroupId(2)
                .withGroupName("name")
                .build();
        
        groupsDao.save(expected);
        Group actual = groupsDao.findById(2).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));;
    }
    
    @Test
    void saveAllShouldSaveCoursesWhenGetListOfCroup() {
        List<Group> groups = new ArrayList<>();
        
        Group firstGroup = new Group.Builder()
                .withGroupId(3)
                .withGroupName("firstGroup")
                .build();
        
        Group secondGroup = new Group.Builder()
                .withGroupId(4)
                .withGroupName("secondGroup")
                .build();

        groups.add(firstGroup);
        groups.add(secondGroup);
        groupsDao.saveAll(groups);
        
        List<Group> actual = groupsDao.findAll(0, 4);

        assertThat(actual).contains(firstGroup).contains(secondGroup);
    }
    
    @Test
    void findByIdShouldReturnGroupsWhenGetId() {
        Group expected = new Group.Builder()
                .withGroupId(2)
                .withGroupName("name")
                .build(); 
        
        Group actual = groupsDao.findById(2).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void updateShouldUpdateGroupWhenGetGroup() {
        Group expected = new Group.Builder()
                .withGroupId(2)
                .withGroupName("name")
                .build();  
        
        groupsDao.update(expected);
        Group actual = groupsDao.findById(2).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void deleteByIdShouldDeleteGroupWhenGetId() {
        groupsDao.deleteById(4);
        Group actual = groupsDao.findById(4).orElse(null);
         
        assertThat(actual).isNull();
    }
    
    @Test
    void findAllByStudentCountShouldReturnListOfGroupWhenGetParameters() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupsDao.findById(1).orElse(null));
        List<Group> actual = groupsDao.findAllByStudentCount(1);
 
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void findAllShouldReturnListOfGroupWhenGetParameters() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupsDao.findById(1).orElse(null));
        expected.add(groupsDao.findById(2).orElse(null));
        expected.add(groupsDao.findById(3).orElse(null));
        
        List<Group> actual = groupsDao.findAll(0, 3);
 
        assertThat(expected, is(equalTo(actual)));
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

        students.add(new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("Luke")
                .withLastName("Skywalker")
                .withCourses(courses)
                .build());
    }

    private static void insertTestDataToDB() throws DataBaseRuntimeException {
        groupsDao.saveAll(groups);
        coursesDao.saveAll(courses);
        studentsDao.saveAll(students);
        studentsDao.assignStudentsToCourses(students);
    }
}
