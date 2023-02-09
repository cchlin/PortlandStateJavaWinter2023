package edu.pdx.cs410J.chlin;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.ArrayList;

/**
 * The main class for the CS410J airline Project
 */
public class Project3 {

  /**
   * methods that checks if the date and time is valid
   * Example:
   * 3/15/22 -> false, year should be 4 digits
   * 4/31/2023 -> false, only 30 days in April
   * 24:00 -> false, should be 00:00
   * AM am and Pm pm should all be ok but ap should not
   * @param dateAndTime date and time string in MM/DD/YYYY hh:mm aa
   * @return true if the date and time are valid, false otherwise.
   */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    String[] splitDateTime = dateAndTime.split(" ");
    String date = splitDateTime[0];
    String time = splitDateTime[1];
    String marker = splitDateTime[2];
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

    // check marker
    if (!marker.equalsIgnoreCase("AM") && !marker.equalsIgnoreCase("PM")) {
      return false;
    }

    return true;
  }

  /**
   * Read the content of README.txt and return the result
   * @return content of README.txt file
   * @throws IOException
   */
  static String printReadMe() throws IOException {
    String result = "";
    try (
      InputStream readme = Project3.class.getResourceAsStream("README.txt")
    ) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      result += line + "\n";
      while ((line = reader.readLine()) != null) {
        result += line + "\n";
      }
    }
    return result;
  }

  /**
   * display the usage information of this program
   */
  static void printUsage() {
    System.err.println("usage: java -jar target/airline-2023.0.0.jar [options] <args>");
    System.err.println("  args order:");
    System.err.println("  airline flightNumber src departDate departTime AM/PM dest arriveDate arriveTime AM/PM");
  }

  /**
   * checks if the argument passed in is integer
   * @param argument the argument from the command line in string format
   * @return true if the string argument is integer number, false otherwise
   */
  static boolean isInt(String argument) {
    try {
      int i = Integer.parseInt(argument);
    } catch (NumberFormatException e){
      return false;
    }
    return true;
  }

  /**
   * checks if the argument contains english letter in it
   * @param argument the string argument that is passed from command line
   * @return true if the argument contains letter (example: 12a34), false otherwise
   */
  static boolean containsLetter(String argument) {
    for (int i = 0; i < argument.length(); i++) {
      if (Character.isLetter(argument.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * checks missing argument and display to user what is missing
   * also calls isValidDateAndTime to check the date time value
   * @param arguments An array of command line arguments
   * @return true if arguments are all correct and if and argument is wrong, returns false
   */
  static boolean argsNoErrorAndMissing(String[] arguments) {
    // first check missing arguments
    // if the first argument is integer, the airline name is missing
    if (isInt(arguments[0])) {
      System.err.println("missing: airline name");
      return false;
      // if the second argument is null or not integer, flight number is missing
    } else if (arguments[1] == null) {
      System.err.println("missing: flight number");
      return false;
    } else if (!isInt(arguments[1])) {
      System.err.println("Flight number should be only numbers");
      return false;
      // if the third argument is null or length > 3 (not airport code) or is integer, departure code is missing
    } else if (arguments[2] == null) {
      System.err.println("missing: departure airport code");
      return false;
    } else if (arguments[2].length() != 3) {
      System.err.println("Departure airport code should be 3 letters only");
      return false;
    } else if (isInt(arguments[2])) {
      System.err.println("Departure airport code should be letters only");
      return false;
      // if the 4th argument is null or contains letters in it, it is not a date, departure date is missing
    } else if (arguments[3] == null || containsLetter(arguments[3])) {
      System.err.println("missing: departure date");
      return false;
      // if the 5th argument is null or contains letters in it, it is not a time, departure time is missing
    } else if (arguments[4] == null || containsLetter(arguments[4])) {
      System.err.println("missing: departure time");
      return false;
    } else if (arguments[5] == null) {
      System.err.println("missing: departure am/pm marker");
      return false;
    } else if (isInt(arguments[5])) {
      System.err.println("The departure am/pm marker should not include integer");
      return false;
    } else if (arguments[5].length() != 2) {
      System.err.println("The departure am/pm marker should be 2 letters only");
      return false;
      // if the 6th argument is null or length > 3 (not airport code) or is integer, departure code is missing
    } else if (arguments[6] == null) {
      System.err.println("missing: arrival airport code");
      return false;
    } else if (arguments[6].length() != 3) {
      System.err.println("Arrival airport code should be 3 letters only");
      return false;
    } else if (isInt(arguments[6])) {
      System.err.println("Arrival airport code should be letters only");
      return false;
      // if the 7th argument is null or contains letters in it, it is not a date, arrival date is missing
    } else if (arguments[7] == null || containsLetter(arguments[7])) {
      System.err.println("missing: arrival date");
      return false;
      // if the 8th argument is null or contains letters in it, it is not a time, departure time is missing
    } else if (arguments[8] == null || containsLetter(arguments[8])) {
      System.err.println("missing: arrival time");
      return false;
    } else if (arguments[9] == null) {
      System.err.println("missing: arrival am/pm marker");
      return false;
    } else if (isInt(arguments[9])) {
      System.err.println("The arrival am/pm marker should not include integer");
      return false;
    } else if (arguments[9].length() != 2) {
      System.err.println("The arrival am/pm marker should be 2 letters only");
      return false;
      // then check if the date time is valid
      // check if the departure date and time is valid
    } else if (!isValidDateAndTime(arguments[3] + " " + arguments[4] + " " + arguments[5])) {
      System.err.println("Err: departure date time is not valid -> " + arguments[3] + " " + arguments[4] + " " + arguments[5]);
      return false;
    } else if (!isValidDateAndTime(arguments[7] + " " + arguments[8] + " " + arguments[9])) {
      System.err.println("Err: arrival date time is not valid -> " + arguments[7] + " " + arguments[8] + " " + arguments[9]);
      return false;
    }

    return true;
  }

  static Airline readFromFile(File file, String airlineName) {
    try {
    // if file exists, retrieve the airline
      if (file.exists()) {
        Reader reader = new FileReader(file);
        TextParser textparser = new TextParser(reader);
        return textparser.parse();
        // if the file does not exist, create one and return null airline
      } else {
        // get the path of the file object passed in
        File path  = file.getParentFile();
        // check if the file contains path
        if (path != null) {
          // if the path does not exist, create the path
          if (!path.exists() && !path.mkdirs()) {
            throw new IOException("Cannot create directory: " + path);
          }
        }
        if (!file.createNewFile()) {
          throw new IOException("Cannot create file: " + file);
        }
      }
    } catch (IOException | ParserException ex) {
      System.err.println("** " + ex);
    }
    // if file does not exist, return a new airline instead
    return new Airline(airlineName);
  }

  static void writeToFile(File file, String airlineName, Flight flight) {
    // retrieve the airline, from file
    // if the file exist, the airline is the one in the file
    // otherwise it is a newly created airline
    Airline airline = readFromFile(file, airlineName);
    // if name of the airline passed in does not match name of the airline
    // returned by readFromFile(return by textParser)
    if (!airline.getName().equals(airlineName)) {
      System.err.println("Airline not found in this file");
      return;
    }
    try {
      airline.addFlight(flight);
      Writer writer = new FileWriter(file);
      TextDumper textDumper = new TextDumper(writer);
      textDumper.dump(airline);
    } catch (IOException ex) {
      System.err.println("** " + ex);
    }
  }

  static boolean validFileNameFormat(String fileName) {
    String[] splitFileName = fileName.split("\\.");
    if (splitFileName.length == 1) {
      System.err.println("** Bad file name: " + fileName);
      return false;
    }
    return true;
  }

  public static void main(String[] args) throws IOException {

    // array to store arguments for later use
    String[] arguments = new String[10];
    // print switch indicator
    boolean print = false;
    // keeps track of how many arguments is entered by user (exclude options
    int numberOfArguments = 0;
    // if enabled write to file
    boolean textFileEnabled = false;
    // indicate if the previous argument is -textFile
    boolean textFile = false;
    String file = "";
    // if enabled write to pretty file
    boolean prettyEnabled = false;
    // indicate if the previous argument is -pretty
    boolean pretty = false;
    boolean prettyConsole = false;
    String prettyFile = "";

    int size = args.length;
    for (String arg : args) {
      // process command line arguments
        if (arg.equalsIgnoreCase("-README")) {
          System.out.println(printReadMe());
          return;
        } else if (arg.equalsIgnoreCase("-PRINT")) {
          print = true;
        }
        // all other options or options with typo go here
        else if (arg.equalsIgnoreCase("-TEXTFILE")) {
          textFile = true;
          textFileEnabled = true;
        } else if (arg.equalsIgnoreCase("-PRETTY")) {
          pretty = true;
          prettyEnabled = true;
        } else if (!pretty && arg.charAt(0) == '-') {
          if (prettyEnabled && prettyFile.length() != 0 && !prettyConsole) {
            prettyConsole = true;
          } else {
            System.err.println("Error: unknown option: " + arg);
            System.err.println("options:");
            System.err.println("  -print           Prints a description of the entered flight");
            System.err.println("  -README          Information about the project");
            System.err.println("  -textFile file   Read/write the airline info to the file");
            System.err.println("  -pretty file/-   write the pretty format to file/command line");
            return;
          }
        } else {
          if (textFile) {
            if (!validFileNameFormat(arg)) {
              return;
            }
            file = arg;
            textFile = false;
          } else if (pretty) {
            if (arg.compareTo("-") != 0) {
              if (!validFileNameFormat(arg)) {
                return;
              }
            }
            if (arg.compareTo("-") == 0) {
              prettyConsole = true;
            }
            prettyFile = arg;
            pretty = false;
          } else {
            if (numberOfArguments < 10)
              arguments[numberOfArguments] = arg;
            numberOfArguments++;
          }
        }
    }

    // message to user
    // no arguments
    if (numberOfArguments == 0) {
      System.err.println("Missing command line arguments");
      printUsage();
      return;
    }
    // too many arguments
    if (numberOfArguments > 10) {
      System.err.println("Too many arguments");
      printUsage();
      return;
    }
    // missing and error checking
    if (!argsNoErrorAndMissing(arguments)) {
      return;
    }


    // if everything is good, goes here and start to add flight and print if the
    // print option is entered
    String airlineName = arguments[0];
    int flightNumber = Integer.parseInt(arguments[1]);
    String src = arguments[2];
    String depart = arguments[3] + " " + arguments[4] + " " + arguments[5];
    String dest = arguments[6];
    String arrive = arguments[7] + " " + arguments[8] + " " + arguments[9];

    Airline airline = new Airline(airlineName);
    Flight flight = new Flight(flightNumber, src, depart, dest, arrive);

    if (print) {
      System.out.println(flight);
    }

    // write to file
    if (textFileEnabled) {
      File fileArg = new File(file);
      writeToFile(fileArg, airlineName, flight);
    }

    if (prettyEnabled) {
      if (textFileEnabled) {
        File tFile = new File(file);
        airline = readFromFile(tFile, airlineName);
      }
      airline.addFlight(flight);
      if (prettyConsole) {
          PrettyPrinter pp = new PrettyPrinter();
          ArrayList<String> output = pp.formatOutput(airline);
          output.forEach(System.out::println);
      }
       if (prettyFile.charAt(0) != 0) {
        File pFile = new File(prettyFile);
        try {
          Writer writer = new FileWriter(pFile);
          PrettyPrinter pp = new PrettyPrinter(writer);
          pp.dump(airline);
        } catch (IOException ex) {
          System.err.println("** " + ex);
        }
      }
    }
  }
  }