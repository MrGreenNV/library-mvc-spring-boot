package ru.averkiev.libraryboot.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.averkiev.libraryboot.models.Book;
import ru.averkiev.libraryboot.models.Person;
import ru.averkiev.libraryboot.services.BooksService;
import ru.averkiev.libraryboot.services.PeopleService;
import ru.averkiev.libraryboot.util.BookValidator;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) String isSort, Model model) {

        if (page != null && booksPerPage !=null && (isSort != null && isSort.equals("true"))) {
            model.addAttribute("books", booksService.findAll(page, booksPerPage, "yearOfRealize"));
            return "books/index";
        }
        if (page != null && booksPerPage !=null && isSort == null) {
            model.addAttribute("books", booksService.findAll(page, booksPerPage));
            return "books/index";
        }
        if ((page == null || booksPerPage == null) && (isSort != null && isSort.equals("true"))) {
            model.addAttribute("books", booksService.findAll("yearOfRealize"));
            return "books/index";
        }
        System.out.println("4");
        model.addAttribute("books", booksService.findAll());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute("person") Person person, @PathVariable("id") int id, Model model) {

        Book book = booksService.findById(id);
        Person abonent = book.getAbonent();

        model.addAttribute("book", book);

        if (abonent == null) {
            model.addAttribute("people", peopleService.findAll());
        } else {
            model.addAttribute("abonent", abonent);
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if(bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add")
    public String addBook(@ModelAttribute("person") Person person, @PathVariable("id") int bookId) {
        booksService.addBook(person, bookId);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/rm")
    public String rmBook(@PathVariable("id") int bookId) {
        booksService.rmBook(bookId);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("foundBooks", booksService.findByTitleStartWith(query));
        return "books/search";
    }
}