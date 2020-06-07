package com.stripe.interview.challenge;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MultiKeyComparator<T> implements Comparator<T> {
  List<compareKeyImpl> keyOrders;


  // Each pair tie breaker for the previous
//  public MultiKeyComparator(List<compareKeyImpl> keyOrders) {
//    this.keyOrders = keyOrders;
//  }

  public MultiKeyComparator(List<compareKeyImpl> keyOrders) {
    this.keyOrders = keyOrders;
  }

  @Override
  public int compare(T o1, T o2) {
    int ind = 0;
    int compare = 0;
    while(ind < keyOrders.size() && compare == 0){
      compareKeyImpl curr = keyOrders.get(ind);
      Field field = Arrays
          .stream(o1.getClass().getDeclaredFields())
          .filter(f -> f.getName().equals(curr.getName()))
          .findFirst().orElse(null);
      try {

        assert field != null;
        field.setAccessible(true);
        compare = curr.getCompareKey().compare(field.get(o1), field.get(o2));
        ind++;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return compare;
  }


  static class compareKeyImpl<T> {
    Comparator<T> comparator;
    String name;

    public compareKeyImpl(String name, Comparator<T> comparator) {
      this.name = name;
      this.comparator = comparator;
    }

    public Comparator<T> getCompareKey() {
      return comparator;
    }

    public String getName() {
      return name;
    }
  }
}
