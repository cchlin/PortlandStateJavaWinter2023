package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class XmlDumper implements AirlineDumper<Airline> {

    private String SYSTEM_ID = AirlineXmlHelper.SYSTEM_ID;
    private String PUBLIC_ID = AirlineXmlHelper.PUBLIC_ID;
    private Document document;
    private Writer writer;

    public XmlDumper() {
        createDocument();
        this.writer= null;
    }

    /**
     * the constructor of the XmlDumper
     * @param writer the writer that specified the file
     */
    public XmlDumper(Writer writer) {
        createDocument();
        this.writer = writer;
    }

    /**
     * The method that writes the xml file
     * @param airline the airline that is to dump
     * @throws IOException
     */
    @Override
    public void dump(Airline airline) throws IOException {

//        try {
            createDOMTree(airline);
//        } catch (IOException ex) {
//            throw new IOException("** empty flights");
//        }

        try {
            Source src = new DOMSource(document);
            Result res = new StreamResult(writer);

            TransformerFactory xFactory = TransformerFactory.newInstance();
            Transformer xform = xFactory.newTransformer();
            xform.setOutputProperty(OutputKeys.INDENT, "yes");
            xform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, SYSTEM_ID);
            xform.transform(src, res);
        } catch (TransformerConfigurationException ex) {
            System.out.println("** An error occurred while configuring the transformer for writing the DOM tree to output");
        } catch (TransformerException ex) {
            System.out.println("** An error occurred while transforming the DOM tree to output");
//            System.out.println(ex);
            ex.printStackTrace();
        }

    }

    /**
     * This method creates the DOM tree that it reads the airline and its flights
     * and create elements and appends them to the root document
     * @param airline the airline that is to dump
     * @return the document that is to be used to write out
     * @throws IOException If there's no flights the exception will be thrown
     */
    private void createDOMTree(Airline airline) throws IOException {

        Element root = document.getDocumentElement();
        String airlineName = airline.getName();
        Collection<Flight> flights = airline.getFlights();

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(airlineName));
        root.appendChild(name);

        if (!flights.isEmpty()) {
            for (Flight f : flights) {
                Element flight = document.createElement("flight");

                String flightNumber = Integer.toString(f.getNumber());
                Element number = document.createElement("number");
                number.appendChild(document.createTextNode(flightNumber));
                flight.appendChild(number);

                String source = f.getSource();
                Element src = document.createElement("src");
                src.appendChild(document.createTextNode(source));
                flight.appendChild(src);

                Date departDateTime = f.getDeparture();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departDateTime);
                String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
                String year = Integer.toString(calendar.get(Calendar.YEAR));
                String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                String minute = Integer.toString(calendar.get(Calendar.MINUTE));
                Element depart = document.createElement("depart");
                Element departDate = document.createElement("date");
                departDate.setAttribute("day", day);
                departDate.setAttribute("month", month);
                departDate.setAttribute("year", year);
                depart.appendChild(departDate);
                Element departTime = document.createElement("time");
                departTime.setAttribute("hour", hour);
                departTime.setAttribute("minute", minute);
                depart.appendChild(departTime);
                flight.appendChild(depart);

                String destination = f.getDestination();
                Element dest = document.createElement("dest");
                dest.appendChild(document.createTextNode(destination));
                flight.appendChild(dest);

                Date arriveDateTime = f.getArrival();
                calendar.setTime(arriveDateTime);
                day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
                year = Integer.toString(calendar.get(Calendar.YEAR));
                hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                minute = Integer.toString(calendar.get(Calendar.MINUTE));
                Element arrive = document.createElement("arrive");
                Element arriveDate = document.createElement("date");
                arriveDate.setAttribute("day", day);
                arriveDate.setAttribute("month", month);
                arriveDate.setAttribute("year", year);
                arrive.appendChild(arriveDate);
                Element arriveTime = document.createElement("time");
                arriveTime.setAttribute("hour", hour);
                arriveTime.setAttribute("minute", minute);
                arrive.appendChild(arriveTime);
                flight.appendChild(arrive);

                root.appendChild(flight);
            }
        }
//        } else {
//            throw new IOException("Empty flights");
//        }


//        return document;
    }

    /**
     * This method creates a document for the dumper's field and so later
     * other methods in the dumper can work on the document
     * @return a document that is to output
     */
    private void createDocument() {
        document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            DOMImplementation dom = builder.getDOMImplementation();
            DocumentType docType = dom.createDocumentType("airline", PUBLIC_ID, SYSTEM_ID);
            document = dom.createDocument(null, "airline", docType);
        } catch (ParserConfigurationException ex) {
            System.out.println("An error occurred while initializing the DocumentBuilder");
        }

    }
}
