package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;

import java.io.*;

public class Converter {

    static void xmlWrite(File file, Airline airline) throws IOException {
        try {
            Writer writer = new FileWriter(file);
            XmlDumper xd = new XmlDumper(writer);
            xd.dump(airline);
        } catch (IOException ex) {
            throw new IOException();
        }
    }

    static Airline readFromText(File file) throws FileNotFoundException, ParserException {
        Airline airline = null;
       try {
           Reader reader = new FileReader(file);
           TextParser tp = new TextParser(reader);
           airline = tp.parse();
       } catch (FileNotFoundException ex) {
           throw new FileNotFoundException();
       } catch (ParserException ex) {
           throw new ParserException("An error occurred while parsing file");
       }
       return airline;
    }

    static boolean checkFileNameFormat(String fileName) {
       String[] splitFileName = fileName.split("\\.") ;
       if (splitFileName.length == 1) {
           return false;
       }
       return true;
    }
    public static void main(String[] args) throws ParserException, IOException {
        if (args.length != 2) {
            System.err.println("Command line argument should be 2 and only 2");
            System.err.println("usage: java -cp target/airline-2023.0.0.jar edu.pdx.cs410J.<login-id>.Converter textFile xmlFile");
            return;
        }

        String textFile = args[0];
        String xmlFile = args[1];

        if (!checkFileNameFormat(textFile)) {
            System.err.println("Invalid text file name: " + textFile);
            return;
        }
        if (!checkFileNameFormat(xmlFile)) {
            System.err.println("Invalid xml file name: " + xmlFile);
            return;
        }

        File tFile = new File(textFile);
        Airline airline = readFromText(tFile);
        File xFile = new File(xmlFile);
        xmlWrite(xFile, airline);

    }
}
