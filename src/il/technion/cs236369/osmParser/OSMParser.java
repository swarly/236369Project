package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.simple.JSONArray;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OSMParser extends DefaultHandler implements IOSMParser
{

	// Way object
	private Way way;

	// Node object
	private NodeLocation node;

	// temp string
	private String temp;

	// Define if current element is 'way'
	private boolean stateWay = false;

	public ArrayList<Way> wayList = new ArrayList<Way>();

	// List of all node object

	private final ArrayList<NodeLocation> NodeList = new ArrayList<NodeLocation>();

	// Define how many time each node appear in ways
	private final Map<String, Integer> NodeApperenace = new HashMap<String, Integer>();

	private final SAXParser sp;

	public OSMParser() throws Exception, SAXException
	{
		// Create a "parser factory" for creating SAX parsers
		SAXParserFactory spfac = SAXParserFactory.newInstance();

		// Now use the parser factory to create a SAXParser object
		sp = spfac.newSAXParser();
	}

	/*
	 * When the parser encounters plain text (not XML elements), it calls(this
	 * method, which accumulates them in a string buffer not in use here.
	 */
	@Override
	public void characters(char[] buffer, int start, int length)
	{
		temp = new String(buffer, start, length);
	}

	/*
	 * Every time the parser encounters the beginning of a new element, it calls
	 * this method, which resets the string buffer. it can be nested element -
	 * will call for each element.
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		temp = "";
		String key;
		// Node element
		/*
		 * if(qName.equalsIgnoreCase("node")) { node = new NodeLocation();
		 * node.setId(attributes.getValue("id").toString());
		 * node.setLat(Float.parseFloat(attributes.getValue("lat")));
		 * node.setLon(Float.parseFloat(attributes.getValue("lon")));
		 * node.setUid(attributes.getValue("uid").toString());
		 * node.setUser(attributes.getValue("user").toString());
		 * NodeList.add(node); NodeApperenace.put(node.getId(), 0); }
		 */

		// check if way element arrived
		if (qName.equalsIgnoreCase("way") && stateWay == false)
		{
			way = new Way();
			way.setID(attributes.getValue("id").toString());
			stateWay = true;
		}

		if (stateWay == true && qName.equalsIgnoreCase("tag"))
			if (attributes.getValue("k").equalsIgnoreCase("website"))
				way.setWebSite(attributes.getValue("v").toString());
			else if (attributes.getValue("k").equalsIgnoreCase("wikipedia"))
				way.setWikipedia(attributes.getValue("v").toString());

		// read nd id for current 'way'
		if (stateWay == true && "nd".equalsIgnoreCase(qName))
		{
			key = attributes.getValue(0);
			if (NodeApperenace.containsKey(key))
			{
				int k = NodeApperenace.get(key);
				NodeApperenace.remove(key);
				NodeApperenace.put(key, k + 1);
			}
			way.AddNode(new NodeLocation(key));
		}
	}

	/*
	 * When the parser encounters the end of an element, it calls this method
	 * call forevery elemnet - include nested
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if (way != null && stateWay == true && qName.equalsIgnoreCase("way"))
		{
			if (way.IsCloseWay())
				wayList.add(way);
			else
				way = null;
			stateWay = false;
		}
	}

	@Override
	public JSONArray parse(String osmFile, ITagsRequired tagsRequired)
	{
		// TODO Auto-generated method stub

		return null;
	}
}