package ua.com.foxminded.school.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

class StudentsDaoImplTest {
    private final static String PROPERTIES = "src/test/resources/sql.properties";
    private final static String SCRIPT_SQL = "src/test/resources/school.sql"; 
    
    private static List<Course> courses = new ArrayList<>();
    private final static List<Group> groups = new ArrayList<>();
    private final static List<Student> students = new ArrayList<Student>();
    
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
    void saveShouldSaveStudentWhenGetStudent() {
        Student expected = new Student.Builder()
                .withStudentId(4)
                .withGroupId(1)
                .withFirstName("Name")
                .withLastName("Surname")
                .build();
        
        studentsDao.save(expected);
        Student actual = studentsDao.findById(4).orElse(null);
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void saveShouldReturnExceptionWhenGetIncorrectParameters() {
        Student expected = new Student.Builder()
                .withStudentId(4)
                .withGroupId(1)
                .withFirstName(null)
                .withLastName("Surname")
                .build();
        
        assertThrows(DataBaseRuntimeException.class, () -> studentsDao.save(expected));      
    }
    
    @Test
    void saveAllShouldSaveStudentWhenGetListOfStudent() {
        List<Student> student = new ArrayList<>();
        
        Student firstStudent = new Student.Builder()
                .withStudentId(5)
                .withGroupId(1)
                .withFirstName("firstName")
                .withLastName("firstSurname")
                .build();
        
        Student secondStudent = new Student.Builder()
                .withStudentId(6)
                .withGroupId(1)
                .withFirstName("secondName")
                .withLastName("secondSurname")
                .build();
        
        student.add(firstStudent);
        student.add(secondStudent);
        studentsDao.saveAll(student);
        
        List<Student> actual = studentsDao.findAll(0, 6);
        assertThat(actual).contains(firstStudent).contains(secondStudent);
    }
    
    @Test
    void saveAllShouldReturnExceptionWhenGetIncorrectParameters() {
        List<Student> student = new ArrayList<>();
        
        Student firstStudent = new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName(null)
                .withLastName("firstSurname")
                .build();
        
        Student secondStudent = new Student.Builder()
                .withStudentId(4)
                .withGroupId(1)
                .withFirstName("secondName")
                .withLastName(null)
                .build();
        
        student.add(firstStudent);
        student.add(secondStudent);
        
        assertThrows(DataBaseRuntimeException.class, () -> studentsDao.saveAll(student));      
    }
    
    @Test
    void findByIdShouldReturnStudentWhenGetId() {
        Student expected = new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("Luke")
                .withLastName("Skywalker")
                .build();
        
        Student actual = studentsDao.findById(1).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }  
    
    @Test
    void updateShouldReturnExceptionWhenGetIncorrectParameters() {
        Student expected = new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName(null)
                .withLastName("Skywalker")
                .build();
        
        assertThrows(DataBaseRuntimeException.class, () -> studentsDao.update(expected)); 
    }
    
    @Test
    void updateShouldUpdateStudentWhenGetStudent() {
        Student expected = new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("Luke")
                .withLastName("Skywalker")
                .build();
        
        studentsDao.update(expected);
        Student actual = studentsDao.findById(1).orElse(null);
        
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void deleteByIdShouldDeleteStudentWhenGetId() {
        studentsDao.deleteById(4);
        Student actual = studentsDao.findById(4).orElse(null);
         
        assertThat(actual).isNull();
    }
    
    @Test
    void findAllShouldReturnListOfStudentWhenGetParameters() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentsDao.findById(1).orElse(null));
        expected.add(studentsDao.findById(4).orElse(null));
        
        List<Student> actual = studentsDao.findAll(0, 4);
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void findAllByCourseNameShouldReturnListOfStudentWhenGetParametersCourseName() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentsDao.findById(1).orElse(null));
        
        List<Student> actual = studentsDao.findAllByCourseName("Mathematics");
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void addToCourseShouldAddStudentToCourse() {
        List<Course> expected = new ArrayList<>();
        
        expected.add(new Course.Builder()
                .withCourseId(1)
                .withCourseName("Mathematics")
                .withCourseDescription("Description")
                .build());
        
        expected.add(new Course.Builder()
                .withCourseId(2)
                .withCourseName("Biology")
                .withCourseDescription("Description")
                .build());
        
        expected.add(new Course.Builder()
                .withCourseId(3)
                .withCourseName("Phics")
                .withCourseDescription("Description")
                .build());
        
        studentsDao.addToTheCourse(1, 3);
        List<Course> actual = coursesDao.findAllByStudentId(1);
        assertThat(expected, is(equalTo(actual)));
    }
    
    @Test
    void addToCourseShouldExceptionWhenGetIncorrectParameters() {
        assertThatThrownBy(() -> studentsDao.addToTheCourse(3, 4))
        .isInstanceOf(DataBaseRuntimeException.class).hasMessage("Can't add student to the course.");
    }
    
    @Test
    void removeFromCourseShouldRemoveStudentFromCourse() {
        List<Course> expected = new ArrayList<>();
        
        expected.add(new Course.Builder()
                .withCourseId(1)
                .withCourseName("Mathematics")
                .withCourseDescription("Description")
                .build());
        
        expected.add(new Course.Builder()
                .withCourseId(3)
                .withCourseName("Phics")
                .withCourseDescription("Description")
                .build());
        
        studentsDao.removeFromCourse(1, 2);
        
        List<Course> actual = coursesDao.findAllByStudentId(1);

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

        students.add(new Student
                .Builder()
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
