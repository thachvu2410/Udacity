package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;

/**
 * A concrete implementation of {@link WebCrawler} that runs multiple threads on a
 * {@link ForkJoinPool} to fetch and process multiple web pages in parallel.
 */
final class ParallelWebCrawler implements WebCrawler {
  private final Clock clock;
  private final Duration timeout;
  private final int popularWordCount;
  private final ForkJoinPool pool;
  private final int maxDepth;
  @Inject
  private PageParserFactory parserFactory;

  private final List<Pattern> ignoredUrls;
  @Inject
  ParallelWebCrawler(
      Clock clock,
      @Timeout Duration timeout,
      @PopularWordCount int popularWordCount,
      @TargetParallelism int threadCount,@MaxDepth int maxDepth,
      @IgnoredUrls List<Pattern> ignoredUrls) {
    this.clock = clock;
    this.timeout = timeout;
    this.ignoredUrls = ignoredUrls;
    this.popularWordCount = popularWordCount;
    this.pool = new ForkJoinPool(Math.min(threadCount, getMaxParallelism()));
    this.maxDepth = maxDepth;
  }

  @Override
  public CrawlResult crawl(List<String> startingUrls) {

    Instant deadline = clock.instant().plus(timeout);
    Map<String, Integer> counts = new HashMap<>();
    Set<String> visitedUrls = new HashSet<>();


    Collections.synchronizedCollection(startingUrls).forEach(url -> {
      ICrawl crawl = new ICrawl(clock, pool, parserFactory, ignoredUrls, url, counts, visitedUrls, deadline, maxDepth);
      pool.invoke(crawl);
    });

    if (!counts.isEmpty()) {
      return new CrawlResult.Builder()
              .
              setUrlsVisited(visitedUrls.size())

              .setWordCounts(WordCounts.sort(counts, popularWordCount))
              .build();
    }else {
      return new CrawlResult.Builder()
              .setUrlsVisited(visitedUrls.size())

              .
              setWordCounts(counts)
              .build();
    }


  }

  @Override
  public int getMaxParallelism() {
    return Runtime.getRuntime().availableProcessors();
  }
}
