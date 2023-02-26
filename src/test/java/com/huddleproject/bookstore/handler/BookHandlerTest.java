package com.huddleproject.bookstore.handler;

import com.huddleproject.bookstore.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
class BookHandlerTest {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void findAllBooks() {
    Flux<Book> books = webTestClient.get()
        .uri("/books/all")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Book.class).getResponseBody()
        .log();

    StepVerifier.create(books)
        .expectNextCount(9)
        .verifyComplete();
  }

  @Test
  public void findBook() {
    Mono<Book> books = webTestClient.get()
        .uri("/books/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Book.class).getResponseBody()
        .next()
        .log();

    StepVerifier.create(books)
        .expectNextCount(1)
        .verifyComplete();
  }
}
