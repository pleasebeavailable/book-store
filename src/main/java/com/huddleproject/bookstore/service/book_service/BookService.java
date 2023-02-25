package com.huddleproject.bookstore.service.book_service;

import com.huddleproject.bookstore.model.Book;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface BookService {

  Flux<Book> findAllBooks() throws IOException;
}
