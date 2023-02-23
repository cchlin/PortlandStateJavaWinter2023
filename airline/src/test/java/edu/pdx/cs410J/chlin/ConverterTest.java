package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class ConverterTest extends InvokeMainTestCase {
    @Test
    void testCheckInvalidFileName() {
       String fileName = "filetxt";
       assertFalse(Converter.checkFileNameFormat(fileName));
    }

    @Test
    void testCheckValidFileName() {
        String fileName = "file.txt";
        assertTrue(Converter.checkFileNameFormat(fileName));
    }

    @Test
    void testArgumentsNumbers() {
        MainMethodResult result = invokeMain(Converter.class, "text.txt");
        assertThat(result.getTextWrittenToStandardError(), containsString("Command line argument should be 2 and only 2\nusage: java -cp target/airline-2023.0.0.jar edu.pdx.cs410J.<login-id>.Converter textFile xmlFile\n"));
    }

    @Test
    void testInvalidTextFileName() {
        MainMethodResult result = invokeMain(Converter.class, "texttxt", "valid.xml");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid text file name: texttxt"));
    }

    @Test
    void testInalidXmlFileName() {
        MainMethodResult result = invokeMain(Converter.class, "text.txt", "validxml");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid xml file name: validxml"));
    }

    @Test
    void testReadNotValidFile() throws FileNotFoundException {
        File file = new File("notValid");
        assertThrows(ParserException.class, () -> Converter.readFromText(file)) ;
    }

    @Test
    void testReadValidFile() throws FileNotFoundException, ParserException {
        File file = new File(getClass().getResource("valid-airline.txt").getFile());
        Airline airline = Converter.readFromText(file);
        assertThat(airline.getName(), equalTo("Test Airline"));
    }

    @Test
    void testXmlWriteInvalid() {
        File file = new File("notValid");
        assertThrows(IOException.class, () -> Converter.xmlWrite(file, new Airline("aa")));
    }

    @Test
    void testXmlWriteValid(@TempDir File tempDir) throws IOException, ParserException {
        Airline airline = new Airline("Test Airline");
        Flight f1 = new Flight(1437, "BJX", "9/25/2020 5:0 PM", "CMN", "9/26/2020 3:56 AM");
        Flight f2 = new Flight(7865, "JNB", "5/15/2020 7:24 AM", "XIY", "5/16/2020 9:7 AM");
        airline.addFlight(f1);
        airline.addFlight(f2);
        File tempFile = new File(tempDir, "tempXml.xml");
        Converter.xmlWrite(tempFile, airline);
        File tempXmlFile = new File(tempDir, "tempXml.xml");
        InputStream is = new FileInputStream(tempXmlFile);
        XmlParser xp = new XmlParser(is);
        Airline a2 = xp.parse();
        assertThat(a2.getName(), equalTo("Test Airline"));
    }
}
