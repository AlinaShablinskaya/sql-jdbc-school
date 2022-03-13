package ua.com.foxminded.school.menu;

public interface MenuExecutor {
    void startMenu();
    
    void findAllGroupsWithStudentCount();
    
    void findAllStudentsToCourseName ();
    
    void addStudent();
    
    void deleteStudentById();
    
    void addStudentToCourse();
    
    void removeStudentFromCourse();
}
