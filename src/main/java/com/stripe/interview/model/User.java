package com.stripe.interview.model;

import java.util.UUID;

public class User {
  public String id;
  public String name;
  public String email;
  // transient skips the json serialization
  //private transient int lastId;


  public User(String name, String email) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
