package com.stripe.interview.challenge;

import org.junit.Test;

public class QuestionMarkTest {
  QuestionMark qstMark = new QuestionMark();

  @Test
  public void givenStringWithCorrectQstMarksItSucceeds(){
    String[] tests = new String[]{"arrb6???4xxbl5???eee5","acc?7??sss?3rr1??????5","9???1???9???1???9"};
    for(String st : tests)
      assert qstMark.hasThreeQstMark(st, 10, 3);
  }

  @Test
  public void givenStringWithIncorrectQstMarksItFails(){
    String[] tests = new String[]{"aa6?9", "5??aaaaaaaaaaaaaaaaaaa?5?5"};
    for(String st : tests)
      assert !qstMark.hasThreeQstMark(st, 10, 3);
  }

  @Test
  public void givenStringWithPatternsAndCorrectQstMarksItSucceeds(){
    String[] tests = new String[]{"arrb6???4xxbl5???eee5","acc?7??sss?3rr1??????5","9???1???9???1???9", "5??aaaaaaaaaaaaaaaaaaa?5?4"};
    for(String st : tests)
      assert qstMark.hasQuestionMarksWithPatterns(st);
  }

  @Test
  public void givenStringWithPatternsAndIncorrectQstMarksItFails(){
    String[] tests = new String[]{"aa6?9", "5??aaaaaaaaaaaaaaaaaaa?5?5"};
    for(String st : tests)
      assert !qstMark.hasQuestionMarksWithPatterns(st);
  }
}
