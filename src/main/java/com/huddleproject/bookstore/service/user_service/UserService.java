package com.huddleproject.bookstore.service.user_service;

import com.huddleproject.bookstore.model.User;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> findUser(String username);

  Mono<User> saveUser(User user);

  Mono<User> calculateAndSaveLoyaltyPoints(String username, boolean isAward);

}
