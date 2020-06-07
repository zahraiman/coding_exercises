package com.stripe.interview;

import com.google.common.html.HtmlEscapers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.interview.controller.AcccountController;
import com.stripe.interview.controller.UserController;
import com.stripe.interview.model.Account;
import com.stripe.interview.service.AccountService;
import com.stripe.interview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	Logger logger =  LoggerFactory.getLogger(Main.class);
	public static void main(String... args) {
		System.out.println("Hello worldyy!");

//		// Spark will start an embedded Jetty server that listens on Port 4567
//		Injector injector = Guice.createInjector(new UserModule());
//		UserController userController = injector.getInstance(UserController.class);
		gsonBuilder();
		new UserController(new UserService());
		new AcccountController(new AccountService());

	}

	public static void gsonBuilder(){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Account newAcc = new Account("type1", "company1");
		String jsonString = gson.toJson(newAcc);
		System.out.println(jsonString);
		Account accObject = gson.fromJson(jsonString, Account.class);
		System.out.println(accObject.toString());
		assert(accObject.getBusinessType().equals(newAcc.getBusinessType()));

	}

	public static String useGuavaForSomeReason(String input) {
		return HtmlEscapers.htmlEscaper().escape(input);
	}

	public void ZahraTest(){
		logger.info("************************* zahra test ************************* ");

	}
}
