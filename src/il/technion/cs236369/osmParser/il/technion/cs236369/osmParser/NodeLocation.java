package il.technion.cs236369.osmParser;

public class NodeLocation
{

	private String id;
	private double lat;
	private double lon;
	private String user;
	private String uid;
	
	
	NodeLocation()
	{
		
	}
	public NodeLocation(String id)
	{
		this.id = id;
	}

	public NodeLocation(String id, double lat, double lon) 
	{
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}

	public String getId() 
	{
		return id; 	
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public double getLat() 
	{
		return lat;
	}

	public void setLat(double lat) 
	{
		this.lat = lat;
	}

	public double getLon() 
	{
		return lon;
	}

	public void setLon(double lon) 
	{
		this.lon = lon;
	}

	public String getUser() 
	{
		return user;
	}

	public void setUser(String user) 
	{
		this.user = user;
	}

	public String getUid() 
	{
		return uid;
	}

	public void setUid(String uid) 
	{
		this.uid = uid;
	}

	/*
	 * Return node position
	 */
	public Position getPs() 
	{
		return (new Position(lat, lon));
	}

	
	
}
