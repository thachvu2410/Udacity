package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A {@link WebCrawler} that downloads and processes one page at a time.
 */
final class SequentialWebCrawler implements WebCrawler {

  private final List<Pattern> ignoredUrls;
  private final Clock clock;
  private final PageParserFactory parserFactory;
  private final int popularWordCount;
  private final int maxDepth;
  private final Duration timeout;


  @Inject
  SequentialWebCrawler(
      Clock clock,
      @MaxDepth int maxDepth,
      @PopularWordCount int popularWordCount,
      PageParserFactory parserFactory,
      @Timeout Duration timeout,
      @IgnoredUrls List<Pattern> ignoredUrls) {
    this.parserFactory = parserFactory;
    this.clock = clock;
    this.maxDepth = maxDepth;
    this.timeout = timeout;
    this.popularWordCount = popularWordCount;
    this.ignoredUrls = ignoredUrls;
  }

  @Override
  public CrawlResult crawl(List<String> startingUrls) {
    Instant deadline = clock.instant().plus(timeout);
    Map<String, Integer> counts = new HashMap<>();
    Set<String> visitedUrls = new HashSet<>();
    for (String url : startingUrls) {
      crawlInternal(url, deadline, maxDepth, counts, visitedUrls);
    }

    if (counts.isEmpty()) {
      return new CrawlResult.Builder()
          .setWordCounts(counts)
          .setUrlsVisited(visitedUrls.size())
          .build();
    }

    return new CrawlResult.Builder()
        .setWordCounts(WordCounts.sort(counts, popularWordCount))
        .setUrlsVisited(visitedUrls.size())
        .build();
  }

  private void crawlInternal(
      String url,
      Instant deadline,
      int maxDepth,
      Map<String, Integer> counts,
      Set<String> visitedUrls) {
    if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
      return;
    }
    for (Pattern pattern : ignoredUrls) {
      if (pattern.matcher(url).matches()) {
        return;
      }
    }
    if (visitedUrls.contains(url)) {
      return;
    }
    visitedUrls.add(url);
    PageParser.Result result = parserFactory.get(url).parse();
    for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
      if (counts.containsKey(e.getKey())) {
        counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
      } else {
        counts.put(e.getKey(), e.getValue());
      }
    }
    for (String link : result.getLinks()) {
      crawlInternal(link, deadline, maxDepth - 1, counts, visitedUrls);
    }
  }
}
