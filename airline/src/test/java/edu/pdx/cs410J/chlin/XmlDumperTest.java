package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlDumperTest {

    @Test
    void testCreateDocumentMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        XmlDumper xd = new XmlDumper();
        Method method = XmlDumper.class.getDeclaredMethod("createDocument");
        method.setAccessible(true);
        Document document = (Document)method.invoke(xd);

        assertThat(document.getDocumentElement().getNodeName(), equalTo("airline"));
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

    Airline getAirline() {
        Airline a = new Airline("Valid Airlines");
        Flight f1 = new Flight(1437, "BJX", "9/25/2020 5:0 PM", "CMN", "9/26/2020 3:56 AM");
        Flight f2 = new Flight(7865, "JNB", "5/15/2020 7:24 AM", "XIY", "5/16/2020 9:7 AM");
        a.addFlight(f1);
        a.addFlight(f2);
        return a;
    }

    Document getDocFromDumper() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Airline airline = getAirline();
        XmlDumper xd = new XmlDumper();
        Method method = XmlDumper.class.getDeclaredMethod("createDOMTree", Airline.class);
        method.setAccessible(true);
        Document document = (Document)method.invoke(xd, airline);

        return document;
    }

    @Test
    void testCreateDOMTree() throws ParserConfigurationException, IOException, SAXException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Document resourceDocument = getValidDomDocument();
        Document dumperDocument = getDocFromDumper();

        Element resourceRoot = resourceDocument.getDocumentElement();
        Element dumperRoot = dumperDocument.getDocumentElement();
        assertThat(dumperRoot.getNodeName(), equalTo(resourceRoot.getNodeName()));
    }

    @Test
    void testCreateDOMTreeNoFlights() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Airline airline = new Airline("Valid Airlines");
        XmlDumper xd = new XmlDumper();
        assertThrows(IOException.class, () -> xd.dump(airline));
    }

    @Test
    void testDump(@TempDir File tempDir) throws IOException, ParserException {
       File file = new File(tempDir, "test.xml");
       XmlDumper xd = new XmlDumper(new FileWriter(file));
       Airline airline = getAirline();
       xd.dump(airline);

        InputStream is = new FileInputStream(file);
       XmlParser xp = new XmlParser(is);

       Airline a = xp.parse();
       assertThat(a.getName(), equalTo(airline.getName()));

    }
}
