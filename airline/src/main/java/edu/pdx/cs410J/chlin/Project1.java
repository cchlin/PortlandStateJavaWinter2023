package edu.pdx.cs410J.chlin;

import com.google.common.annotations.VisibleForTesting;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  public static void main(String[] args) {
    Flight flight = new Flight(42, "PDX", "3/15/2023 10:39", "SEA", "3/15/2023 11:39");  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    System.err.println("Missing command line arguments");
    for (String arg : args) {
      System.out.println(arg);
    }
  }

}