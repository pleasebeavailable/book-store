package com.huddleproject.bookstore.service.book_service;

import com.huddleproject.bookstore.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {

  Flux<Book> findAllBooks();

  Mono<Book> findBook(Long id);
}
