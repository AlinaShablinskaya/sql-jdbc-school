package ua.com.foxminded.school.menu;

import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;
import ua.com.foxminded.school.viewprovider.ViewProvider;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuExecutorImplTest {
    @Mock
    private ViewProvider viewProvider;
    
    @Mock
    private CourseDao courseDao;
    
    @Mock
    private GroupDao groupDao;
    
    @Mock
    private StudentDao studentDao; 
    
    @InjectMocks
    private MenuExecutorImpl menuExecutor;

    @Test
    void findAllGroupsWithStudentCountShouldReturnCorrectResult() {
        List<Group> result = Arrays.asList(
                new Group.Builder().withGroupId(1).withGroupName("AM-12").build(),
                new Group.Builder().withGroupId(2).withGroupName("BP-25").build()); 
        int studentCount = 5;
        
        when(viewProvider.readInt()).thenReturn(1).thenReturn(studentCount).thenReturn(7);
        when(groupDao.findAllByStudentCount(studentCount)).thenReturn(result);


        menuExecutor.startMenu();
        
        verify(viewProvider, times(3)).readInt();
        verify(groupDao).findAllByStudentCount(studentCount);
    }
    
    @Test
    void findAllStudentsToCourseNameCorrectResult() {
        List<Student> result = Arrays.asList(
                new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("name")
                .withLastName("surname")
                .build(),
                new Student.Builder()
                .withStudentId(2)
                .withGroupId(1)
                .withFirstName("name")
                .withLastName("surname")
                .build());   
        String courseName = "math";
        
        when(viewProvider.readInt()).thenReturn(2).thenReturn(7);
        when(viewProvider.readString()).thenReturn(courseName);
        when(studentDao.findAllByCourseName(courseName)).thenReturn(result);

        menuExecutor.startMenu();
        
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).readString();
        verify(studentDao).findAllByCourseName("math");
    }
    
    @Test
    void addStudentShouldReturnCorrectResult() {
        Student student = new Student.Builder()
                .withStudentId(1)
                .withGroupId(1)
                .withFirstName("name")
                .withLastName("surname")
                .build();
        
        when(viewProvider.readInt()).thenReturn(3).thenReturn(1).thenReturn(1).thenReturn(7);
        when(viewProvider.readString()).thenReturn("name").thenReturn("surname");
        doNothing().when(studentDao).save(student);

        menuExecutor.startMenu();
        
        verify(viewProvider, times(4)).readInt();
        verify(studentDao).save(student);
    }
    
    @Test
    void deleteStudentByIdShouldReturnCorrectResult() {
        when(viewProvider.readInt()).thenReturn(4).thenReturn(1).thenReturn(7);
        doNothing().when(studentDao).deleteById(1);
        
        menuExecutor.startMenu();
        
        verify(viewProvider, times(3)).readInt();
        verify(studentDao).deleteById(1);   
    }
    
    @Test
    void addStudentToCourseShouldReturnCorrectResult() {
        when(viewProvider.readInt()).thenReturn(5).thenReturn(1).thenReturn(1).thenReturn(7);
        doNothing().when(studentDao).addToTheCourse(1, 1);
        
        menuExecutor.startMenu();
        
        verify(viewProvider, times(4)).readInt();
        verify(studentDao).addToTheCourse(1, 1);
    }
    
    @Test
    void removeStudentFromCourseShouldReturnCorrectResult() {
        when(viewProvider.readInt()).thenReturn(6).thenReturn(1).thenReturn(1).thenReturn(7);
        doNothing().when(studentDao).removeFromCourse(1, 1);
        
        menuExecutor.startMenu();
        
        verify(viewProvider, times(4)).readInt();
        verify(studentDao).removeFromCourse(1, 1);
    }
    
    @Test
    void startMenuShouldReturnDefault() {
        String message = "Toolbar\n" + "1. Find all groups with less or equals student count.\n"
                + "2. Find all students related to course with given name.\n" + "3. Add new student.\n"
                + "4. Delete student by STUDENT_ID.\n" + "5. Add a student to the course (from a list).\n"
                + "6. Remove the student from one of his or her courses.\n" + "7. Exit\n"
                + "Enter a number from the list:\n";
        when(viewProvider.readInt()).thenReturn(12).thenReturn(7);
        doNothing().when(viewProvider).printMessage(message);
        
        menuExecutor.startMenu();
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider, times(2)).printMessage(message);
    }
}
