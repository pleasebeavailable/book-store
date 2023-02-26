package com.huddleproject.bookstore.service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huddleproject.bookstore.mapper.Mapper;
import com.huddleproject.bookstore.model.User;
import com.huddleproject.bookstore.util.UtilsClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Value("${mock_data.users}")
  private String users_path;

  @Override
  public Mono<User> findUser(final String username) {
    String usersJsonArrayAsString = UtilsClass.readJsonFromClasspath(users_path);

    return getUserFlux(usersJsonArrayAsString).filter((User user) -> user.getUsername().equals(username)).next();
  }

  /**
   * This method only returns user because there is no database
   */
  @Override public Mono<User> saveUser(final User user) {
    try {
      LOGGER.info("Save user!");

    } catch (Exception e) {
      LOGGER.error("Error on save user: " + e.getLocalizedMessage());
    }
    return Mono.just(user);
  }

  @Override
  public Mono<User> calculateAndSaveLoyaltyPoints(final String username, final boolean isAward) {

    return findUser(username)
        .doOnNext((User user) -> LOGGER.info("Before change: " + user.getLoyaltyPoints()))
        .map((User user) -> addLoyaltyAndReturnUser(user, isAward))
        .doOnNext((User user) -> LOGGER.info("Loyalty changed: " + user.getLoyaltyPoints()))
        .flatMap(this::saveUser);
  }

  private Flux<User> getUserFlux(final String usersJsonArrayAsString) {
    return Mapper.mapJsonToListOfObjects(usersJsonArrayAsString, new TypeReference<List<User>>() {
    });
  }

  private User addLoyaltyAndReturnUser(User user, final boolean isAward) {
    return User.builder()
        .id(user.getId())
        .username(user.getUsername())
        .loyaltyPoints(user.getLoyaltyPoints() + (isAward ? 1 : -10))
        .build();
  }
}
