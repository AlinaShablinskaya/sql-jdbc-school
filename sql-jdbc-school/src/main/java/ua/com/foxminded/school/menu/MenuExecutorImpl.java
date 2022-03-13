package ua.com.foxminded.school.menu;

import ua.com.foxminded.school.dao.impl.StudentDaoImpl;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.dao.impl.GroupDaoImpl;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;
import ua.com.foxminded.school.validator.DataBaseRuntimeException;
import ua.com.foxminded.school.viewprovider.ViewProvider;

import java.util.List;

public class MenuExecutorImpl implements MenuExecutor {
    private final StudentDao studentsDao;
    private final GroupDao groupsDao;
    private final ViewProvider viewProvider;

    public MenuExecutorImpl(StudentDao studentsDao, GroupDao groupsDao,
            ViewProvider viewProvider) {
        this.studentsDao = studentsDao;
        this.groupsDao = groupsDao;
        this.viewProvider = viewProvider;
    }

    @Override
    public void startMenu() {
        boolean isWork = true;
        while (isWork) {
            viewProvider.printMessage("Toolbar\n" + "1. Find all groups with less or equals student count.\n"
                    + "2. Find all students related to course with given name.\n" + "3. Add new student.\n"
                    + "4. Delete student by STUDENT_ID.\n" + "5. Add a student to the course (from a list).\n"
                    + "6. Remove the student from one of his or her courses.\n" + "7. Exit\n"
                    + "Enter a number from the list:\n");
            int choose = viewProvider.readInt();

            switch (choose) {
            case 1:
                findAllGroupsWithStudentCount();
                break;
            case 2:
                findAllStudentsToCourseName();
                break;
            case 3:
                addStudent();
                break;
            case 4:
                deleteStudentById();
                break;
            case 5:
                addStudentToCourse();
                break;
            case 6:
                removeStudentFromCourse();
                break;
            case 7:
                isWork = false;
                break;
            default:
                viewProvider.printMessage("Incorrect command\n");
            }
        }
    }

    @Override
    public void findAllGroupsWithStudentCount() {
        viewProvider.printMessage("Enter any number:");
        int choice = viewProvider.readInt();
        List<Group> groups = null;
        
        try {
            groups = groupsDao.findAllByStudentCount(choice);
        } catch (DataBaseRuntimeException throwables) {
            throwables.printStackTrace();
        }
        viewProvider.printMessage(groups.toString()); 
    }
    
    @Override
    public void findAllStudentsToCourseName () {
        viewProvider.printMessage("Enter a course name:");
        String courseName = viewProvider.readString();    
        List<Student> students = studentsDao.findAllByCourseName(courseName);
        viewProvider.printMessage(students.toString());
    }
    
    @Override
    public void addStudent() {        
        viewProvider.printMessage("Enter student id:");
        int studentId = viewProvider.readInt(); 
        viewProvider.printMessage("Enter group id:");
        int groupId = viewProvider.readInt();  
        viewProvider.printMessage("Enter student's first name:");
        String firstName = viewProvider.readString();   
        viewProvider.printMessage("Enter student's last name:");
        String lastName = viewProvider.readString();  
   
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
                
                try {
                    studentsDao.save(student);
                } catch (DataBaseRuntimeException throwables) {
                    throwables.printStackTrace();
                }
        viewProvider.printMessage("New student added");
    }
    
    @Override
    public void deleteStudentById() {
        viewProvider.printMessage("Enter student id:");
        int studentId = viewProvider.readInt();
        studentsDao.deleteById(studentId);
        viewProvider.printMessage("Student deleted");
    }
    
    @Override
    public void addStudentToCourse() {      
        viewProvider.printMessage("Enter student id:");
        int studentId = viewProvider.readInt();     
        viewProvider.printMessage("Enter course id:");
        int courseId = viewProvider.readInt();
        
        try {
            studentsDao.addToTheCourse(studentId, courseId);
        } catch (DataBaseRuntimeException throwables) {
            throwables.printStackTrace();
        }
        viewProvider.printMessage("Student added to the course");
    }
    
    @Override
    public void removeStudentFromCourse() {        
        viewProvider.printMessage("Enter student id:");
        int studentId = viewProvider.readInt(); 
        viewProvider.printMessage("Enter course id:");
        int courseId = viewProvider.readInt();
        
        try {
            studentsDao.removeFromCourse(studentId, courseId);
        } catch (DataBaseRuntimeException throwables) {
            throwables.printStackTrace();
        }
        viewProvider.printMessage("Student remove from course");
    }
}
