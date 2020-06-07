package com.stripe.interview.model;

import java.util.concurrent.Semaphore;

public class RateLimitingToken<T> {
  int tokenSize;
  int givenTokenSize;
  T[] tokens;
  final Semaphore semaphore;

  public RateLimitingToken(int tokenSize) {
    tokens = (T[]) new Object[tokenSize];
    this.tokenSize = tokenSize;
    semaphore = new Semaphore(tokenSize);
    this.givenTokenSize = 0;
  }

//  public T getToken(){
//    T token = null;
//    synchronized (semaphore){
////      if(givenTokenSize == tokenSize)
//       /// lock.wait();
//    }
//  }
}
