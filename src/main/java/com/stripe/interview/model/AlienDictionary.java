package com.stripe.interview.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AlienDictionary {
  @SerializedName("words")
  List<String> words;

  @SerializedName("letter_orders")
  String letterOrders;

  LinkedList<Character> orderList;
  List<Character> currentOrder;
  HashMap<Character, Integer> charOrder;

  public AlienDictionary(List<String> words) {
    this.words = words;
    orderList = new LinkedList<>();
    charOrder = new HashMap<>();
    currentOrder = new ArrayList<>();
  }

  public String getLetterOrders(){
    String res= "";

    for(String w : words){
      insertToOrderList(w);
    }
    //letterOrders = (String) orderList.entrySet().stream().collect(Collectors.joining("ba"));
    return res;
  }

  private void insertToOrderList(String w) {
    // [w, r, t, f]
    // [e r]

    if(currentOrder.isEmpty()){
      currentOrder = w.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
      currentOrder.forEach(c -> charOrder.put(c, currentOrder.indexOf(c)));
      return;
    }

    char[] strArr = w.toCharArray();
    int[] order = new int[strArr.length];
    for(int i = 0; i < strArr.length; i++){
      order[i] = charOrder.getOrDefault(strArr[i], Integer.MAX_VALUE);
    }

    for(int i = 0; i < order.length; i++) {
      // Try to find order based on elements coming before this
      if (order[i] == Integer.MAX_VALUE) {
        for (int j = i - 1; j >= 0; j--) {
          if (order[j] != Integer.MAX_VALUE) {
            order[i] = order[j] + 1;
            break;
          }
        }
      }

      // Try to find order based on elements coming after this
      if (order[i] == Integer.MAX_VALUE) {
        for (int j = i + 1; j < order.length; j++) {
          if (order[j] != Integer.MAX_VALUE) {
            order[i] = order[j] - 1;
            break;
          }
        }
      }
    }

    // Merge
    merge(strArr, order);

  }


  public String getAlphabetOrder(){
    int maxLen = Integer.MIN_VALUE;
    for(String w : words){
      maxLen = Math.max(maxLen, w.length());
    }

//    List<Map<String, Integer>> list = Arrays.asList(Map.of("a", 1, "b", 2), Map.of("c", 3, "b", 4));

    List<Character> characterList = new ArrayList<>();
    for(int i = 0; i < maxLen; i++){
      // Get all characters at location i
      for(String w : words){
        if(i < w.length()){
          char c = w.charAt(i);
          if(!characterList.contains(c)) {
            characterList.add(w.charAt(i));
          }
        }
      }
    }

    return characterList.stream().map(String::valueOf).collect(Collectors.joining(""));
  }


  public void merge(char[] currStr, int[] order){
    // currentOrder, currStr

    List<Character> res = new ArrayList<>();
    int i = 0, k = 0;

    while(i < currentOrder.size() && k < currStr.length){
      if(charOrder.get(currentOrder.get(i)) == order[k]){
        res.add(currentOrder.get(i));
        if(currentOrder.get(i) != currStr[k]) {
          res.add(currStr[k]);k++;
        }else {
          while (k < currStr.length && currStr[k] == currentOrder.get(i)) {
            k++;
          }
        }
        i++;
      }else if(charOrder.get(currentOrder.get(i)) < order[k]){
        res.add(currentOrder.get(i));
        i++;
      }else{
        res.add(currStr[k]);
        k++;
      }
    }

    if(i < currentOrder.size()) {
      res.addAll(currentOrder.subList(i, currentOrder.size()));
    }

    if(k < currStr.length) {
      for (int j = k; j < currStr.length; j++) {
        res.add(currStr[j]);
      }
    }

    currentOrder = res;
    currentOrder.forEach(c -> charOrder.put(c, currentOrder.indexOf(c)));

  }

}
