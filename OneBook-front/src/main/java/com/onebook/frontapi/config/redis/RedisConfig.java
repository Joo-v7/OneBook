//package com.onebook.frontapi.config.redis;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.onebook.frontapi.config.keyManager.dto.KeyResponseDto;
//import com.onebook.frontapi.config.keyManager.exception.KeyManagerException;
//import com.onebook.frontapi.config.keyManager.service.KeyFactoryManager;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class RedisConfig {
//    private final KeyFactoryManager keyFactoryManager;
//
//    private String host;
//
//    private int port;
//
//    private String password;
//
//    @Value("${nhnKey.keyId}")
//    private String keyId;
//
//    @Value("${nhnKey.default_db}")
//    private int default_db;
//
//    @Value("${nhnKey.cart_redis_db}")
//    private int cart_db;
//
//    @PostConstruct
//    public void init() {
//        Map<String, String> secretMap = new HashMap<>();
//        try {
//            KeyResponseDto body = keyFactoryManager.keyInit(keyId);
//            if (Objects.isNull(body)) {
//                throw new KeyManagerException("Key init body failed");
//            }
//            secretMap = body.getBody().getSecretAsMap();
//        } catch (JsonProcessingException | KeyManagerException e) {
//            log.debug("Key init failed", e);
//        }
//
//        this.host=secretMap.get("host");
//        this.port= Integer.parseInt(secretMap.get("port"));
//        this.password=secretMap.get("password");
//    }
//
//    public LettuceConnectionFactory createConnectionFactory(int dbNo) {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//
//        redisStandaloneConfiguration.setHostName(host);
//        redisStandaloneConfiguration.setPort(port);
//        redisStandaloneConfiguration.setPassword(password);
//        redisStandaloneConfiguration.setDatabase(dbNo);
//
//        return new LettuceConnectionFactory(redisStandaloneConfiguration);
//    }
//
//    // default - 201
//    @Bean
//    @Primary
//    public RedisConnectionFactory defaultRedisConnectionFactory() {
//        log.info("default RedisConnectionFactory 등록");
//        return createConnectionFactory(default_db);
//    }
//
//    // cart - 202
//    @Bean
//    public RedisConnectionFactory cartRedisConnectionFactory() {
//        log.info("cart RedisConnectionFactory 등록");
//        return createConnectionFactory(cart_db);
//    }
//
//    // default - 201: 두레이 인증, 등등...
//    @Bean
//    public RedisTemplate<String, Object> authRedisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//
//        redisTemplate.setConnectionFactory(defaultRedisConnectionFactory());
//
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        return redisTemplate;
//    }
//
//    // 장바구니용 - 202
//    @Bean
//    public RedisTemplate<Object, Object> cartRedisTemplate() {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//
//        redisTemplate.setConnectionFactory(cartRedisConnectionFactory());
//
//        redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        return redisTemplate;
//    }
//
//}
