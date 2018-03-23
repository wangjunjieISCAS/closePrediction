package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.csvreader.CsvReader;

public class PerformanceBoxPlotMoreAttributes {
	public void prepareBoxPlotData ( String folderName, String[] attrName ) {
		HashMap<Integer, HashMap<Integer, ArrayList<Double[]>>> boxPlotDataset = new HashMap<Integer, HashMap<Integer, ArrayList<Double[]>>>();
		//<equalTimeThres, <captureSize, values>>
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String[] temp = projectFileList[i].split( "-");
				Integer captureSize = Integer.parseInt( temp[1] );
				Integer equalTimeThres = Integer.parseInt( temp[2] );
				
				ArrayList<Double[]> attrValues = this.readPerformance( folderName + "/" + projectFileList[i], attrName );
				HashMap<Integer, ArrayList<Double[]>> dataset = new HashMap<Integer, ArrayList<Double[]>>();
				if ( boxPlotDataset.containsKey( equalTimeThres )) {
					dataset = boxPlotDataset.get( equalTimeThres );
				}
				
				dataset.put( captureSize, attrValues );
				boxPlotDataset.put( equalTimeThres, dataset );
			}
		}
		
		Integer[] equalTimeThres = {2, 3, 4, 5, 6, 7, 8, 9, 10 };
		String[] outputAttrName = { "%bugs", "%reports"};
		for ( int i =0; i < equalTimeThres.length; i++ ) {
			Integer equalTime = equalTimeThres[i];
			this.generatePlotDataFile( "data/output/equalTime-" + equalTime + ".csv", boxPlotDataset.get( equalTime ), outputAttrName);
		}
	}
	
	public void generatePlotDataFile ( String outFileName, HashMap<Integer, ArrayList<Double[]>> dataset, String[] attrName ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			writer.write( " " + "," + "  " + "," + "number");
			writer.newLine();
			
			for ( Integer captureSize : dataset.keySet() ) {
				ArrayList<Double[]> values = dataset.get( captureSize);
				for ( int i =0; i < values.size(); i++ ) {
					for ( int j =0; j < values.get(i).length; j++ ) {
						writer.write( captureSize + "," + attrName[j] + "," + values.get(i)[j]);
						writer.newLine();
					}					
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<Double[]> readPerformance ( String fileName, String[] attrName ) {
		ArrayList<Double[]> attrValues = new ArrayList<Double[]>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( fileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
	        while ( reader.readRecord() ){
        		Double[] values = new Double[attrName.length];
	        	for ( int i =0; i < attrName.length; i++ ) {
	        		Double value = Double.parseDouble( reader.get( attrName[i] ) ) ;        
	        		values[i] = value;
	        	}
	        	attrValues.add( values );	
	        }
			
	        reader.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attrValues;
	}
	
	public static void main ( String args[] ) {
		PerformanceBoxPlotMoreAttributes plot = new PerformanceBoxPlotMoreAttributes();
		
		String[] attrName = { "percentBugsDetected", "percentReportsSubmit"};
		plot.prepareBoxPlotData( "data/output/performance", attrName);
	}
}
