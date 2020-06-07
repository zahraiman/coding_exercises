package com.stripe.interview.controller;
import static com.stripe.interview.helper.JsonUtil.json;
import static com.stripe.interview.helper.JsonUtil.toJson;
import static spark.Spark.*;

import com.stripe.interview.helper.ResponseError;
import com.stripe.interview.service.PaymentService;

public class PaymentController {
  private static PaymentService paymentService;

  public PaymentController() {
    get("payments", (req, resp) -> {
      return paymentService.getPayments();
    }, json());


    exception(Exception.class, (e, req, res) -> {
      res.status(400);
      res.body(toJson(new ResponseError(e)));
    });

    after((req, res) -> {
      res.type("application/json");
    });
  }
}
