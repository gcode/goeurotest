package de.goeuro.test;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class Main {
	private static final String DELIMITER = ",";
	private static final String HEADER = "_id,name,type,latitude,longitude";
		
	public static void writeToCSV(String filename, String json){
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename);
			fileWriter.append(HEADER);
			fileWriter.append("\n");
			
			//Parse json string to object
			JSONArray arr = new JSONArray(json);
			for(int i = 0; i < arr.length(); i++){
				JSONObject obj = arr.getJSONObject(i);
				fileWriter.append(String.valueOf(obj.getInt("_id")));				
				fileWriter.append(DELIMITER);
				fileWriter.append(obj.getString("name"));
				fileWriter.append(DELIMITER);
				fileWriter.append(obj.getString("type"));
				fileWriter.append(DELIMITER);
				
				JSONObject geo = obj.getJSONObject("geo_position");
				fileWriter.append(String.valueOf(geo.getDouble("latitude")));
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(geo.getDouble("longitude")));
				
				fileWriter.append("\n");				
			}		
		} catch (Exception e) {
			System.out.println("Failed");
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Failed closing filewriter");
			}
			}


	}
	
	public static void main(String[] args){
		final String filename = args[0] + ".csv";
		final String uri = "http://api.goeuro.com/api/v2/position/suggest/en/" + args[0];
		
		//Call http get
		RestTemplate restTemplate = new RestTemplate();
	    String json = restTemplate.getForObject(uri, String.class);
	    
	    //Write to CSV
		writeToCSV(filename, json);
		System.out.println("Success");
	}
}
