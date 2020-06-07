package com.stripe.interview.model;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;

public class Account {
  @SerializedName("id")
  String id;

  @SerializedName("business_type")
  String businessType;

  @SerializedName("company")
  String company;

  @SerializedName("numberOfCharges")
  int numOfCharges;

  public Account(String businessType, String company) {
    this.businessType = businessType;
    this.company = company;
    this.id = UUID.randomUUID().toString();
  }

  public Account(String businessType, String company, int numOfCharges) {
    this.businessType = businessType;
    this.company = company;
    this.numOfCharges = numOfCharges;
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public int getNumOfCharges() {
    return numOfCharges;
  }

  public void setNumOfCharges(int numOfCharges) {
    this.numOfCharges = numOfCharges;
  }
}
