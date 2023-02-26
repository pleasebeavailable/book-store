package com.huddleproject.bookstore.service.book_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huddleproject.bookstore.mapper.Mapper;
import com.huddleproject.bookstore.model.Book;
import com.huddleproject.bookstore.util.UtilsClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImpl implements BookService {

  @Value("${mock_data.books}")
  private String books_path;

  public Flux<Book> findAllBooks() {
    String booksJsonArrayAsString = UtilsClass.readJsonFromClasspath(books_path);

    return getBookFlux(booksJsonArrayAsString);
  }

  @Override
  public Mono<Book> findABook(final Long id) {
    String booksJsonArrayAsString = UtilsClass.readJsonFromClasspath(books_path);

    return getBookFlux(booksJsonArrayAsString).filter((Book book) -> Objects.equals(book.getId(), id)).next();
  }

  private Flux<Book> getBookFlux(final String booksJsonArrayAsString) {
    return Mapper.mapJsonToListOfObjects(booksJsonArrayAsString, new TypeReference<List<Book>>() {
    });
  }

}
