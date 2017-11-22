/*
 * Copyright (c) 2017 RockScript.io.
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.rockscript.http.test;

import io.rockscript.http.servlet.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ServerTest extends AbstractServerTest {

  @Override
  protected void configureServer(TestServer server) {
    RouterServlet servlet = new RouterServlet();
    servlet.requestHandler(new GreetingHandler());
    servlet.requestHandler(new BangHandler());
    server.servlet(servlet);
  }

  @Get("/hello/{greeting}")
  public static class GreetingHandler implements RequestHandler {
    @Override
    public void handle(ServerRequest request, ServerResponse response) {
      response.bodyString("world");
      assertEquals("john", request.getPathParameter("greeting"));
      response.statusOk();
    }
  }

  @Get("/bang")
  public static class BangHandler implements RequestHandler {
    @Override
    public void handle(ServerRequest request, ServerResponse response) {
      throw new RuntimeException("bang");
    }
  }

  @Test
  public void testHttpOk() {
    String response = newGet("/hello/john")
      .execute()
      .getBody();
    assertEquals("world", response);
  }

  @Test
  public void testHttpInternalServerException() {
    try {
      newGet("/bang")
        .execute()
        .assertStatusOk();
      fail("Expected exception");
    } catch (RuntimeException e) {
      assertEquals("bang", e.getCause().getMessage());
    }
  }
}
