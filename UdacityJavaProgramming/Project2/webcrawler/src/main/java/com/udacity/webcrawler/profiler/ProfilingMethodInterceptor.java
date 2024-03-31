package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final ProfilingState profilingState;
  private final Object object;
  private ZonedDateTime startTime;


  // TODO: You will need to add more instance fields and constructor arguments to this class.
  public ProfilingMethodInterceptor(Clock clock, ProfilingState profilingState, Object object, ZonedDateTime startTime) {
    this.clock = clock;
    this.profilingState = profilingState;
    this.object = object;
    this.startTime = startTime;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object invokeObject = new Object();

    if (!method.isAnnotationPresent(Profiled.class)) {
      System.out.println("error");
    } else {
      startTime = ZonedDateTime.now(clock);
    }
    try {
      invokeObject = method.invoke(object, args);
      
    } catch (IllegalAccessException illegalException) {
      illegalException.printStackTrace();
      throw illegalException.getCause();
    } catch (InvocationTargetException invocationException) {
      invocationException.printStackTrace();
      throw invocationException.getTargetException();
    } finally {
      if (!method.isAnnotationPresent(Profiled.class)) {
        System.out.println("error");
      } else {
        Duration duration = Duration.between(startTime, ZonedDateTime.now(clock));
        profilingState.record(object.getClass(), method, duration);
      }
    }

    return invokeObject;
  }
}
