package com.huddleproject.bookstore.handler;

import com.huddleproject.bookstore.model.Book;
import com.huddleproject.bookstore.service.book_service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookHandler {
  @Autowired
  BookService bookServiceImpl;

  public Mono<ServerResponse> findAllBooks(ServerRequest request) {
    Flux<Book> bookFlux = bookServiceImpl.findAllBooks();
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(bookFlux, Book.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> findBook(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));

    Mono<Book> bookMono = bookServiceImpl.findBook(id);
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(bookMono, Book.class);
  }
}
