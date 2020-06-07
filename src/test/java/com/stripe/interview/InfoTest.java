package com.stripe.interview;


import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.junit.Assert;
import org.junit.Test;
/*
A dummy object is passed around but never used, i.e., its methods are never called. Such an object can for example be used to fill the parameter list of a method.

Fake objects have working implementations, but are usually simplified. For example, a memory database is used for testing and not a SQL based database.

A stub class is an partial implementation for an interface or class with the purpose of using an instance of this stub class during testing. Stubs usually don’t respond to anything outside what’s programmed in for the test. Stubs may also record information about calls.

A mock object is a dummy implementation for an interface or a class in which you define the output of certain method calls. Mock objects are configured to perform a certain behavior during a test. They typically record the interaction with the system and tests can validate that.
 */
public class InfoTest {

  @Mock
  Info.MyDatabase databaseMock;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testQuery()  {
    Info t  = new Info(databaseMock);
//    boolean check = t.query("* from t");
//    assertTrue(check);
//    verify(databaseMock).query("* from t");
  }

}
