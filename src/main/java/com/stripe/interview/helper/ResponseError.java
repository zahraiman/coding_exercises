package com.stripe.interview.helper;

public class ResponseError {
  public String message;

  public ResponseError(String message, String... args) {
    this.message = String.format(message, args);
  }

  public ResponseError(String message) {
    this.message = message;
  }

  public ResponseError(Exception e) {
    this.message = e.getMessage();
  }

  public String getMessage() {
    return message;
  }
}
