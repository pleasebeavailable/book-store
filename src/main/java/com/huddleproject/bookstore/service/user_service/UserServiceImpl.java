package com.huddleproject.bookstore.service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huddleproject.bookstore.mapper.Mapper;
import com.huddleproject.bookstore.model.Order;
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

  /**
   * This method finds specific user using unique username
   */
  @Override
  public Mono<User> findUser(final String username) {
    String usersJsonArrayAsString = UtilsClass.readJsonFromClasspath(users_path);

    return getUserFlux(usersJsonArrayAsString)
        .filter((User user) -> user.getUsername().equals(username))
        .next();
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

  /**
   * This method finds user and calculates new loyalty points
   * For each bought book +1 award point
   * If loyalty is used then set to 0
   */
  @Override
  public Mono<User> calculateAndSaveLoyaltyPoints(final Order order) {
    LOGGER.info("Calculate loyalty points for user: " + order.getUsername());

    return findUser(order.getUsername())
        .log(printLoyaltyDescription(order))
        .map((User user) -> User.builder()
            .id(user.getId())
            .username(user.getUsername())
            .loyaltyPoints(user.getLoyaltyPoints() + (order.isWithLoyaltyPoints() ? order.getOrderedBooks().size() : 0))
            .build()
        );
  }

  private String printLoyaltyDescription(final Order order) {
    if ((order.isWithLoyaltyPoints())) {LOGGER.info(order.getUsername() + " spent loyalty points! Set to 0!");} else {
      LOGGER.info(order.getUsername() + " is awarded: " + order.getOrderedBooks().size() + " award points!");
    }
    return "";
  }

  /**
   * Helper method for mapping json to User object
   */
  private Flux<User> getUserFlux(final String usersJsonArrayAsString) {
    return Mapper.mapJsonToListOfObjects(usersJsonArrayAsString, new TypeReference<List<User>>() {
    });
  }
}
