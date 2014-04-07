package il.technion.cs236369.osmParser;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.omg.CORBA.portable.InputStream;

public class test1 
{

	private static final  String xmlFilePath = "C:/Users/hagitz/Downloads/new-york-latest/new-york-latest.osm";
	//private static final  String xmlFilePath = "C:/Users/hagitz/Downloads/new-york-latest/london_small.osm";
	
	public static void main(String[] args) throws Exception 
	{
		 
		OSMParser osm = new OSMParser();
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFilePath, osm);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
			
		
		System.out.println("Number of the total closed ways in file: " + osm.wayList.size() + "'.");
}

	

		       
		            
		       
		       /**
		        * Downlaod OSM sample file.
		        * @param file The file to save the data into.
		        * @throws IOException
		        */
		       public void downloadSample(File file) throws IOException {
		         URL downloadUrl = new URL("http://api.openstreetmap.org/api/0.6/map?bbox=11.62973,52.12236,11.63736,52.12853");
		         InputStream in = (InputStream) downloadUrl.openStream();
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
		      
}		