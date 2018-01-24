package dataManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InputManager {
	private String inputFile;
	private DistanceMatrix matrix;
	private HashMap<Integer,Release> releaseMap = new HashMap<Integer,Release>();	
	
	public InputManager(String inputFile){
		this.inputFile=inputFile;
	}
	
	public void parseReleases(){
					
		String inputPath="files/input/release_input/"+inputFile;
		Scanner reader;
		File dataFile = new File(inputPath);		
		try 
		 { 
			 reader = new Scanner(new FileInputStream(dataFile));
			//skip header 
			 reader.nextLine();
			 while(reader.hasNextLine()){
				String line1=reader.nextLine();
				String fields[] = line1.split(";");
				int growth=0;
				int maintenance=0;
				if(!fields[4].equals("-")){
					growth=Integer.parseInt(fields[4]);
				}
				if(!fields[5].equals("-")){
					maintenance=Integer.parseInt(fields[5]);
				}
									
				Release tmp = new Release(Integer.parseInt(fields[0]),fields[1],fields[2], Integer.parseInt(fields[3]),growth,maintenance, Double.parseDouble(fields[6]), Double.parseDouble(fields[7]), fields[8], fields[9], Integer.parseInt(fields[10]), fields[12], fields[14], Integer.parseInt(fields[15]));
				releaseMap.put(Integer.parseInt(fields[0]), tmp);	
			 }
			reader.close();				
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",inputPath); 
		 }
	}
	
	public HashMap<Integer,Release> getReleases(){
		return releaseMap;
	}
	
}
