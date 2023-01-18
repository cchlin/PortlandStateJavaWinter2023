package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.lang.Human;

import java.util.ArrayList;
                                                                                    
/**                                                                                 
 * This class is represents a <code>Student</code>.                                 
 */                                                                                 
public class Student extends Human {

  private ArrayList<String> classes;
  private double gpa;
  private String gender;
                                                                                    
  /**                                                                               
   * Creates a new <code>Student</code>                                             
   *                                                                                
   * @param name                                                                    
   *        The student's name                                                      
   * @param classes                                                                 
   *        The names of the classes the student is taking.  A student              
   *        may take zero or more classes.                                          
   * @param gpa                                                                     
   *        The student's grade point average                                       
   * @param gender                                                                  
   *        The student's gender ("male", "female", or "other", case insensitive)
   */                                                                               
  public Student(String name, ArrayList<String> classes, double gpa, String gender) {
    super(name);
    this.classes = classes;
    this.gpa = gpa;
    this.gender = gender;
  }

  /**                                                                               
   * All students say "This class is too much work"
   */
  @Override
  public String says() {
    return "This class is too much work";
  }
                                                                                    
  /**                                                                               
   * Returns a <code>String</code> that describes this                              
   * <code>Student</code>.                                                          
   */                                                                               
  public String toString() {
    int size = this.classes.size();
    String classesStr = (size == 1) ? "class" : "classes";
    if (size != 0) {
      classesStr += ": ";
      if (size == 1) {
        classesStr += this.classes.get(0);
      }
      else if (size == 2) {
        classesStr += this.classes.get(0) + " and " + this.classes.get(1);
      }
      else {
        for (int i = 0; i < size - 1; ++i) {
          classesStr += this.classes.get(i) + ", ";
        }
        classesStr += "and " + this.classes.get(size - 1);
      }
    }

    String pronounsAndSay = "They say \"";

    switch (this.gender) {
      case "male":
        pronounsAndSay = "He says \"";
        break;
      case "female":
        pronounsAndSay = "She says \"";
        break;
      default: break;
    }

    return this.name + " has a GPA of " + this.gpa + " and is taking " + size + " " + classesStr +
            ". " + pronounsAndSay + says() + "\".";

  }

  /**
   * verify if an argument is a number
   */
  private static boolean isNumber(String argument) {
    try {
      double arg = Double.parseDouble(argument);
    } catch (NumberFormatException ex) {
      return false;
    }
    return true;
  }

  /**
   * Main program that parses the command line, creates a
   * <code>Student</code>, and prints a description of the student to
   * standard out by invoking its <code>toString</code> method.
   */
  public static void main(String[] args) {
    String tempName;
    ArrayList<String> classList = new ArrayList<>();
    double tempGpa;
    String tempGender;

    if (args.length < 3) {
      System.err.println("Missing command line arguments");
      System.err.println("Usage: Name gender gpa [classA \"class B\" ... ]");
//      System.exit(1);
      return;
    }

    if (isNumber(args[0])) {
      System.err.println("Missing name and gender");
//      System.exit(1);
      return;
    }
    if (!isNumber(args[2])) {
      if (Character.isUpperCase(args[0].charAt((0)))) {
        System.err.println("Missing gender");
//        System.exit(1);
        return;
      } else {
        System.err.println("Missing name");
//        System.exit(1);
        return;
      }
    }

    if (Character.isUpperCase(args[1].charAt(0))) {
      System.err.println("gender should not be capitalized");
//      System.exit(1);
      return;
    }

    if (args.length > 3) {
      int size = args.length;
      boolean isPhrase = false;
      String phrase = "";
      for (int i = 3; i < size; ++i) {
        if (args[i].charAt(0) == '\"') {
          isPhrase = true;
        }
        if (args[i].charAt(args[i].length() - 1) == '\"') {
          classList.add(phrase.substring(1, phrase.length() - 1));
          isPhrase = false;
        }
        if (isPhrase) {
          phrase += args[i];
        }
        else {
          classList.add(args[i]);
        }
      }
    }

    tempName = args[0];
    tempGender = args[1];
    tempGpa = Double.parseDouble(args[2]);

    Student student = new Student(tempName, classList, tempGpa, tempGender);
    System.out.println(student);

  }
}