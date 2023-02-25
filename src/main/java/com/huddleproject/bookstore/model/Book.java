package com.huddleproject.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  private Long id;
  private String name;
  private BigDecimal price;
  private BookType bookType;
}
