package com.stripe.interview.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static spark.Spark.awaitInitialization;

import com.google.gson.Gson;
import com.stripe.interview.model.User;
import com.stripe.interview.service.UserService;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

public class UserControllerIntegrationTest {
  private static UserService userService;

  @BeforeClass
  public static void beforeClass() {
    System.out.println("---------------- iT GETS HERE ------------");
    userService = new UserService();
    new UserController(new UserService());
    awaitInitialization();
//    Main.main(null);
  }

  @AfterClass
  public static void afterClass() {
    Spark.stop();
  }

  @Test
  public void aNewUserShouldBeCreated() {
    TestResponse res = request("POST", "/users?name=john&email=john@foobar.com");
    Map<String, String> json = res.json();
    assertEquals(200, res.status);
    assertEquals("john", json.get("name"));
    assertEquals("john@foobar.com", json.get("email"));
    assertNotNull(json.get("id"));
  }

  @Before
  public void seUp(){
    // will run before all tests
    System.out.println("BEFORE TEST");
  }

  @Test
  public void aUserShouldBeReturned() {
//    User testUser = new User("test", "test@test.com");
    User testUser = userService.createUser("test", "test@test.com");
//    given(userService.getUser(testUser.id)).willReturn(testUser);
//    when(userService.getUser(testUser.id)).thenReturn(testUser);

    TestResponse res = request("GET", String.format("/users/%s", testUser.id));
    Map<String, String> json = res.json();
    assertEquals(200, res.status);
    assertEquals(testUser.name, json.get("name"));
    assertEquals(testUser.email, json.get("email"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void TestStream(){
    List<Integer> list = Arrays.asList(0, 1, 2, 1, 0, 4, 3, 2, 7, 1);
    HashMap<Integer, Integer> valCount = new HashMap<>();
//    list.stream().map(a -> new AbstractMap.SimpleEntry<>(a, 1)).reduce(new LinkedHashMap<Integer, Integer>(), (k, entry) -> {
//      k.put(entry.getKey(), (k, v) -> v == null ? 1 : v + 1);
//    });
//
    LinkedHashMap<Integer, Integer> x = list.stream().map(word -> new AbstractMap.SimpleEntry<>(word, 1))
        .reduce(new LinkedHashMap<Integer, Integer>(), (lmap, entry) -> {
          lmap.put(entry.getKey(), lmap.compute(entry.getKey(), (k, v) -> v == null ? 1 : (v + 1)));
          return lmap;
        }, (m1, m2) -> m1);

    x.entrySet().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    System.out.println("***************");
    Map<Integer, Integer> x2 = list.stream().map(word -> new AbstractMap.SimpleEntry<>(word, 1))
        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, e -> e.getValue(), (v1, v2) -> v1+v2));

    x.entrySet().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
  }

  private TestResponse request(String method, String path) {
    try {
      URL url = new URL("http://localhost:4567" + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);
      connection.setDoOutput(true);
      connection.connect();
      String body = IOUtils.toString(connection.getInputStream());
      return new TestResponse(connection.getResponseCode(), body);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Sending request failed: " + e.getMessage());
      return null;
    }
  }


  private static class TestResponse {

    public final String body;
    public final int status;

    public TestResponse(int status, String body) {
      this.status = status;
      this.body = body;
    }

    public HashMap<String,String> json() {
      return new Gson().fromJson(body, HashMap.class);
    }
  }
}
