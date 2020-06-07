package com.stripe.interview.model;

import java.util.UUID;
import org.junit.Test;

public class BlockingQueueTest {

  @Test
  public void multiThreadProperlyWait() throws InterruptedException {
    BlockingQueue<String> blockingQueue = new BlockingQueue<>(10);
    Thread dequeuingTh = new Thread(() -> {
      try {
        for (int i = 0; i < 50; i++) {
          String st = blockingQueue.dequeue();
          System.out.println("dequeued: " + st);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Thread enqueuingTh = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          for (int i = 0; i < 50; i++) {
            String st = "Random_" + UUID.randomUUID().toString();
            blockingQueue.enqueue(st);
            System.out.println("enqueued: " + st);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    enqueuingTh.start();
    dequeuingTh.start();

    enqueuingTh.join();
    dequeuingTh.join();

  }
}
