package com.stripe.interview.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlienDictionaryTest {
  public static AlienDictionary alienDictionary;


  @Before
  public void setUp(){
    List<String> givenWords = Arrays.asList(
        "wrt",
        "wrf",
        "er",
        "ett",
        "rftt"
    );
    alienDictionary = new AlienDictionary(givenWords);
  }


  @Test
  public void givenWordsReturnsRightOrder(){
    String correctOrder = "wertf";
    assert(alienDictionary.getAlphabetOrder().equals(correctOrder));
  }

  @Test
  public void givenDifferentWordsReturnsRightOrder(){
    String correctOrder = "efg";
    alienDictionary = new AlienDictionary( Arrays.asList("eff", "fg"));
    assert(alienDictionary.getAlphabetOrder().equals(correctOrder));
  }

}
