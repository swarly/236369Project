package il.technion.cs236369.osmParser;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse an OSM XML file and print all names of 'way' elements.
 * http://java.kompf.de/download/OsmSaxNames.java
 *
 */
public class test {

  /**
   * Find all way names in the given OSM file.
   * @param file The file.
   * @return The sorted set of way names.
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  public SortedSet<String> findWayNames(File file) throws ParserConfigurationException,
      SAXException, IOException {
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    //parserFactory.setNamespaceAware(true);
    SAXParser parser = parserFactory.newSAXParser();

    OsmNamesHandler handler = new OsmNamesHandler();
    parser.parse(file, handler);
    
    return handler.getNames();
  }

  private static class OsmNamesHandler extends DefaultHandler {
    private final SortedSet<String> nameSet = new TreeSet<String>();
    private final Stack<String> eleStack = new Stack<String>();
    
    public SortedSet<String> getNames() {
      return nameSet;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
        Attributes attrs) throws SAXException {
      // System.out.printf("element: uri=%s localName=%s qName=%s\n", uri, localName, qName);
      if ("tag".equals(qName) && "way".equals(eleStack.peek())) {
        String key = attrs.getValue("k");
        if ("name".equals(key)) {
          nameSet.add(attrs.getValue("v"));
        }
      }
      eleStack.push(qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
      eleStack.pop();
    }
  }

  private void printWayNames(SortedSet<String> nameSet, PrintStream out) {
    for (String name : nameSet) {
      out.println(name);
    }
  }
  
  /**
   * Downlaod OSM sample file.
   * @param file The file to save the data into.
   * @throws IOException
   */
  public void downloadSample(File file) throws IOException {
    URL downloadUrl = new URL("http://api.openstreetmap.org/api/0.6/map?bbox=11.62973,52.12236,11.63736,52.12853");
    InputStream in = downloadUrl.openStream();
    OutputStream out = new FileOutputStream(file);
    byte[] buffer = new byte[10000];
    try {
      int len = in.read(buffer);
      while (len > 0) {
        out.write(buffer, 0, len);
        len = in.read(buffer);
      }
    } finally {
      out.close();
      in.close();
    }
  }

  /**
   * MAIN.
   * @param args ignored.
   * @throws Exception If an error occurs.
   */
  public static void main(String[] args) throws Exception {
    File osmXmlFile = new File("C:/Users/hagitz/Downloads/new-york-latest/london_small.osm");
    test osmSaxNames = new test();
    if (! osmXmlFile.canRead()) {
      System.out.println("Downloading OSM data...");
      osmSaxNames.downloadSample(osmXmlFile);
    }
    SortedSet<String> nameSet = osmSaxNames.findWayNames(osmXmlFile);
    osmSaxNames.printWayNames(nameSet, System.out);
  }

}