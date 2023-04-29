package ru.averkiev.libraryboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.averkiev.libraryboot.models.Book;
import ru.averkiev.libraryboot.models.Person;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByAbonent(Person abonent);
    List<Book> findByTitleStartingWith(String startTitle);
}