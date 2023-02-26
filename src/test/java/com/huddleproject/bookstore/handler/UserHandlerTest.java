package com.huddleproject.bookstore.handler;

import com.huddleproject.bookstore.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
class UserHandlerTest {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void findUser() {
    Mono<User> user = webTestClient.get()
        .uri("/user/username1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(User.class).getResponseBody()
        .next()
        .log();

    StepVerifier.create(user)
        .expectNextCount(1)
        .verifyComplete();
  }

}
