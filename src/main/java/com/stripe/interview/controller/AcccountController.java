package com.stripe.interview.controller;


import static com.stripe.interview.helper.JsonUtil.json;
import static com.stripe.interview.helper.JsonUtil.toJson;
import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.stripe.interview.helper.ResponseError;
import com.stripe.interview.service.AccountService;

public class AcccountController {

  @Inject
  public AcccountController(AccountService accountService) {

    get("api/v1/accounts/:id", (request, response) -> accountService.get(request.params(":id")), json());

    get("api/v1/accounts", (request, response) -> accountService.list(), json());

    post("api/v1/accounts", (request, response) -> accountService.createAccount(request), json());

    put("api/v1/accounts/:id", (request, response) -> accountService.updateAccount(request), json());

    delete("api/v1/accounts", (request, response) -> accountService.deleteAccount(request), json());

    exception(IllegalArgumentException.class, (e, request, response) -> {
      response.status(400);
      response.body(toJson(new ResponseError(e)));
    });

    after((request, response) -> response.type("application/json"));

  }
}
