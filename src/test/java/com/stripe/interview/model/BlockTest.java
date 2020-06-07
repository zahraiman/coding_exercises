package com.stripe.interview.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import spark.Response;

public class BlockTest {
  public Block block;
  //["CHARGE: card_country=US&currency=USD&amount=150&ip_country=CA","ALLOW:amount<100","BLOCK:card_country != ip_country AND amount > 100",]
//  @Before
//  public void setBlocks(){
//
//    block = new Block(new Block.Charge("US", "USD", 150.0, "CA"), Arrays.asList(
//        new Block.Condition("ALLOW","<","amount",100),
//        new Block.Condition("BLOCK","!=","cardCountry","ipCountry")
//    ));
//  }

  @Test
  public void givenAllowLessThenChargeReturnsZero() throws IllegalAccessException {
    block = new Block(new Block.Charge("US", "USD", 150.0, "CA"), Arrays.asList(
        new Block.Condition("ALLOW","<","amount",100)
    ));
    assert(block.calculateBlockOutput() == 0);
  }

  @Test
  public void givenBlockCountryThenChargeReturnsZero() throws IllegalAccessException {
    block = new Block(new Block.Charge("US", "USD", 150.0, "CA"), Collections.singletonList(
        new Block.Condition("BLOCK", "!=", "card_country", "ip_country")
    ));
    assert(block.calculateBlockOutput() == 0);
  }

  @Test
  public void givenNonMatchingConditionsThenChargeReturnsAmount() throws IllegalAccessException {
    block = new Block(new Block.Charge("US", "USD", 150.0, "US"), Arrays.asList(
        new Block.Condition("BLOCK","!=","card_country","ip_country")
    ));
    assert(block.calculateBlockOutput() == block.charge.amount);
  }

  @Ignore
  @Test
  public void testSync() throws InterruptedException {
    synchronized (this) {
      Object obj = new Object();
      obj.wait();
      System.out.println(obj);
      obj.notify();
    }

    Lock lock = new ReentrantLock();
    Condition myCondition  = lock.newCondition();
    myCondition.signal();
    myCondition.await();

    final Semaphore semaphore = new Semaphore(1);
  }

  @Test
  public void readsFileAndCreatesBlocksCorrectly() throws IOException, IllegalAccessException {
    InputStream file = getClass().getResourceAsStream("/model_fixtures/block_model.txt");
    List<Block> blocks = Block.readBlockFromFile(file);
    assert(blocks.size() == 4);
    assert(blocks.get(0).charge.amount == 150);
    assert blocks.get(0).conditions.size() == 1;
    assert blocks.get(0).conditions.get(0).getOperator() == Block.OperaterEnum.DIGIT_MORE_THAN;
    assert blocks.get(0).conditions.get(0).getPredicate().equals("amount");
    assert Double.parseDouble(blocks.get(0).conditions.get(0).getValue().toString()) == 100;

    assert blocks.get(0).calculateBlockOutput() == blocks.get(0).charge.amount;
    assert blocks.get(1).calculateBlockOutput() == 0;
  }
}
