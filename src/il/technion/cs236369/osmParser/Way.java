package il.technion.cs236369.osmParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

public class Way
{
	private String name;
	private String id;
	private String webSite;
	private String wikipedia;
	private String nd;
	private String nd2;
	private double radius = 0;
	private Position center;
	private Map<String, String> tags;
	private static final String[] tagsToInsert =
	{ "name", "wikipedia", "website" };
	// List of way's node
	// private List nodes;

	List<NodeLocation> nodes;
	// List of node's position
	private Set<Position> positions;

	public Way()
	{
		nodes = new LinkedList<NodeLocation>();
		positions = new HashSet<Position>();
		tags = new HashMap<String, String>();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setID(String id)
	{
		this.id = id;
	}

	public String getID()
	{
		return id;
	}

	public void setWebSite(String webSite)
	{
		this.webSite = webSite;
	}

	public String getWebSite()
	{
		return webSite;
	}

	public String getWikipedia()
	{
		return wikipedia;
	}

	public void setWikipedia(String wikipedia)
	{
		this.wikipedia = wikipedia;
	}

	public void setNd(String nd)
	{
		this.nd = nd;
	}

	public String getNd()
	{
		return nd;
	}

	public void setNd2(String nd2)
	{
		this.nd2 = nd2;
	}

	public String getNd2()
	{
		return nd2;
	}

	/*
	 * Add node to way and convert it to position
	 */
	public void AddNode(NodeLocation n)
	{
		nodes.add(n);
		positions.add(n.getPs());
	}

	/*
	 * Return way radius according to nodes value
	 */
	public double calculateRadius()
	{
		center = Position.getCenter(positions);
		for (Position p : positions)
			radius = Math.max(radius, p.haversineDistance(center));
		return radius;
	}

	public double getCalculateCircumscribedArea()
	{
		calculateRadius();
		return radius * radius * Math.PI;
	}

	public boolean isCloseWay()
	{
		nd = ((LinkedList<NodeLocation>) nodes).getFirst().getId();
		nd2 = ((LinkedList<NodeLocation>) nodes).getLast().getId();
		return nd.equals(nd2);
	}

	/**
	 * add new tag to way tags
	 * 
	 * @param name
	 *            - tag name
	 * @param value
	 *            - tag value
	 */
	public void addTag(String name, String value)
	{
		tags.put(name, value);
	}

	/**
	 * @param key
	 * @return true if tag key exist in tags
	 */
	public boolean containTag(String key)
	{
		return tags.containsKey(key);
	}

	public JSONObject toJSON()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", getID().toString());
		for (String element : tagsToInsert)
			if (tags.containsKey(element))
				jsonObject.put("name", tags.get(element));
		jsonObject.put("area", getCalculateCircumscribedArea());
		return jsonObject;
	}

	public boolean containTags(Map<String, String> requiredTags)
	{
		for (String string : requiredTags.keySet())
			if (!tags.containsKey(string)
					|| !tags.get(string).equals(requiredTags.get(string)))
				return false;
		return true;
	}
}
