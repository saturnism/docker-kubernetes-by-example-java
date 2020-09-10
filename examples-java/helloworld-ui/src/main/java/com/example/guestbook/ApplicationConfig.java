/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.guestbook;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rayt on 5/1/17.
 */
@Configuration
@EnableRedisHttpSession
public class ApplicationConfig {

  @Value("${HELLOWORLDSERVICE_PORT:tcp://localhost:8081}")
  private String helloworldServiceEndpoint;

  @Value("${GUESTBOOKSERVICE_PORT:tcp://localhost:8082}")
  private String guestbookServiceEndpoint;

  @Value("${REDIS_PORT:tcp://localhost:6379}")
  private String redisEndpoint;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  HelloworldService helloworldService(RestTemplate restTemplate) {
    String endpoint = helloworldServiceEndpoint.trim().replace("tcp:", "http:") + "/hello";
    return new HelloworldService(restTemplate, endpoint);
  }

  @Bean
  GuestbookService guestbookService(RestTemplate restTemplate) {
    String endpoint = guestbookServiceEndpoint.trim().replace("tcp:", "http:") + "/api/messages";
    return new GuestbookService(restTemplate, endpoint);
  }

  @Bean
  RedisConnectionFactory redisConnectionFactory() throws URISyntaxException {
    URI uri = new URI(redisEndpoint);
    RedisConnectionFactory factory = new LettuceConnectionFactory(uri.getHost(), uri.getPort());
    return factory;
  }

}
