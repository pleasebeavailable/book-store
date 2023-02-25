package com.huddleproject.bookstore.controller;

import com.huddleproject.bookstore.model.Book;
import com.huddleproject.bookstore.service.book_service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("books")
public class BooksController {
  @Autowired BookService bookServiceImpl;

  @GetMapping("/list-of-books")
  public Flux<Book> listOfBooks() throws IOException {

    return bookServiceImpl.findAllBooks();
  }
}
