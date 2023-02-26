package com.huddleproject.bookstore.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.huddleproject.bookstore.handler.OrderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OrderRouterConfig {

  @Autowired
  private OrderHandler handler;

  @Bean
  RouterFunction<ServerResponse> orderRoutes() {
    return route()
        .POST("/order/execute", handler::executeOrder)
        .build();
  }

}
