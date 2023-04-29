package ru.averkiev.libraryboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.averkiev.libraryboot.models.Book;
import ru.averkiev.libraryboot.models.Person;
import ru.averkiev.libraryboot.repositories.BooksRepository;
import ru.averkiev.libraryboot.repositories.PeopleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private static final int RENT_TIME = 1000 * 3600 * 24 * 10;  // 10 дней
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int itemsPerPage) {
        return booksRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public List<Book> findAll(String nameSort) {
        return booksRepository.findAll(Sort.by(nameSort));
    }

    public List<Book> findAll(int page, int itemsPerPage, String nameSort) {
        return booksRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by(nameSort))).getContent();
    }

    public Book findById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        Book book = booksRepository.findById(id).get();

        updateBook.setId(id);
        updateBook.setAbonent(book.getAbonent());

        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public List<Book> findByAbonent(Person abonent) {
        List<Book> books = booksRepository.findByAbonent(abonent);
        for (Book book : books) {
            if (book.getReceivedIn() != null && new Date().getTime() - book.getReceivedIn().getTime() < RENT_TIME) {
                System.out.println("книга не просрочена");
                book.setOverdue(false);
            }
            if (book.getReceivedIn() != null && new Date().getTime() - book.getReceivedIn().getTime() > RENT_TIME) {
                System.out.println("книга просрочена");
                book.setOverdue(true);
            }
        }
        return books;
    }

    @Transactional
    public void addBook(Person abonent, int bookId) {

        Book book = this.findById(bookId);

        book.setReceivedIn(new Date());
        abonent.setBooks(new ArrayList<>(Collections.singletonList(book)));
        book.setAbonent(abonent);

        booksRepository.save(book);
    }

    @Transactional
    public void rmBook(int bookId) {

        Book book = this.findById(bookId);
        Person abonent = book.getAbonent();
        book.setAbonent(null);
        book.setReceivedIn(null);

        abonent.getBooks().remove(book);

        booksRepository.save(book);
    }

    public List<Book> findByTitleStartWith(String startTitle) {
        return booksRepository.findByTitleStartingWith(startTitle);
    }
}
