package ua.com.foxminded.school.generate;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.ArrayList;
import java.util.List;

class DBGeneratorImplTest {
    private final static String COURSES_TXT = "src/main/resources/courses.txt";
    private final static String FIRST_NAME_TXT = "src/main/resources/first_name.txt";
    private final static String LAST_NAME_TXT = "src/main/resources/last_name.txt";
    
    private final DBGenerator generator = new DBGeneratorImpl(COURSES_TXT, FIRST_NAME_TXT, LAST_NAME_TXT);

    @Test
    void generateGroupsTest() {
        final List<Group> actual = new ArrayList<>(generator.generateGroups());
        
        actual.forEach(a -> assertThat(a, instanceOf(Group.class)));
    }
    
    @Test
    void generateCoursesTest() {
        final List<Course> actual = new ArrayList<>(generator.generateCourses());
        
        actual.forEach(a -> assertThat(a, instanceOf(Course.class)));
    }
    
    @Test
    void generateStudentsTest() {
        List<Group> groups = new ArrayList<>(generator.generateGroups());
        List<Course> courses = new ArrayList<>(generator.generateCourses());
        final List<Student> actual = new ArrayList<>(generator.generateStudents(groups, courses));
        
        actual.forEach(a -> assertThat(a, instanceOf(Student.class)));
    }
}
