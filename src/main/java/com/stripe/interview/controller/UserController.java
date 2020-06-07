package com.stripe.interview.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.stripe.interview.model.User;
import com.stripe.interview.helper.ResponseError;
import com.stripe.interview.service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.stripe.interview.helper.JsonUtil.json;
import static com.stripe.interview.helper.JsonUtil.toJson;
import static spark.Spark.*; // gives access to various static methods

public class UserController {
  public static final String GET_USERS_PATH = "/users";
  public static final String GET_USER_PATH = "/users/:id";
  public static final String POST_USER_PATH = "/users";
  public static final String PUT_USER_PATH = "/users/:id";

  @Inject
  public UserController(@Named("DefaultUserService") UserService userService) {
    // Registering GET request, Route will process requests
    // get(GET_PATH, (request, response) -> userService.getAllUsers(), json());
    get(GET_USERS_PATH, new Route() {
      @Override
      public Object handle(Request request, Response response){
        return userService.getAllUsers();
      }
    }, json());

    get(GET_USER_PATH, (request, response) -> {
      String id = request.params(":id");
      User user = userService.getUser(id);
      if(user != null) {
        return user;
      }

      response.status(400);
      return new ResponseError("No user is found with %s id ", id);
    }, json());


    post(POST_USER_PATH, (request, response) -> {
//      JsonObject bodyParams = new Gson().fromJson(request.body(), JsonObject.class).getAsJsonObject();
//      System.out.println(bodyParams);
//      System.out.println(bodyParams.keySet());
//      System.out.println(bodyParams.get("email"));
      // Map<String,String> map = new Gson().fromJson(body, HashMap.class)
      return userService.createUser(
        request.queryParams("name"),
        request.queryParams("email")
      );
    }, json());

    put(PUT_USER_PATH, (request, response) -> userService.updateUser(
        request.params(":id"),
        request.queryParams("name"),
        request.queryParams("email")
    ), json());

    exception(IllegalArgumentException.class, (e, req, res) -> {
      res.status(400);
      res.body(toJson(new ResponseError(e)));
    });

    after((req, res) -> {
      res.type("application/json");
    });
  }
}
