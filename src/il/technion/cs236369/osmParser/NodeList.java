package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Arik NodeList is an object that is used to scan the XML file and
 *         return a list of nodes that exist only in closed ways ( these node
 *         may exist in other non closed ways
 */
public class NodeList extends DefaultHandler
{

	private boolean stateWay;
	private Way currentWay;
	private Map<String, String> requiredTags;
	private List<String> tmpNodes;
	private List<String> finalNodes;

	/*
	 * w (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	/**
	 * @param requiredTags2
	 * 
	 */
	public NodeList(Map<String, String> requiredTags2)
	{
		super();
		this.requiredTags = requiredTags2;
		tmpNodes = new LinkedList<>();
		finalNodes = new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		if (qName.equalsIgnoreCase("way") && !stateWay)
		{
			currentWay = new Way();
			currentWay.setID(attributes.getValue("id").toString());
			stateWay = true;
		} else if (stateWay && "nd".equalsIgnoreCase(qName))
			tmpNodes.add(attributes.getValue(0));
		else if (stateWay && qName.equalsIgnoreCase("tag"))
		{
			String tagKey = attributes.getValue("k");
			if (tagKey.equalsIgnoreCase("website"))
				currentWay.setWebSite(attributes.getValue("v").toString());
			else if (tagKey.equalsIgnoreCase("wikipedia"))
				currentWay.setWikipedia(attributes.getValue("v").toString());
			currentWay.addTag(tagKey, attributes.getValue("v").toString());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if (currentWay != null && stateWay && qName.equalsIgnoreCase("way"))
		{
			if (isCloseWay() && currentWay.containTags(requiredTags))
				finalNodes.addAll(tmpNodes);
			tmpNodes.clear();
			currentWay = null;
			stateWay = false;
		}
	}

	/**
	 * @return true if supplied required tags contained in the way
	 */
	private boolean isCloseWay()
	{
		String nd = ((LinkedList<String>) tmpNodes).getFirst();
		String nd2 = ((LinkedList<String>) tmpNodes).getLast();
		return nd.equals(nd2);
	}

	/**
	 * @return the finalNodes
	 */
	public List<String> getFinalNodes()
	{
		return finalNodes;
	}
}
