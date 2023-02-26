package com.huddleproject.bookstore.handler;

import com.huddleproject.bookstore.model.Book;
import com.huddleproject.bookstore.model.BookType;
import com.huddleproject.bookstore.model.Order;
import com.huddleproject.bookstore.service.order_service.OrderService;
import com.huddleproject.bookstore.service.user_service.UserService;
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
import java.util.stream.Collectors;

@Service
public class OrderHandler {
  @Autowired
  private OrderService orderServiceImpl;
  @Autowired
  private UserService userServiceImpl;

  public Mono<ServerResponse> executeOrder(ServerRequest request) {
    Mono<Order> orderMono = request.bodyToMono(Order.class);
    return orderMono
        .map(this::useLoyaltyPoints)
        .map(this::getOrderWithBasePrice)
        .flatMap((Order order) ->
            {
              Mono<Order> executedOrder = orderServiceImpl.executeOrder(order);
              userServiceImpl.calculateAndSaveLoyaltyPoints(order.getUsername(), order.isWithLoyaltyPoints());
              return ServerResponse
                  .status(HttpStatus.CREATED)
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(executedOrder, Order.class);
            }
        )
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  private Order useLoyaltyPoints(final Order order) {
    List<Book> books =
        order.getOrderedBooks().stream()
            .sorted(Comparator.comparing(Book::getPrice).reversed())
            .collect(Collectors.toList())
            .stream().map((Book book) ->
                book.getBookType().equals(BookType.REGULAR) || book.getBookType().equals(BookType.OLD_EDITION) ?
                    Book.builder()
                        .id(book.getId())
                        .name(book.getName())
                        .bookType(book.getBookType())
                        .price(order.isWithLoyaltyPoints() ? BigDecimal.ZERO : book.getPrice())
                        .build()
                    : book)
            .collect(Collectors.toList());

    return Order.builder()
        .username(order.getUsername())
        .withLoyaltyPoints(order.isWithLoyaltyPoints())
        .orderedBooks(order.isWithLoyaltyPoints() ? books : order.getOrderedBooks())
        .basePrice(BigDecimal.ZERO)
        .build();
  }

  private Order getOrderWithBasePrice(final Order order) {
    return Order.builder()
        .username(order.getUsername())
        .orderedBooks(order.getOrderedBooks())
        .withLoyaltyPoints(order.isWithLoyaltyPoints())
        .basePrice(calculateBasePrice(order))
        .build();
  }

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
