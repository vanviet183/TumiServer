package com.example.tumiweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public StringRedisSerializer stringRedisSerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public JedisConnectionFactory connectionFactory() {
    final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setPort(6379);
    configuration.setHostName("localhost");
    return new JedisConnectionFactory(configuration);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new JdkSerializationRedisSerializer());
    template.setValueSerializer(new JdkSerializationRedisSerializer());
    template.setEnableTransactionSupport(true);

    template.afterPropertiesSet();
    return template;
  }
}
