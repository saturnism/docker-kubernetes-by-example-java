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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.util.SpanNameUtil;
import org.springframework.stereotype.Component;

/**
 * Created by rayt on 5/2/17.
 */
@Aspect
@Component
public class JdbcTraceAspect {
  private final Tracer tracer;

  public JdbcTraceAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  @Around("execution (* org.springframework.jdbc.core.JdbcTemplate.*(..))")
  public Object traceJdbcCall(final ProceedingJoinPoint pjp) throws Throwable {
    String spanName = SpanNameUtil.toLowerHyphen(pjp.getSignature().getName());
    Span span = this.tracer.createSpan("jdbc:/" + spanName);
    try {
      return pjp.proceed();
    }
    finally {
      this.tracer.close(span);
    }
  }

}
