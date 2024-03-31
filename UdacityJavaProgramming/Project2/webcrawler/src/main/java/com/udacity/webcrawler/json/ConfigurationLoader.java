package com.udacity.webcrawler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A static utility class that loads a JSON configuration file.
 */
public final class ConfigurationLoader {

  private final Path path;

  /**
   * Create a {@link ConfigurationLoader} that loads configuration from the given {@link Path}.
   */
  public ConfigurationLoader(Path path) {
    this.path = Objects.requireNonNull(path);
  }

  /**
   * Loads configuration from this {@link ConfigurationLoader}'s path
   *
   * @return the loaded {@link CrawlerConfiguration}.
   */
  public CrawlerConfiguration load() {
    try (BufferedReader buffReader = Files.newBufferedReader(path)) {
      System.out.println("start reder");
      return read(buffReader);
    } catch (IOException oiException) {
      System.out.println(oiException.getMessage());
      System.out.println("please follow message");
    } catch (Exception exception) {
      System.out.println("please follow message");
      exception.printStackTrace();
    }finally {
      System.out.println("end workflow");
    }

    return new CrawlerConfiguration
            .Builder()
            .
            build();
  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */
  public static CrawlerConfiguration read(Reader reader) {

    try{
      Objects.requireNonNull(reader);
      ObjectMapper objMapper;
      objMapper = new ObjectMapper();
      objMapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
      // return value
      return objMapper
              .readValue(reader, CrawlerConfiguration.class);
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }

    return new CrawlerConfiguration.Builder().build();
  }
}
