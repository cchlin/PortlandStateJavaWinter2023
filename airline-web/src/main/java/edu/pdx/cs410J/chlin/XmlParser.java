package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XmlParser implements AirlineParser<Airline> {
    private Document document;

    public XmlParser(InputStream is) {

        try {
            AirlineXmlHelper helper = new AirlineXmlHelper();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);
            document = builder.parse(is);

        } catch (ParserConfigurationException ex) {
            System.out.println("An error occurred while initializing the DocumentBuilder");
        } catch (SAXException ex) {
            System.out.println("An error occurred while parsing XML document. ");
        } catch (IOException ex) {
            System.out.println("An error occurred while reading the input source");
        }
    }

    /**
     * this method parse the xml file content and use them to create the
     * airline and its flights
     * @return a airline object
     * @throws ParserException
     */
    @Override
    public Airline parse() throws ParserException {
        Airline airline = null;
        Element root = document.getDocumentElement();

        String rootName = root.getTagName();
        if (rootName.equals("airline")) {
            NodeList entries = root.getChildNodes();
            int entryLength = entries.getLength();
            for (int i = 0; i < entryLength; i++) {
                Node node = entries.item(i);
                if (!(node instanceof Element)) {
                    continue;
                }
                Element entry = (Element) node;
                String nodeName = entry.getNodeName();
                switch (nodeName) {
                    case "name": {
                        String airlineName = entry.getTextContent();
                        airline = new Airline(airlineName);
                        break;
                    }
                    case "flight": {
                        airline.addFlight(parseFlight(entry));
                        break;
                    }
                }
            }
        }

        return airline;
    }

    /**
     * this method takes the flight element and proccess its children elements
     * and use them to create a new flight that is to be returned to the calling
     * method so the airline can the flight
     * @param flightNode the flight dom element
     * @return a flight object
     */
    private Flight parseFlight(Element flightNode) {
        int flightNumber = 0;
        String source = "";
        String departureTime = "";
        String destination = "";
        String arrivalTime = "";

        NodeList entries = flightNode.getChildNodes();
        int entryLength = entries.getLength();
        for (int i = 0; i < entryLength; i++) {
            Node node = entries.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            Element entry = (Element) node;
            String nodeName = entry.getNodeName();
            switch (nodeName) {
                case "number": {
                   flightNumber = Integer.parseInt(entry.getTextContent());
                   break;
                }
                case "src": {
                    source = entry.getTextContent();
                    break;
                }
                case "depart": {
                    departureTime = formatTime(entry);
                    break;
                }
                case "dest": {
                    destination = entry.getTextContent();
                    break;
                }
                case "arrive": {
                    arrivalTime = formatTime(entry);
                    break;
                }
            }
        }

        return new Flight(flightNumber, source, departureTime, destination, arrivalTime);
    }

    /**
     * this method takes an element of the flight element's child if the element is
     * depart or arrive to process the date and time children. It format the date
     * and time to string and reformat the string from 24hr format in xml file to
     * 12 hour format string. Returns the 12 hour format string so the calling
     * method can use it to create a flight
     * @param timeNode the dom element. a depart or a arrive
     * @return a 12hr string time format that follows the Flight's field
     */
    private String formatTime(Element timeNode) {
        StringBuilder sb = new StringBuilder();
        String month = "";
        String day = "";
        String year = "";
        String hour = "";
        String minute = "";

        NodeList entries = timeNode.getChildNodes();
        int entryLength = entries.getLength();
        for (int i = 0; i < entryLength; i++) {
            Node node = entries.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            Element entry = (Element) node;
            String nodeName = entry.getNodeName();
            switch (nodeName) {
                case "date": {
                    month = entry.getAttribute("month");
                    day = entry.getAttribute("day");
                    year = entry.getAttribute("year");
                    break;
                }
                case "time": {
                    hour = entry.getAttribute("hour");
                    minute = entry.getAttribute("minute");
                    break;
                }
            }
        }

        sb.append(month).append("/").append(day).append("/").append(year)
                .append(" ")
                .append(hour).append(":").append(minute);

        String dateTime = sb.toString().trim();

        Date date = null;
        try {
            SimpleDateFormat xmlFormat = new SimpleDateFormat("MM/dd/yyyy H:mm");
            date = xmlFormat.parse(dateTime);
        } catch (ParseException ex) {
            System.err.println("** Bad time: " + dateTime);
        }

        SimpleDateFormat flightFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        dateTime = flightFormat.format(date);

        return dateTime;
    }
}
