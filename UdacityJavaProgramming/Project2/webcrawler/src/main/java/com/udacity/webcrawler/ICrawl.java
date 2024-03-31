package com.udacity.webcrawler;

import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class ICrawl extends RecursiveAction {
    private final Clock clock;
    private Map<String, Integer> countsMap;
    private Set<String> visitedUrlsSet;
    private int maxDepth;
    private final ForkJoinPool pool;
    private PageParserFactory parserFactory;
    private String url;
    private Instant deadLine;
    private final List<Pattern> ignoredUrls;


    @Inject
    public ICrawl(Clock clock, ForkJoinPool pool, PageParserFactory parserFactory, List<Pattern> ignoredUrls, String url, Map<String, Integer> countsMap, Set<String> visitedUrlsSet, Instant deadLine, int maxDepth) {
        this.clock = clock;
        this.visitedUrlsSet = visitedUrlsSet;
        this.parserFactory = parserFactory;
        this.url = url;
        this.countsMap = countsMap;
        this.ignoredUrls = ignoredUrls;
        this.pool = pool;
        this.deadLine = deadLine;
        this.maxDepth = maxDepth;
    }

    @Override
    protected void compute() {

        if (clock.instant().isAfter(deadLine)) {
            return;
        }
        if (maxDepth == 0) {
            return;
        }

        if (!ignoredUrls.stream().noneMatch(ignoredUrl -> ignoredUrl.matcher(url).matches())) {
            return;
        }

        synchronized (this) {
            if (visitedUrlsSet.contains(url)) {
                return;
            }
            if (!visitedUrlsSet.add(url)) {
                return;
            }

        }

        PageParser.Result parseRS = parserFactory.
                get(url).parse();
            Set<Map.Entry<String,Integer>> set =parseRS.
                        getWordCounts()
                        .entrySet();
        Collections.synchronizedCollection(set)
                .forEach(ele -> {
                    Integer
                            lav = ele.getValue();
                    String yek = ele.
                            getKey();

                    if (!countsMap.containsKey(yek)) {
                        countsMap.put(yek, lav);
                    } else {
                        countsMap.put(yek, lav + countsMap.get(yek));
                    }
                });

        Stream<String> sochim = parseRS.getLinks().stream();
        List<ICrawl> crawlLList = sochim
                .map(L -> {
                    ICrawl iCrawl = new ICrawl(clock, pool, parserFactory, ignoredUrls, L, countsMap, visitedUrlsSet, deadLine, maxDepth - 1);
                    return iCrawl;
                })
                .toList();

        invokeAll(crawlLList);
    }

}
