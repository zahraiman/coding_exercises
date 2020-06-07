package com.stripe.interview.service;

import com.stripe.interview.model.Account;
import java.util.Collection;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class AccountService {
  private static HashMap<String, Account> idAccountMap;

  public AccountService() {
    idAccountMap = new HashMap<>();
  }

  public Collection<Account> list() {
    return idAccountMap.values();
  }

  public Account createAccount(Request request) {
    return new Account(request.queryParams("bussinessType"), request.queryParams("company"));
  }

  public Account updateAccount(Request request) {
    String id = request.params(":id");
    if (!request.queryParams().contains("businessType")
        || !request.queryParams().contains("company")
        || !idAccountMap.containsKey(id)) {
      throw new IllegalArgumentException("parameter id is required");
    }

    idAccountMap.get(id).setBusinessType(request.queryParams("bussinessType"));
    idAccountMap.get(id).setCompany(request.queryParams("company"));
    return idAccountMap.get(id);
  }

  public boolean deleteAccount(Request request) {
    String id = request.params(":id");
    if(idAccountMap.containsKey(id)){
      idAccountMap.remove(id);
      return true;
    }
    return false;
  }

  public Account get(String id) {
    if(idAccountMap.containsKey(id)){
      return idAccountMap.get(id);
    }
    throw new IllegalArgumentException("parameter id is required");
  }
}
