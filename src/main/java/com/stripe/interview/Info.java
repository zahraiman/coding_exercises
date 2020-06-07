package com.stripe.interview;

public class Info {
  //public static final Gson GSON = createGson();
  public static void main(String[] args) {

    int id = 1;
    String url =
        String.format(
            "%s%s",
            getApiBase(),
            String.format("/v1/accounts/%s/reject", id));


  }

  public Info() {
  }

  public Info(MyDatabase db){

  }

  public static String getApiBase() {
    return "blah";
  }


  public class MyDatabase{
    public MyDatabase() {
    }
  }
}
