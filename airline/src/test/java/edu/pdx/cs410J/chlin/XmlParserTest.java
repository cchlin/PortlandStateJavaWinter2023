package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlParserTest {

    @Test
    void testNullConstructorArgument() {
        assertThrows(IllegalArgumentException.class, () -> new XmlParser(null));
    }

    Document getValidDomDocument() throws ParserConfigurationException, IOException, SAXException {
        AirlineXmlHelper helper = new AirlineXmlHelper();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(helper);
        builder.setEntityResolver(helper);


        return builder.parse(this.getClass().getResourceAsStream("valid-airline.xml"));
    }

    @Test
    void testXmlParserNotNull() throws ParserConfigurationException, IOException, SAXException {
        InputStream input = getClass().getResourceAsStream("valid-airline.xml");
        XmlParser xp = new XmlParser(input);

        assertNotNull(xp);
    }

    @Test
    void testParseMethodReturnAirlineNotNull() throws ParserConfigurationException, IOException, SAXException, ParserException {
        InputStream input = getClass().getResourceAsStream("valid-airline.xml");
        XmlParser xp = new XmlParser(input);

        Airline airline = xp.parse();

        assertNotNull(airline);

    }

    @Test
    void testParseAirlineNameValid() throws ParserConfigurationException, IOException, SAXException, ParserException {
        InputStream input = getClass().getResourceAsStream("valid-airline.xml");
        XmlParser xp = new XmlParser(input);
        Airline airline = xp.parse();

        assertThat(airline.getName(), equalTo("Valid Airlines"));
    }

    @Test
    void testParseMethodAddsFlights() throws ParserConfigurationException, IOException, SAXException, ParserException{
        InputStream input = getClass().getResourceAsStream("valid-airline.xml");
        XmlParser xp = new XmlParser(input);
       Airline airline = xp.parse();

       Airline a = new Airline("Valid Airlines");
       Flight f1 = new Flight(1437, "BJX", "9/25/2020 5:0 PM", "CMN", "9/26/2020 3:56 AM");
       Flight f2 = new Flight(7865, "JNB", "5/15/2020 7:24 AM", "XIY", "5/16/2020 9:7 AM");
       a.addFlight(f1);
       a.addFlight(f2);

       assertThat(airline.getFlights().size(), equalTo(a.getFlights().size()));
    }




}
