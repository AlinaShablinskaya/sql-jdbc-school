package ua.com.foxminded.school.generate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;

import static java.util.stream.Collectors.toList;

public class DBGeneratorImpl implements DBGenerator {
    private static final Logger LOGGER = LogManager.getLogger(DBGeneratorImpl.class);
    private static final String DASH = "_";
    private static final int FIRST_LETTER_OF_THE_ALPHABET = 65;
    private static final int LAST_LETTER_OF_THE_ALPHABET = 91;
    private static final int FIRST_NUMBERS = 10;
    private static final int LAST_NUMBERS = 99;
    private static final int MAX_GROUP = 10;
    private static final int MAX_STUDENT = 200;

    private final String coursesFile;
    private final String firstNameFile;
    private final String lastNameFile;

    public DBGeneratorImpl(String coursesFile, String firstNameFile, String lastNameFile) {
        this.coursesFile = coursesFile;
        this.firstNameFile = firstNameFile;
        this.lastNameFile = lastNameFile;
    }

    @Override
    public List<Group> generateGroups() {
        List<Group> groups = new ArrayList<>();

        for (int i = 1; i <= MAX_GROUP; i++) {
            StringBuilder builder = new StringBuilder();
            Random random = new Random();

            String generateCharacters = random.ints(FIRST_LETTER_OF_THE_ALPHABET, LAST_LETTER_OF_THE_ALPHABET).limit(2)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

            int generateNumber = random.ints(FIRST_NUMBERS, LAST_NUMBERS).findFirst().orElseThrow(IllegalStateException::new);

            builder.append(generateCharacters).append(DASH).append(generateNumber);
            
            groups.add(new Group.Builder()
                    .withGroupId(i)
                    .withGroupName(builder.toString())
                    .build());
        }
        return groups;
    }

    @Override
    public List<Course> generateCourses() {
        List<String> readCourses = readFromFile(coursesFile);

        return readCourses.stream().map(course -> {
            String[] splitLine = course.split(DASH);
            int courseID = Integer.parseInt(splitLine[0]);
            String courseName = splitLine[1];
            String courseDescription = splitLine[2];
            
            return new Course.Builder().withCourseId(courseID).withCourseName(courseName)
                    .withCourseDescription(courseDescription).build();
        }).collect(toList()); 
    }

    @Override
    public List<Student> generateStudents(List<Group> groups, List<Course> course) {
        List<Student> students = new ArrayList<>();

        for (int i = 1; i <= MAX_STUDENT; i++) {
            students.add(Student.builder()
                    .withStudentId(i)
                    .withGroupId(randomGroupNumber())
                    .withFirstName(readFromFile(firstNameFile).get(nameRandom()))
                    .withLastName(readFromFile(lastNameFile).get(nameRandom()))
                    .withCourses(courseRandom(course))
                    .build());
        }
        return students;
    }

    private List<Course> courseRandom(List<Course> courses) {
        List<Course> result = new ArrayList<>();
        int randomCount = (int) (Math.random() * 3) + 1;

        for (int i = 1; i <= randomCount; i++) {
            int randomCourses = randomGroupNumber() - 1;
            if (!result.contains(courses.get(randomCourses))) {
                result.add(courses.get(randomCourses));
            }
        }
        return result;
    }

    private Integer randomGroupNumber() {
        return (int) (Math.random() * 10) + 1;
    }

    private Integer nameRandom() {
        return (int) (Math.random() * 20);
    }

    private List<String> readFromFile(String fileAddress) {
        List<String> readFromFile = new ArrayList<>();

        try {
            readFromFile = Files.lines(Paths.get(fileAddress)).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        return readFromFile;
    }
}
