package com.huddleproject.bookstore.service.order_service;

import com.huddleproject.bookstore.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

  /**
   * This method only returns user because there is no database
   */
  @Override
  public Mono<Order> executeOrder(final Order order) {
    try {
      LOGGER.info("Execute order!");

    } catch (Exception e) {
      LOGGER.error("Error on save user: " + e.getLocalizedMessage());
    }
    return Mono.just(order);
  }
}
