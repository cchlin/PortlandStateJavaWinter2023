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

    // array to store arguments for later use
    String[] arguments = new String[8];
    // print switch indicator
    boolean print = false;
    // keeps track of how many arguments is entered by user (exclude options
    int numberOfArguments = 0;

    // process command line arguments
    for (String arg : args) {
      if (arg.equals("-readme") || arg.equals("-README")) {
        printReadMe();
        return;
      }
      else if (arg.equals("-print") || arg.equals("-PRINT"))
        print = true;
      // all other options or options with typo go here
      else if (arg.charAt(0) == '-') {
        System.err.println("Error: unknown option: " + arg);
        System.out.println("Options:");
        System.out.println("  -print      Prints a description of the entered flight");
        System.out.println("  -README     Information about the project");
        return;
      }
      else {
        if (numberOfArguments < 8)
          arguments[numberOfArguments] = arg;
        numberOfArguments++;
      }
    }

    if (numberOfArguments < 8) {
      System.err.println("Missing command line arguments");
    }
    else if (numberOfArguments > 8)
      System.err.println("Too many arguments");
    else {
      if (print) {
        for (String arg : arguments) {
          System.out.println(arg);
        }
      }
    }

  }

}