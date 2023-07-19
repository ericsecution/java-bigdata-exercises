package com.myapp.models;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "age")
    private int age;

    @Column(name = "email")
    private String email;

    @Column(name = "course")
    private String course;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "registrations",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    //////////////////////////////////////////////////
    // Hibernate requires a no-argument constructor:
    public Student() {
    }
    //////////////////////////////////////////////////
    public Student(int id, String fullName, int age, String email, String course) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.course = course;
    }

    public Student(String fullName, int age, String email, String course) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.course = course;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() { return course; }

    public void setCourse(String course) {this.course = course;}


    public Set<Course> getCourses() {

        return courses;
    }

    public void setCourses(Set<Course> courses) {

        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", full_name='" + fullName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                '}';
    }

}
