package com.stripe.interview;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.stripe.interview.controller.UserController;
import com.stripe.interview.service.UserService;

public class UserModule extends AbstractModule {

  @Override
  protected void configure() {
//    bind(UserController.class).to((Class<? extends UserController>) Controller.class);
    bind(UserService.class).annotatedWith(Names.named("DefaultUserService")).to(UserService.class);
//    try {
//      bind(UserController.class).toConstructor(UserController.class.getConstructor(UserService.class));
//    } catch (NoSuchMethodException e) {
//      e.printStackTrace();
//    }
  }
}
