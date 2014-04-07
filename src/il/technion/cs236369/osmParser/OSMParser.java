package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.simple.JSONArray;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OSMParser extends DefaultHandler implements IOSMParser
{

	// Way object
	private Way currentWay;

	// // Node object
	// private NodeLocation node;

	// temp string
	// private String temp;

	// Define if current element is 'way'
	private boolean stateWay = false;

	private ArrayList<Way> closedwayList = new ArrayList<Way>();

	// List of all node object

	// private final ArrayList<NodeLocation> NodeList = new
	// ArrayList<NodeLocation>();

	// Define how many time each node appear in ways
	private Map<String, Integer> NodeApperenace = new HashMap<String, Integer>();

	// private SAXParser sp;

	public OSMParser() throws Exception, SAXException
	{
		SAXParserFactory.newInstance();

		// Now use the parser factory to create a SAXParser object
		// sp = spfac.newSAXParser();
	}

	/*
	 * When the parser encounters plain text (not XML elements), it calls(this
	 * method, which accumulates them in a string buffer not in use here.
	 */
	@Override
	public void characters(char[] buffer, int start, int length)
	{
		// temp = new String(buffer, start, length);
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
		// temp = "";
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
		if (qName.equalsIgnoreCase("way") && !stateWay)
		{
			currentWay = new Way();
			currentWay.setID(attributes.getValue("id").toString());
			stateWay = true;
		}

		if (stateWay && qName.equalsIgnoreCase("tag"))
		{
			String tagKey = attributes.getValue("k");
			if (tagKey.equalsIgnoreCase("website"))
				currentWay.setWebSite(attributes.getValue("v").toString());
			else if (tagKey.equalsIgnoreCase("wikipedia"))
				currentWay.setWikipedia(attributes.getValue("v").toString());
			currentWay.addTag(tagKey, attributes.getValue("v").toString());
		}
		// read nd id for current 'way'
		if (stateWay && "nd".equalsIgnoreCase(qName))
		{
			key = attributes.getValue(0);
			if (NodeApperenace.containsKey(key))
			{
				int k = NodeApperenace.get(key);
				NodeApperenace.remove(key);
				NodeApperenace.put(key, k + 1);
			}
			currentWay.AddNode(new NodeLocation(key));
		}
	}

	/*
	 * When the parser encounters the end of an element, it calls this method
	 * call forever element - include nested
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if (currentWay != null && stateWay == true
				&& qName.equalsIgnoreCase("way"))
		{
			if (currentWay.isCloseWay())
				closedwayList.add(currentWay);
			else
				currentWay = null;
			stateWay = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.technion.cs236369.osmParser.IOSMParser#parse(java.lang.String,
	 * il.technion.cs236369.osmParser.ITagsRequired)
	 */
	@Override
	public JSONArray parse(String osmFile, ITagsRequired tagsRequired)
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(osmFile, this);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		JSONArray array = new JSONArray();
		for (Way way : closedwayList)
		{
			Set<Entry<String, String>> tagsToCheck = tagsRequired.getTags()
					.entrySet();
			for (Entry<String, String> entry : tagsToCheck)
				if (!way.containTag(entry.getKey()))
					break;
			array.add(way.toJSON());
		}
		return array;
	}

	/**
	 * @return the wayList
	 */
	public ArrayList<Way> getWayList()
	{
		return closedwayList;
	}
}