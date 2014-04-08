package il.technion.cs236369.osmParser;

import org.xml.sax.Attributes;

public class NodeLocation
{

	private String id;
	private double lat;
	private double lon;
	private String user;
	private String uid;

	public NodeLocation(String id, Attributes attributes)
	{
		this.id = id;
		this.lat = Float.parseFloat(attributes.getValue("lat"));
		this.lon = Float.parseFloat(attributes.getValue("lon"));
		this.uid = attributes.getValue("uid").toString();
		this.user = attributes.getValue("user").toString();
	}

	public String getId()
	{
		return id;
	}

	public double getLat()
	{
		return lat;
	}

	public double getLon()
	{
		return lon;
	}

	public String getUser()
	{
		return user;
	}

	public String getUid()
	{
		return uid;
	}

	/*
	 * Return node position
	 */
	public Position getPs()
	{
		return new Position(lat, lon);
	}

}
