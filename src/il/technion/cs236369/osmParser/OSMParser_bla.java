package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;

import org.json.simple.JSONArray;

public class OSMParser_bla implements IOSMParser
{
	// Way object
	private Way way;

	// Node object
	private NodeLocation node;

	// temp string
	private String temp;

	// Define ifcurrent element is 'way'
	private boolean stateWay = false;

	public ArrayList<Way> wayList = new ArrayList<Way>();

	// List of all node object

	private ArrayList<NodeLocation> NodeList = new ArrayList<NodeLocation>();

	// Define how many time each node appear in ways
	private Map<String, Integer> NodeApperenace = new HashMap<String, Integer>();

	private SAXParser sp;

	@Override
	public JSONArray parse(String osmFile, ITagsRequired tagsRequired)
	{

		return null;

	}

}