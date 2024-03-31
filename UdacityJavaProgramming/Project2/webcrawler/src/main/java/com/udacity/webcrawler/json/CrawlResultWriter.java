package com.udacity.webcrawler.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class to write a {@link CrawlResult} to file.
 */
public final class CrawlResultWriter {
  private final CrawlResult result;

  /**
   * Creates a new {@link CrawlResultWriter} that will write the given {@link CrawlResult}.
   */
  public CrawlResultWriter(CrawlResult result) {
    this.result = Objects.requireNonNull(result);
  }

  /**
   * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Path}.
   *
   * <p>If a file already exists at the path, the existing file should not be deleted; new data
   * should be appended to it.
   *
   * @param path the file path where the crawl result data should be written.
   */
  public void write(Path path) {
    // This is here to get rid of the unused variable warning.
    Objects.requireNonNull(path);
    try (BufferedWriter buffWriter = Files.newBufferedWriter(path)) {
      write(buffWriter);
    }catch (NullPointerException pointerException){
      System.out.println(pointerException.getCause());
      System.out.println("follow this message!");
    }
    catch (Exception exception) {
      System.out.println("follow this message!");
      exception.printStackTrace();
    }finally {
      System.out.println();
      System.out.println("this is the end of the code");
    }
  }

  /**
   * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Writer}.
   *
   * @param writer the destination where the crawl result data should be written.
   */
  public void write(Writer writer) {
    // This is here to get rid of the unused variable warning.

    try {
      Objects.requireNonNull(writer);
      ObjectMapper objMper;
      objMper = new ObjectMapper();
      objMper
              .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
      objMper.writeValue(writer, result);
    }  catch (NullPointerException nullPointerException){
      System.out.println("follow this message!");
      nullPointerException.printStackTrace();
    }
    catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("follow this message!");
    }
    finally {
      System.out.println("end of the flow");
    }
  }
}
