package ru.averkiev.libraryboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp = "([A-Z][a-z]+|[А-Я][а-я]+) ([A-Z][a-z]+|[А-Я][а-я]+) ([A-Z][a-z]+|[А-Я][а-я]+)", message = "The FullName should be like this: Lastname Firstname Patronymic")
    @Size(min = 8, max = 100, message = "The Fullname should be fuller")
    @NotEmpty(message = "The FullName not should be empty")
    @Column(name = "fullname")
    private String fullName;

    @Min(value = 1900, message = "Year should be between 1900 and 2017 character")
    @Max(value = 2017, message = "Year should be between 1900 and 2017 character")
    @Column(name = "yearofbirthday")
    private int yearOfBirthday;

    @OneToMany(mappedBy = "abonent")
    private List<Book> books;

    public Person(String fullName, int date) {
        this.fullName = fullName;
        this.yearOfBirthday = date;
    }

    public Person() {
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

    public int getYearOfBirthday() {
        return yearOfBirthday;
    }

    public void setYearOfBirthday(int yearOfBirthday) {
        this.yearOfBirthday = yearOfBirthday;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", yearOfBirthday=" + yearOfBirthday +
                ", books=" + books +
                '}';
    }
}
