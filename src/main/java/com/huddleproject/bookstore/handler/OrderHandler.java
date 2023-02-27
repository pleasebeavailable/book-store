package com.huddleproject.bookstore.handler;

import com.huddleproject.bookstore.model.Book;
import com.huddleproject.bookstore.model.BookType;
import com.huddleproject.bookstore.model.Order;
import com.huddleproject.bookstore.service.order_service.OrderService;
import com.huddleproject.bookstore.service.user_service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class OrderHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderHandler.class);
  @Autowired
  private OrderService orderServiceImpl;
  @Autowired
  private UserService userServiceImpl;

  /**
   * executeOrder method should do logic for order execution
   * It should handle POST request since order comes from client but for this project order is mocked
   * First order is executed and if it does not fail then calculate and save loyalty points
   */
  public Mono<ServerResponse> executeOrder(ServerRequest request) {
    Mono<Order> orderMono = request.bodyToMono(Order.class);

    return orderMono
        .map(this::useLoyaltyPoints)
        .map(this::getOrderWithBasePrice)
        .flatMap((Order order) ->
            {
              Mono<Order> executedOrder = orderServiceImpl.executeOrder(order);
              userServiceImpl.calculateAndSaveLoyaltyPoints(order).map(userServiceImpl::saveUser);
              executionEndLogging(order);
              return ServerResponse
                  .status(HttpStatus.CREATED)
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(executedOrder, Order.class);
            }
        )
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  private void executionEndLogging(final Order order) {
    LOGGER.info("Order base price is: " + order.getBasePrice());
    LOGGER.info("End of execution!");
    LOGGER.info("--------------------------------------------");
  }

  /**
   * Method which sets book price to 0 if loyalty is used
   * Check if user chose to use loyalty points
   * If true then sort books from more expensive to less expensive and find first most expensive REGULAR or
   * OLD_EDITION book and set price to 0
   * Set isWithLoyaltyPoints to false to prevent all prices to go to 0
   * Set base price to 0 because calculation is done in next step
   */
  private Order useLoyaltyPoints(final Order order) {
    final AtomicBoolean isWithLoyaltyPoints = new AtomicBoolean(order.isWithLoyaltyPoints());
    List<Book> books = isWithLoyaltyPoints.get() ?
        order.getOrderedBooks().stream()
            .sorted(Comparator.comparing(Book::getPrice).reversed())
            .collect(Collectors.toList())
            .stream().map((Book book) -> {
              if (book.getBookType().equals(BookType.REGULAR) || book.getBookType().equals(BookType.OLD_EDITION)) {
                if (isWithLoyaltyPoints.get()) {
                  isWithLoyaltyPoints.set(false);
                  return Book.builder()
                      .id(book.getId())
                      .name(book.getName())
                      .bookType(book.getBookType())
                      .price(BigDecimal.ZERO)
                      .build();
                }
              }
              return book;
            }).collect(Collectors.toList()) : order.getOrderedBooks();

    return Order.builder()
        .username(order.getUsername())
        .withLoyaltyPoints(order.isWithLoyaltyPoints())
        .orderedBooks(order.isWithLoyaltyPoints() ? books : order.getOrderedBooks())
        .basePrice(BigDecimal.ZERO)
        .build();
  }

  /**
   * Order builder which uses calculateBasePrice for base price calculation
   */
  private Order getOrderWithBasePrice(final Order order) {
    return Order.builder()
        .username(order.getUsername())
        .orderedBooks(order.getOrderedBooks())
        .withLoyaltyPoints(order.isWithLoyaltyPoints())
        .basePrice(calculateBasePrice(order))
        .build();
  }

  /**
   * Base price calculation method
   * Set isBundle to true if 3 or more books are orderd
   * Go through each book, check book type and set price following given rules
   * In the end use reduce operator to get final value
   */
  private BigDecimal calculateBasePrice(Order order) {
    List<Book> books = order.getOrderedBooks();
    boolean isBundle = books.size() >= 3;

    return books.stream()
        .map((Book book) ->
            book.getBookType().equals(BookType.OLD_EDITION) ?
                isBundle ?
                    book.getPrice().multiply(BigDecimal.valueOf(0.75)) :
                    book.getPrice().multiply(BigDecimal.valueOf(0.8)) :
                (book.getBookType().equals(BookType.REGULAR) && isBundle) ?
                    book.getPrice().multiply(BigDecimal.valueOf(0.9)) :
                    book.getPrice()
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

  }

}
