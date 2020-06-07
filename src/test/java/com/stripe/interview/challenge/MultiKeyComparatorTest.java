package com.stripe.interview.challenge;

import com.stripe.interview.model.Account;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.junit.Test;

public class MultiKeyComparatorTest {

  @Test
  public void sortsAccountByBusinessTypeThenCompany(){
    List<Account> accounts = Arrays.asList(
        new Account("gghh", "ccdd", 2),
        new Account("aabb", "ccdd", 3),
        new Account("gghh", "kkmb", 10),
        new Account("gghh", "kkmb", 7),
        new Account("ccdd", "ccdd", 1)
        );

    MultiKeyComparator<Account> multiKeyComparator =
        new MultiKeyComparator<Account>(
            Arrays.asList(
                new MultiKeyComparator.compareKeyImpl<String>("businessType", String::compareTo),
                new MultiKeyComparator.compareKeyImpl<>("company", String::compareTo),
                new MultiKeyComparator.compareKeyImpl<>("numOfCharges", Integer::compareTo)
            )
        );
    accounts.sort(multiKeyComparator);


    assert accounts.get(0).getBusinessType().equals("aabb");
    assert accounts.get(1).getBusinessType().equals("ccdd");
    assert accounts.get(2).getBusinessType().equals("gghh");
    assert accounts.get(2).getCompany().equals("ccdd");
    assert accounts.get(3).getCompany().equals("kkmb");
    assert accounts.get(3).getNumOfCharges() == 7;

  }
}
