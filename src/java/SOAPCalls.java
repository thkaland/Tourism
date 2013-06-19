//package testsoap;

import java.io.ByteArrayInputStream;
import org.apache.commons.lang3.StringEscapeUtils;
import java.io.StringWriter;
import java.io.Writer;
import org.xml.sax.InputSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.SortedSet;
import java.util.TreeSet;

public class SOAPCalls {

    /**
     * Starting point for the SAAJ - SOAP Client Testing
     */
    public static String GetCurrency(String country) throws Exception {
        String ret;
        int index;
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://83.212.100.158:8080/ode/processes/c2c";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequestCurrency(country), url);

        // Process the SOAP Response
        ret = processSOAPResponse(soapResponse);
        index = ret.lastIndexOf("tps\">");
        index += 5;
        ret = ret.substring(index);
        ret = ret.substring(0, ret.indexOf("<"));
        soapConnection.close();
        return ret;
    }

    public static String GetWeatherAndCurrency(String fromCountry, String toCountry, String capital, String toCity) throws Exception {
        String ret;
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://83.212.100.158:8080/ode/processes/finalInvocation";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequestCountriesCurrency(fromCountry, toCountry, capital, toCity), url);

        // Process the SOAP Response
        ret = processSOAPResponse(soapResponse);
        ret = extractWeather(ret);
        soapConnection.close();
        return ret;
    }

    public static String extractCities(String xml) {
        SortedSet<String> countries = new TreeSet<String>();
        String ret = "";
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("NewDataSet").item(0).getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    countries.add(eElement.getElementsByTagName("City").item(0).getTextContent());
                }
            }
            System.out.println("<select name=\"cities\">");
            for (int temp = 0; temp < countries.size(); temp++) {
                ret += ("<option value=\"" + countries.toArray()[temp] + "\">" + StringEscapeUtils.escapeHtml4((String) countries.toArray()[temp]) + "</option>\n");
            }
            ret += ("</select>\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String extractCountries(String xml) {
        SortedSet<String> countries = new TreeSet<String>();
        String ret = "";
        int index;
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("NewDataSet").item(0).getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    countries.add(eElement.getElementsByTagName("Country").item(0).getTextContent());
                }
            }


            for (int temp = 0; temp < countries.size(); temp++) {
                ret += ("<option value=\"" + countries.toArray()[temp] + "\">" + StringEscapeUtils.escapeHtml4((String) countries.toArray()[temp]) + "</option>\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String extractWeather(String xml) {
        String ret = "";
        int index;
        Double Money;

        try {

            index = xml.indexOf("<soapenv:Envelope");
            xml = xml.substring(index);
            index = xml.indexOf("\"http://finalInvocation.tps\">") + 1;
            xml = xml.substring(index);
            index = xml.indexOf("\"http://finalInvocation.tps\">") + 1;
            xml = xml.substring(index);
            index = xml.indexOf(">") + 1;
            xml = xml.substring(index);
            Money = new Double(xml.substring(0, xml.indexOf("<")));
            index = xml.indexOf("<CurrentWeather>");
            if (index == -1) {
                ret = "Weather records not found in this area\n<br>Money: " + Money.toString();
                return ret;
            }
            xml = xml.substring(index);

            index = xml.indexOf("</CurrentWeather>");
            xml = xml.substring(0, index + 17);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            Node nNode = doc.getElementsByTagName("CurrentWeather").item(0);

            ret = "";
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                ret += "Location: " + eElement.getElementsByTagName("Location").item(0).getTextContent();
                ret += "<br>\n";
                ret += "Time of last weather report:" + eElement.getElementsByTagName("Time").item(0).getTextContent();
                ret += "<br>\n";
                ret += "Temperature: " + eElement.getElementsByTagName("Temperature").item(0).getTextContent();
                ret += "<br>\n";
                ret += "Dew Point: " + eElement.getElementsByTagName("DewPoint").item(0).getTextContent();
                ret += "<br>\n";
                ret += "Pressure: " + eElement.getElementsByTagName("Pressure").item(0).getTextContent();
                ret += "<br>\n";
                ret += "Wind: " + eElement.getElementsByTagName("Wind").item(0).getTextContent();
                ret += "<br>\n";
                ret += "Visibility: " + eElement.getElementsByTagName("Visibility").item(0).getTextContent();
                ret += "<br>\n";
                ret += "<hr>\n";
                ret += "Money: " + Money.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String GetCities(String country) throws Exception {

        String ret;
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://www.webservicex.com/globalweather.asmx";
        SOAPMessage soapResponse = soapConnection.call(
                createSOAPRequestCities(country), url);

        // Process the SOAP Response
        ret = processSOAPResponse(soapResponse);
        ret = extractCities(ret);
        soapConnection.close();
        return ret;
    }

    public static String GetCountries() throws Exception {

        String ret;
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://www.webservicex.com/globalweather.asmx";
        SOAPMessage soapResponse = soapConnection.call(
                createSOAPRequestCities(""), url);

        // Process the SOAP Response
        ret = processSOAPResponse(soapResponse);
        ret = extractCountries(ret);
        soapConnection.close();
        return ret;
    }

//  public static void main(String args[]) {
//    try {
//      String ret;
//      /*ret = GetWeatherAndCurrency("Great Britain", "Spain", "100", "Melilla");
//      System.out.println(ret);*/
//      
//      ret = GetCurrency("Spain");
//      System.out.println(ret);
//     /* ret = GetCountries();
//      System.out.println(ret);*/
//      /*ret = GetCities("Spain");
//      System.out.println(ret);*/
//    } catch (Exception e) {
//      System.err.println("Error occurred while sending SOAP Request to Server");
//      e.printStackTrace();
//    }
//  }
    private static SOAPMessage createSOAPRequestCurrency(String country) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://c2c.tps";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tns", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("c2cRequest", "tns");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("input", "tns");
        soapBodyElem1.addTextNode(country);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "/c2cRequest");

        soapMessage.saveChanges();
        soapMessage.writeTo(System.out);
        System.out.println("\n\n\n");

        return soapMessage;
    }

    private static SOAPMessage createSOAPRequestCountriesCurrency(String fromCountry, String toCountry, String capital, String toCity) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://finalInvocation.tps";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tns", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("finalInvocationRequest", "tns");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("fromCountry", "tns");
        soapBodyElem1.addTextNode(fromCountry);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("toCountry", "tns");
        soapBodyElem2.addTextNode(toCountry);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("capital", "tns");
        soapBodyElem3.addTextNode(capital);
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("toCity", "tns");
        soapBodyElem4.addTextNode(toCity);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "finalInvocationRequest");

        soapMessage.saveChanges();
        soapMessage.writeTo(System.out);

        return soapMessage;
    }

    private static SOAPMessage createSOAPRequestCities(String country) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://www.webserviceX.NET";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tns", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("GetCitiesByCountry", "tns");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("CountryName", "tns");
        soapBodyElem1.addTextNode(country);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "/GetCitiesByCountry");

        soapMessage.saveChanges();

        soapMessage.writeTo(System.out);

        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static String processSOAPResponse(SOAPMessage soapResponse) throws Exception {
        Writer sW = new StringWriter();
        String str;
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();

        StreamResult result = new StreamResult(sW);
        transformer.transform(sourceContent, result);
        str = sW.toString();
        str = StringEscapeUtils.unescapeXml(str);
        return str;
    }
}
