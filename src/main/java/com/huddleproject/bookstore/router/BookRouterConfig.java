package com.huddleproject.bookstore.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.huddleproject.bookstore.handler.BookHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookRouterConfig {

  @Autowired
  private BookHandler handler;

  @Bean
  RouterFunction<ServerResponse> bookRoutes() {
    return route()
        .GET("/books/all", handler::findAllBooks)
        .GET("/books/{id}", handler::findBook)
        .build();
  }
}
