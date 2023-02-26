package com.huddleproject.bookstore.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public final class UtilsClass {
  private static final Logger LOGGER = LoggerFactory.getLogger(UtilsClass.class);

  public static String readJsonFromClasspath(String path) {
    final ClassPathResource jsonResource = new ClassPathResource(path);
    InputStream inputStream = null;
    String json;
    try {
      if (jsonResource.exists()) {
        inputStream = jsonResource.getInputStream();
        json = IOUtils.toString(inputStream);
      } else {
        json = "";
      }
    } catch (IOException e) {
      LOGGER.error("Error reading product from mocked JSON! Error: " + e.getLocalizedMessage());
      json = "";
    } finally {
      safeClose(inputStream);
    }

    return json;
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
