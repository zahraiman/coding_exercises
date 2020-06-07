package com.stripe.interview.service;

import com.stripe.interview.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {
  public static ArrayList<User> users;

  public UserService() {
    users = new ArrayList<>(Arrays.asList(
        new User("zahra", "z@test.com"),
        new User("Matteo", "matteo@test.com")
    ));
  }

  public List<User> getAllUsers(){
    return users;
  }

  public User getUser(String id) {
    //return users.stream().filter(u -> (u.id == id)).findFirst().get();
    return users.stream().filter(u -> (u.id.equals(id))).findAny().orElse(null);
  }

  // creates a new user
  public User createUser(String name, String email) {
    User u = new User(name, email);
    users.add(u);
    return u;
  }

  // updates an existing user
  public User updateUser(String id, String name, String email) {
    User u = getUser(id);
    if (u == null) {
      throw new IllegalArgumentException("No user with id '" + id + "' found");
    }
    u.setName(name);
    u.setEmail(email);
    return u;
  }

  private int convertId(String id){
    try{
      return Integer.parseInt(id);
    }catch (NumberFormatException e){
      return -1;
    }
  }

  private int generateId(){
    return users.size()+1;
  }

}
