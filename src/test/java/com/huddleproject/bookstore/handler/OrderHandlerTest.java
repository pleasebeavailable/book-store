package com.huddleproject.bookstore.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huddleproject.bookstore.mapper.Mapper;
import com.huddleproject.bookstore.model.Order;
import com.huddleproject.bookstore.util.UtilsClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
class OrderHandlerTest {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  void executeOrder() {
    String ordersJsonArrayAsString = UtilsClass.readJsonFromClasspath("mock_data/orders.json");
    Flux<Order> orders = getOrderFlux(ordersJsonArrayAsString);

    Flux<Order> executedOrder = webTestClient.post()
        .uri("/order/execute")
        .body(orders.next(), Order.class)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isCreated()
        .returnResult(Order.class).getResponseBody()
        .log();

    StepVerifier.create(executedOrder)
        .expectNextCount(1)
        .verifyComplete();
  }

  private Flux<Order> getOrderFlux(final String booksJsonArrayAsString) {
    return Mapper.mapJsonToListOfObjects(booksJsonArrayAsString, new TypeReference<List<Order>>() {
    });
  }
}
