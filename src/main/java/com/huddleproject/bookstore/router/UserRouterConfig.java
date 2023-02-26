package com.huddleproject.bookstore.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.huddleproject.bookstore.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouterConfig {

  @Autowired
  private UserHandler handler;

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route()
        .GET("/user/{username}", handler::findUser)
        .build();
  }
}
