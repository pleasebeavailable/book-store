package com.huddleproject.bookstore.handler;

import com.huddleproject.bookstore.model.User;
import com.huddleproject.bookstore.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class UserHandler {
  @Autowired
  UserService userServiceImpl;

  public Mono<ServerResponse> findUser(ServerRequest request) {
    String username = request.pathVariable("username");

    Mono<User> userMono = userServiceImpl.findUser(username);
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userMono, User.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

}
