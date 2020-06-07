package com.stripe.interview.challenge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionMark {

  public static void main(String[] args) throws IOException {
    /*
      "arrb6???4xxbl5???eee5" => true
      "acc?7??sss?3rr1??????5" => true
      "5??aaaaaaaaaaaaaaaaaaa?5?5" => false
      "9???1???9???1???9" => true
      "aa6?9" => false
     */

    // Take an input string parameter and determine if exactly 3 question marks
    // exist between every pair of numbers that add up to 10.
    new QuestionMark().readFile();
    System.out.println("*************************");
    new QuestionMark().readJsonFile();
  }

  public boolean hasThreeQstMark(String s, int target, int qstTarget){
    List<int[]> pairs = getDigitPairs(s, target);
    if(pairs.size() == 0)
      return false;
    //"arrb6???4xxbl5???eee5" => true
    // digitLocs = {4, 8, 13}
    char[] arr = s.toCharArray();
    for (int[] pair : pairs) {
      int qstCount = 0;
      for (int k = pair[0]+1; k < pair[1]; k++) {
        if (arr[k] == '?') {
          qstCount++;
        }
      }
      if (qstCount != qstTarget)
        return false;
    }

    return true;
  }



  public List<int[]> getDigitPairs(String s, int target){
    List<Integer> res = new ArrayList<>();
    List<int[]> pairs = new ArrayList<>();
    char[] arr = s.toCharArray();
    for (int i = 0; i < arr.length; i++){
      if (Character.isDigit(arr[i])){
        res.add(i);
      }
    }

    for(int i = 0; i < res.size(); i++){
      for(int j = i+1; j < res.size(); j++){
        if(Character.getNumericValue(arr[res.get(i)]) + Character.getNumericValue(arr[res.get(j)]) ==  target){
          pairs.add(new int[]{res.get(i), res.get(j)});
          break;
        }
      }
    }

    return pairs;
  }

  public boolean hasQuestionMarksWithPatterns(String str){
    str = str.replaceAll("[a-z]+","");
    Pattern pattern = Pattern.compile("([0-9])([?])([?])([?])([0-9])");
    Pattern pattern01 = Pattern.compile("([0-9])([?])([?])([0-9])");
    Matcher matcher01 = pattern01.matcher(str);
    Pattern pattern02 = Pattern.compile("([0-9])([?])([0-9])");
    Matcher matcher02 = pattern02.matcher(str);
    Matcher matcher = pattern.matcher(str);

    if (matcher01.find() || matcher02.find()) {
      return false;
    } else if (matcher.find()) {
      return true;
    }

    return false;
  }

  public void readFile() throws IOException {
    InputStream is = getClass().getResourceAsStream("/data.json");
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    while ((line = br.readLine())!= null)
      System.out.println(line);

  }


  public void readJsonFile() throws IOException {
    InputStream is = getClass().getResourceAsStream("/data.json");
    Gson gson = new GsonBuilder().create();
    UserPass[] userPasses = gson.fromJson(new InputStreamReader(is), UserPass[].class);
    for(UserPass u : userPasses)
      System.out.println(gson.toJson(u));

  }


  static class UserPass{
    @SerializedName("name")
    String name;

    @SerializedName("pass")
    String pass;

    public UserPass(String name, String pass) {
      this.name = name;
      this.pass = pass;
    }

    public String getName() {
      return name;
    }

    public String getPass() {
      return pass;
    }
  }
}
