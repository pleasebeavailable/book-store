package com.huddleproject.bookstore.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class HelperClass {
  private static final Logger LOGGER = LoggerFactory.getLogger(HelperClass.class);
  ObjectMapper objectMapper = new ObjectMapper();

  public <T> Flux<T> mapJsonToFlux(String jsonArrayAsString) {
    List<T> mappedList = new ArrayList<>();
    try {
      mappedList = objectMapper.readValue(jsonArrayAsString, new TypeReference<List<T>>() {
      });
    } catch (
        IOException e) {
      LOGGER.error("Error mapping books from JSON string! Error: " + e.getLocalizedMessage());
    }

    return Flux.fromIterable(mappedList);
  }

  public String readJsonFromPath(String path) {
    File file = new File(path);
    JsonNode jsonArray = null;
    String jsonArrayAsString = null;
    try {
      if (file.exists()) {
        jsonArray = objectMapper.readValue(file, JsonNode.class);
        jsonArrayAsString = objectMapper.writeValueAsString(jsonArray);
      } else {
        jsonArrayAsString = "";
      }
    } catch (IOException e) {
      LOGGER.error("Error reading books from mocked JSON! Error: " + e.getLocalizedMessage());
      jsonArrayAsString = "";
    }

    return jsonArrayAsString;
  }

  public static void safeClose(Closeable clos) {
    if (clos != null) {
      try {
        clos.close();
      } catch (IOException e) {
        LOGGER.error(e.getMessage());
      }
    }
  }
}
