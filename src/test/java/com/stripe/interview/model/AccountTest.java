package com.stripe.interview.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccountTest {
  public static List<Account> accountList;

  public void setAccountList() throws IOException {
    Gson gson =
        new GsonBuilder()
            //.registerTypeAdapter()
            .serializeNulls()
        .create();
    Account acc = gson.fromJson(getResourceAsString("/model_fixtures/account_model.json"), Account.class);
    System.out.println(gson.toJson(acc).toString());
    accountList = new ArrayList<>();
    accountList.add(acc);
  }

  @Test
  public void getAccount() throws IOException {
    setAccountList();
    assert(accountList.size() == 1);
  }

  private String getResourceAsString(String path) throws IOException {
    InputStream resource = getClass().getResourceAsStream(path);

    ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
    byte[] buf = new byte[1024];

    for (int i = resource.read(buf); i > 0; i = resource.read(buf)) {
      os.write(buf, 0, i);
    }

    return os.toString("utf8");
  }

}
