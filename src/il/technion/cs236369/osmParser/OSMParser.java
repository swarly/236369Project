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
	private Way currentWay;

	// // Node object
	// private NodeLocation node;

	// temp string
	// private String temp;

	// Define if current element is 'way'
	private boolean stateWay = false;

	private ArrayList<Way> closedwayList = new ArrayList<Way>();

	private Map<String, NodeLocation> nodeList;
	private Map<String, Integer> NodeApperenace = new HashMap<String, Integer>();

	private JSONArray array;

	private Map<String, String> requiredTags;

	// private SAXParser sp;

	public OSMParser() throws Exception, SAXException
	{
		SAXParserFactory.newInstance();
		array = new JSONArray();
		nodeList = new HashMap<String, NodeLocation>();
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

		if (qName.equalsIgnoreCase("node"))
		{
			String id = attributes.getValue("id").toString();
			NodeLocation node = new NodeLocation(id, attributes);
			nodeList.put(id, node);
			NodeApperenace.put(node.getId(), 0);
		}

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
			currentWay.addNode(nodeList.get(key));
		}
	}

	/*
	 * When the parser encounters the end of an element, it calls this method
	 * call forever element - include nested
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if (currentWay != null && stateWay && qName.equalsIgnoreCase("way"))
		{
			if (currentWay.isCloseWay() && currentWay.containTags(requiredTags))
				array.add(currentWay.toJSON());

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
		requiredTags = tagsRequired.getTags();
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(osmFile, this);
		} catch (Exception ex)
		{
			ex.printStackTrace();
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