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
package io.rockscript.api;

import com.google.gson.Gson;
import io.rockscript.Engine;
import io.rockscript.http.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandHandler extends AbstractRequestHandler {

  static Logger log = LoggerFactory.getLogger(CommandHandler.class);

  public CommandHandler(Engine engine) {
    super(POST, "/command", engine);
  }

  @Override
  public void handle(ServerRequest request, ServerResponse response) {
    String jsonBodyString = request.getBodyAsString();
    BadRequestException.throwIfNull(jsonBodyString, "No command was provided in the body");
    try {
      Gson gson = engine.getGson();
      Command command = gson.fromJson(jsonBodyString, Command.class);
      BadRequestException.throwIfNull(command, "No valid command was provided in the body: "+jsonBodyString);
      Object commandResponse = command.execute(engine);
      response.bodyJson(commandResponse);
      response.status(200);
    } catch (HttpException e) {
      throw e;
    } catch (Exception e) {
      log.debug("Couldn't execute command "+jsonBodyString+": "+e.getMessage(), e);
      throw new InternalServerException();
    }
  }
}
