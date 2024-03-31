package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

/**
 * Concrete implementation of the {@link Profiler}.
 */
final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state = new ProfilingState();
  private final ZonedDateTime startTime;

  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
  }

  @Override
  public <T> T wrap(Class<T> klass, T delegate) {
    Objects.requireNonNull(klass);

    if (klass.getDeclaredMethods().length == 0)
      throw new IllegalArgumentException("No annotation");

    Stream<Method> sochim = Arrays.stream(klass.getDeclaredMethods());
    Boolean isTrue = sochim
            .noneMatch(method -> method.getAnnotation(Profiled.class) != null);
    if (isTrue)
      throw new IllegalArgumentException("No annotation");

    ProfilingMethodInterceptor interceptor = new ProfilingMethodInterceptor(clock, state, delegate, startTime);
    ClassLoader loader = ProfilerImpl.class.getClassLoader();
    Class[] arrClass  = new Class[] {klass};

    return (T) Proxy.newProxyInstance(loader, arrClass,
            interceptor);
  }

  @Override
  public void writeData(Path path) {
    if (path == null) {
    }
    else {
      try (BufferedWriter buffWriter = Files.newBufferedWriter(path)) {
        writeData(buffWriter);
      }catch (NullPointerException nullPointerException){
        nullPointerException.printStackTrace();
      }catch (Exception e) {
        e.printStackTrace();
      }finally {
        System.out.println("this is the end of code");
      }
    }
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
    writer.write(System.lineSeparator());
    state.write(writer);
    writer.write(System.lineSeparator());
  }
}
