package com.example.tumiweb.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {
  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    Cache cacheUser = new ConcurrentMapCache("user");
    Cache cacheQuestion = new ConcurrentMapCache("question");
    Cache cacheNotification = new ConcurrentMapCache("notification");
    Cache cacheHelp = new ConcurrentMapCache("help");
    Cache cacheGift = new ConcurrentMapCache("gift");
    Cache cacheGiftOrder = new ConcurrentMapCache("gift-order");
    Cache cacheDiary = new ConcurrentMapCache("diary");
    Cache cacheCourse = new ConcurrentMapCache("course");
    Cache cacheChapter = new ConcurrentMapCache("chapter");
    Cache cacheAnswer = new ConcurrentMapCache("answer");
    Cache cacheCategory = new ConcurrentMapCache("category");
    Cache cacheDBFile = new ConcurrentMapCache("dbfile");
    cacheManager.setCaches(Arrays.asList(cacheUser,
        cacheQuestion,
        cacheNotification,
        cacheHelp,
        cacheGift,
        cacheGiftOrder,
        cacheDiary,
        cacheCourse,
        cacheChapter,
        cacheAnswer,
        cacheCategory,
        cacheDBFile));

    return cacheManager;
  }
}