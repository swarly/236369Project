package il.technion.cs236369.osmParser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Way 
{
	private String name;
    private String id;
    private String webSite;
    private String wikipedia;
    private String nd;
    private String nd2;
    private double radius=0;
    private Position center;
    
    //List of way's node
   // private List nodes;    
    
    List<NodeLocation> nodes;
    //List of node's position
    private Set<Position> positions; 
    
    
    public Way() 
    {
    	nd="";
    	nd2="";
    	webSite="";
    	wikipedia="";
    	nodes =new LinkedList();
    	positions = new HashSet<Position>();
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
    	//positions.add(n.getPs());
    }
    
    /*
     * Return way radius according to nodes value
     */
    public double CalculateRadion()
    {
    	 center = Position.getCenter(positions);
 		 for (Position p : positions) 
 		 {
 			radius = Math.max(radius, p.haversineDistance(center));
 		 }
 		 return radius;
    }
    
    public double CalculateCircumscribedArea()
    {
    	CalculateRadion();
    	return radius * radius * Math.PI;
    }
    
    public boolean IsCloseWay()
    {
    	NodeLocation n = nodes.get(0);
    	nd = n.getId();
    	int i = nodes.size() -1;
    	n = nodes.get(i);
    	nd2 = n.getId();
    	return nd.equals(nd2);
    }
    
}

