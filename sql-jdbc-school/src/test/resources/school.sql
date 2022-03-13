CREATE SCHEMA IF NOT EXISTS SCHOOL;

DROP TABLE IF EXISTS school.groups CASCADE;
DROP TABLE IF EXISTS school.students CASCADE;
DROP TABLE IF EXISTS school.courses CASCADE;
DROP TABLE IF EXISTS school.students_courses CASCADE;

CREATE TABLE school.groups
(
    group_id   INT UNIQUE NOT NULL ,
    group_name VARCHAR    NOT NULL,
    PRIMARY KEY (group_id)
);

CREATE TABLE school.students
(
student_id        BIGINT UNIQUE NOT NULL,
group_id          INT,
first_name VARCHAR    NOT NULL,
last_name VARCHAR    NOT NULL,
PRIMARY KEY (student_id),
FOREIGN KEY (group_id) REFERENCES school.groups ON DELETE SET NULL
);

CREATE TABLE school.courses
(
course_id INT UNIQUE NOT NULL,
course_name VARCHAR    NOT NULL,
course_description VARCHAR    NOT NULL,
PRIMARY KEY (course_id)
);

CREATE TABLE school.students_courses
(
student_id BIGINT,
course_id BIGINT,
FOREIGN KEY (student_id) REFERENCES school.students ON DELETE CASCADE,
FOREIGN KEY (course_id) REFERENCES school.courses ON DELETE CASCADE
);
