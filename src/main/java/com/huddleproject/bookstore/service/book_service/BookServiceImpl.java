package com.huddleproject.bookstore.service.book_service;

import com.huddleproject.bookstore.model.Book;
import com.huddleproject.bookstore.utils.HelperClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BookServiceImpl implements BookService {
  @Autowired
  private HelperClass helperClass;
  private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

  public Flux<Book> findAllBooks() {
    String booksJsonArrayAsString = helperClass.readJsonFromPath("src/main/resources/mock_data/books.json");

    return helperClass.mapJsonToFlux(booksJsonArrayAsString);
  }
}
