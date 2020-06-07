package com.stripe.interview.service;

import com.stripe.interview.model.Payment;
import java.util.HashMap;
import java.util.List;

public class PaymentService {
  private static HashMap<String, Payment> payments;

  public PaymentService() {
    payments = new HashMap<String, Payment>();
  }

  public List<Payment> getPayments(){
    return (List<Payment>) payments.values();
  }


  public Payment getPayment(String id){
    if(payments.containsKey(id))
      return payments.get(id);
    return null;
  }

  public void addPayment(String currency, Object[] items){
    Payment payment = new Payment(currency, items);
    payments.put(payment.getId(), payment);
  }

  public void updatePayment(String id, String currency) throws Exception {
    if(payments.containsKey(id)){
      payments.get(id).setCurrency(currency);
    }else{
      throw new Exception(String.format("Item with id: %s not found", id));
    }
  }

  public void updatePayment(String id, Object[] items) throws Exception {
    if(payments.containsKey(id)){
      payments.get(id).setItems(items);
    }else{
      throw new Exception(String.format("Item with id: %s not found", id));
    }
  }


}
