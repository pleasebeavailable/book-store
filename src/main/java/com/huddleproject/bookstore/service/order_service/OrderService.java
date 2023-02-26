package com.huddleproject.bookstore.service.order_service;

import com.huddleproject.bookstore.model.Order;
import reactor.core.publisher.Mono;

public interface OrderService {

  Mono<Order> executeOrder(Order order);
}
