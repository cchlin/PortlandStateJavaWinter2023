package edu.pdx.cs410J.chlin;

import com.google.common.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  static void printReadMe() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      System.out.println(line);
    }
  }

  public static void main(String[] args) throws IOException {
    Flight flight = new Flight(42, "PDX", "3/15/2023 10:39", "SEA", "3/15/2023 11:39");  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    System.err.println("Missing command line arguments");

    String[] arguments = new String[8];
    boolean readMe = false;
    boolean print = false;


    int numberOfArguments = 0;
    for (String arg : args) {
      if (numberOfArguments >= 8)
        break;
      else if (arg.equals("-readme") || arg.equals("-README"))
        readMe = true;
      else if (arg.equals("-print") || arg.equals("-PRINT"))
        print = true;
      else {
        arguments[numberOfArguments] = arg;
        numberOfArguments++;
      }
    }

    if (readMe) {
      printReadMe();
    }
    else if (numberOfArguments >= 8) {
      System.out.println("Too many arguments");
    }
    else {
      for (String arg : arguments) {
        System.out.println(arg);
      }
    }

  }

}