package com.stripe.interview.model;

import com.google.gson.annotations.SerializedName;

public class Payment {
  @SerializedName("items")
  Object[] items;

  @SerializedName("currency")
  String currency;

  @SerializedName("id")
  String id;

  public Payment(String currency, Object[] items) {
    this.currency = currency;
    this.items = items;
  }

  public String getId() {
    return id;
  }

  public Object[] getItems() {
    return items;
  }

  public void setItems(Object[] items) {
    this.items = items;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
