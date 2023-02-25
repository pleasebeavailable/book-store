package com.huddleproject.bookstore.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class Order {
  private List<Book> orderedBooks;
  private BigDecimal basePrice;
  private boolean withLoyaltyPoints;
}
