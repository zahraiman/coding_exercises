package com.stripe.interview.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Block {
  //["CHARGE: card_country=US&currency=USD&amount=150&ip_country=CA","ALLOW:amount<100","BLOCK:card_country != ip_country AND amount > 100",]

  List<Condition> conditions;
  Charge charge;

  public Block(Charge charge, List<Condition> conditions) {
    this.charge = charge;
    this.conditions = conditions;
  }

  public double calculateBlockOutput() throws IllegalAccessException {
    for(Condition c : conditions){
      boolean isValid = validateCondition(charge, c);
      if(!isValid)
        return 0;
    }
    return charge.amount;
  }

  public static List<Block> readBlockFromFile(InputStream file) throws IOException {
    //["CHARGE: card_country=US&currency=USD&amount=150&ip_country=CA",
    // "ALLOW:amount<100",
    // "BLOCK:card_country != ip_country AND amount > 100",]
//    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<Block> blocks = new ArrayList<>();
//    Pattern operatorMatcher = Pattern.compile("(.*) > (.*)");
    //Pattern linePattern = Pattern.compile("\\[(.*)\\]"); // ? : optional
    BufferedReader in = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
    String line;
    while((line = in.readLine()) != null) {
      //Matcher matcher = linePattern.matcher(line);

      String[] blockString = line.replaceAll("\\[|\\]|\"", "").split(",");
      String[] chargeString = blockString[0].split(": ")[1].split("[=&]");
//      Charge charge = gson.fromJson(blockString[0], Charge.class);
      Charge charge = new Charge(chargeString[1], chargeString[3], Double.valueOf(chargeString[5]), chargeString[7]);
      List<Condition> conditions = new ArrayList<>();
      for (int i = 1; i < blockString.length; i++) {
        String[] conditionStrings = blockString[i].split(":");
        String[] predicates = conditionStrings[1].split(" AND ");
        for (String predicate : predicates) {
         // Matcher operatorMatch = operatorMatcher.matcher(predicate);
          Condition c = null;
          for(String op : new String[]{">", "<", "!=", "="}){
            String[] splits = predicate.split(op);
            if(splits.length > 1){
              c = new Condition(conditionStrings[0], op, splits[0].trim(), splits[1].trim());
              break;
            }
          }

          if(c != null)
            conditions.add(c);

        }
      }
      blocks.add(new Block(charge, conditions));
    }

    return blocks;
  }

  private boolean validateCondition(Charge charge, Condition condition) throws IllegalAccessException {
    boolean isAllow = condition.ruleType;
    Stream<Field> chargeFields = Arrays.stream(charge.getClass().getDeclaredFields());

    // condition: ALLOW: amount<100
    // charge: "CHARGE: card_country=US&currency=USD&amount=150&ip_country=CA

    Field chargePredicate = Arrays.stream(charge.getClass().getDeclaredFields())
        .filter(f -> f.getAnnotation(SerializedName.class).value().equals(condition.getPredicate()))
        .findFirst().orElse(null);
    Field conditionField = Arrays.stream(charge.getClass().getDeclaredFields())
        .filter(f -> f.getAnnotation(SerializedName.class).value().equals(condition.getValue()))
        .findFirst().orElse(null);
    Object predicateValue = chargePredicate != null ? chargePredicate.get(charge) : null;
    boolean conditionValid;

    try {
      if (conditionField == null) {
        double conditionValue = Double.parseDouble(condition.getValue().toString());
        conditionValid = validateCondition((double) predicateValue, conditionValue, condition.getOperator());
      }else{
        conditionValid = validateCondition((String) predicateValue, (String) conditionField.get(charge), condition.getOperator());
      }
    }catch(NumberFormatException e){
      throw new IllegalAccessException(String.format("value %s format is not valid", conditionField.toString()));
    }

    if(!isAllow)
      conditionValid = !conditionValid;

    return conditionValid;
  }

  public boolean validateCondition(double chargeValue, double conditionValue, OperaterEnum op){
    switch (op){
      case DIGIT_LESS_THAN:
        return chargeValue < conditionValue;
      case DIGIT_MORE_THAN:
        return chargeValue > conditionValue;
      case EQUAL:
        return chargeValue == conditionValue;
      case NOT_EQUAL:
        return chargeValue != conditionValue;
    }
    return false;
  }

  public boolean validateCondition(String chargeValue, String conditionValue, OperaterEnum op) throws IllegalAccessException {
    switch (op){
      case DIGIT_LESS_THAN:
      case DIGIT_MORE_THAN:
        throw new IllegalAccessException(String.format("Invalid operator for %s %s", chargeValue, conditionValue));
      case EQUAL:
        return chargeValue.equals(conditionValue);
      case NOT_EQUAL:
        return !chargeValue.equals(conditionValue);
    }
    return false;
  }

  public enum OperaterEnum {
    DIGIT_LESS_THAN,
    DIGIT_MORE_THAN,
    EQUAL,
    NOT_EQUAL
  }

  public static class Condition{
    static HashMap<String, Field> allowedPredicateMap;

    @SerializedName("operator")
    OperaterEnum operator;

    @SerializedName("rule_type")
    boolean ruleType;

    @SerializedName("predicate")
    String predicate;

    @SerializedName("value")
    Object value;

    public Condition(String ruleType, String operator, String predicate, Object value){
      if(!getAllowedPredicates().containsKey(predicate))
        throw new ExceptionInInitializerError(String.format("Cannot create condition with %s", predicate));
      OperaterEnum op;
      switch(operator){
        case(">"):
          op = OperaterEnum.DIGIT_MORE_THAN;
          break;
        case("<"):
          op = OperaterEnum.DIGIT_LESS_THAN;
          break;
        case("="):
          op = OperaterEnum.EQUAL;
          break;
        case("!="):
          op = OperaterEnum.NOT_EQUAL;
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + operator);
      }
      this.ruleType = ruleType.equals("ALLOW");
      this.operator = op;
      this.predicate = predicate;
      this.value = value;
    }

    public HashMap<String, Field> getAllowedPredicates(){
      if(allowedPredicateMap == null || allowedPredicateMap.isEmpty())
        setAllowedPredicates();
      return allowedPredicateMap;
    }

    public void setAllowedPredicates(){
      allowedPredicateMap = (HashMap<String, Field>) Arrays.stream(Block.Charge.class.getDeclaredFields())
          .map(f -> new AbstractMap.SimpleEntry<String, Field>(f.getAnnotation(SerializedName.class).value(), f))
          .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    public OperaterEnum getOperator() {
      return operator;
    }

    public boolean isRuleType() {
      return ruleType;
    }

    public String getPredicate() {
      return predicate;
    }

    public Object getValue() {
      return value;
    }
  }


  public static class Charge{
    @SerializedName("card_country")
    String cardCountry;

    @SerializedName("currency")
    String currency;

    @SerializedName("amount")
    double amount;

    @SerializedName("ip_country")
    String ipCountry;

    public Charge(String cardCountry, String currency, double amount, String ipCountry) {
      this.cardCountry = cardCountry;
      this.currency = currency;
      this.amount = amount;
      this.ipCountry = ipCountry;
    }
  }
}
