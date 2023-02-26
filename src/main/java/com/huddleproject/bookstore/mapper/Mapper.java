package com.huddleproject.bookstore.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);

  public static <T> Flux<T> mapJsonToListOfObjects(String jsonArrayAsString, TypeReference<List<T>> valueTypeRef) {
    ObjectMapper objectMapper = new ObjectMapper();
    List<T> mappedList = new ArrayList<>();
    try {
      mappedList = objectMapper.readValue(jsonArrayAsString, valueTypeRef);
    } catch (
        IOException e) {
      LOGGER.error("Error mapping books from JSON string! Error: " + e.getLocalizedMessage());
    }

    return Flux.fromIterable(mappedList);
  }

}
