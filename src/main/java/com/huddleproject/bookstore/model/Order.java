package com.huddleproject.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  private Long id;
  private List<Book> orderedBooks;
  private BigDecimal basePrice;
  private String username;
  private boolean withLoyaltyPoints;
}
