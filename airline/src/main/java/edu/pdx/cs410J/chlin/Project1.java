package edu.pdx.cs410J.chlin;

import com.google.common.annotations.VisibleForTesting;
import com.sun.tools.jconsole.JConsoleContext;

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
    String[] splitDateTime = dateAndTime.split(" ");
    String date = splitDateTime[0];
    String time = splitDateTime[1];
    String[] splitDate = date.split("/");
    String[] splitTime = time.split(":");
    int MM = Integer.parseInt(splitDate[0]);
    int DD = Integer.parseInt(splitDate[1]);
    String YYYY = splitDate[2];
    int hh = Integer.parseInt(splitTime[0]);
    int mm = Integer.parseInt((splitTime[1]));

    // check date
    if (YYYY.length() != 4) {
      return false;
    }
    if (MM < 1 || 12 < MM) {
      return false;
    }
    // months with 31 days
    if (MM == 4 || MM == 6 || MM == 9 || MM == 11) {
      if (DD < 1 || 30 < DD)
        return false;
      // special case February only has 28 days;
    } else if (MM == 2) {
      if (DD < 1 || 28 < DD)
        return false;
      // the rest of months. 31 days
    } else {
      if (DD < 1 || 31 < DD) {
        return false;
      }
    }

    // check time
    if (hh < 0 || 23 < hh) {
      return false;
    }
    if (mm < 0 || 59 < mm) {
      return false;
    }

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

  static void printUsage() {
    System.err.println("usage: java -jar target/airline-2023.0.0.jar [options] <args>");
    System.err.println("  args order:");
    System.err.println("  airline flightNumber src departDate departTime dest arriveDate arriveTime");
  }

  static boolean isInt(String argument) {
    try {
      int i = Integer.parseInt(argument);
    } catch (NumberFormatException e){
      return false;
    }
    return true;
  }

  static boolean containsLetter(String argument) {
    for (int i = 0; i < argument.length(); i++) {
      if (Character.isLetter(argument.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  static boolean argsNoErrorAndMissing(String[] arguments) {
    // first check missing arguments
    // if the first argument is integer, the airline name is missing
    if (isInt(arguments[0])) {
      System.err.println("missing: airline name");
      return false;
      // if the second argument is null or not integer, flight number is missing
    } else if (arguments[1] == null || !isInt(arguments[1])) {
      System.err.println("missing: flight number");
      return false;
      // if the third argument is null or length > 3 (not airport code) or is integer, departure code is missing
    } else if (arguments[2] == null || arguments[2].length() > 3 || isInt(arguments[2])) {
      System.err.println("missing: departure airport code");
      return false;
      // if the 4th argument is null or contains letters in it, it is not a date, departure date is missing
    } else if (arguments[3] == null || containsLetter(arguments[3])) {
      System.err.println("missing: departure date");
      return false;
      // if the 5th argument is null or contains letters in it, it is not a time, departure time is missing
    } else if (arguments[4] == null || containsLetter(arguments[4])) {
      System.err.println("missing: departure time");
      return false;
      // if the 6th argument is null or length > 3 (not airport code) or is integer, departure code is missing
    } else if (arguments[5] == null || arguments[5].length() > 3 || isInt(arguments[2])) {
      System.err.println("missing: arrival airport code");
      return false;
      // if the 7th argument is null or contains letters in it, it is not a date, arrival date is missing
    } else if (arguments[6] == null || containsLetter(arguments[6])) {
      System.err.println("missing: arrival date");
      return false;
      // if the 8th argument is null or contains letters in it, it is not a time, departure time is missing
    } else if (arguments[7] == null || containsLetter(arguments[7])) {
      System.err.println("missing: arrival time");
      return false;
      // then check if the date time is valid
      // check if the departure date and time is valid
    } else if (!isValidDateAndTime(arguments[3] + " " + arguments[4])) {
      System.err.println("Err: departure date time is not valid");
      return false;
    } else if (!isValidDateAndTime(arguments[6] + " " + arguments[7])) {
      System.err.println("Err: arrival date time is not valid");
      return false;
    }


    return true;
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
        System.err.println("options:");
        System.err.println("  -print      Prints a description of the entered flight");
        System.err.println("  -README     Information about the project");
        return;
      }
      else {
        if (numberOfArguments < 8)
          arguments[numberOfArguments] = arg;
        numberOfArguments++;
      }
    }

    if (numberOfArguments == 0) {
      System.err.println("Missing command line arguments");
    }
    else if (numberOfArguments > 8) {
      System.err.println("Too many arguments");
      printUsage();
    }
    else if (argsNoErrorAndMissing(arguments)){
      if (print) {
        for (String arg : arguments) {
          System.out.println(arg);
        }
      }
    }

  }

}