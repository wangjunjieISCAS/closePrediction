package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class PerformanceDemonstrationBox {
	String[] categoryRank = { "Trend", "Arrival", "Knee", "M0", "Mth", "MhJK", "MhCH", "MtCH"};
	//String[] categoryRank = { "M080", "M085", "M090", "M095", "M0100"};
	
	public void prepareBoxPlotData ( String folderName, String[] attrName ) {
		HashMap<String, ArrayList<Double[]>> boxPlotDataset = new HashMap<String, ArrayList<Double[]>>();
		//<fileName, <values>>
		
		TreeMap<String, String> categoryMap = new TreeMap<String, String>();
		//Integer catIndex = 1;
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = projectFileList[i];
				String categoryName = fileName.substring(0, fileName.length() - 4);
				
				String[] temp = categoryName.split( "-");
				int index = temp[0].indexOf( "performance");
				String catIndex = temp[0].substring( index+new String("performance").length() );
				categoryMap.put( categoryName, catIndex);
			
				ArrayList<Double[]> attrValues = this.readPerformance( folderName + "/" + projectFileList[i], attrName );
				boxPlotDataset.put( catIndex, attrValues );
				//catIndex++;
			}
		}
		
		String[] outputAttrName = { "%bug", "%reducedCost", "F1"};
		this.generatePlotDataFile( "data/output/eff.csv", boxPlotDataset, outputAttrName);
		System.out.println( categoryMap.toString() );
		
		this.generateDataPerCategoryAttr( "data/output/eff-test.csv", boxPlotDataset, outputAttrName);
	}
	
	public void generatePlotDataFile ( String outFileName, HashMap<String, ArrayList<Double[]>> dataset, String[] attrName ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			writer.write( " " + "," + "  " + "," + "number");
			writer.newLine();
			
			for ( int k =0; k < categoryRank.length; k++) {
				String categoryName = categoryRank[k];
				
				if ( !dataset.containsKey( categoryName))
					continue;
				
			//for ( String categoryName : dataset.keySet() ) {
				ArrayList<Double[]> values = dataset.get( categoryName);
				for ( int i =0; i < values.size(); i++ ) {
					for ( int j =0; j < values.get(i).length; j++ ) {
						
						writer.write( categoryName + "," + attrName[j] + "," + values.get(i)[j]);
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
	
	public void generateDataPerCategoryAttr ( String outFileName, HashMap<String, ArrayList<Double[]>> dataset, String[] attrName ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			
			for ( int k  =0; k < categoryRank.length; k++ ) {
				String categoryName = categoryRank[k];
				
				if ( !dataset.containsKey( categoryName ))
					continue;
				
				for ( int i =0; i < attrName.length; i++ ) {
					writer.write( categoryName + "-" + attrName[i] + ",");
				}
			}
			writer.newLine();
			
			ArrayList<Double[]> values = dataset.get( categoryRank[0] );
			for ( int i =0; i < values.size(); i++ ) {
				for ( int j=0; j < categoryRank.length; j++ ) {
					String categoryName = categoryRank[j];
					if ( !dataset.containsKey( categoryName ))
						continue;
					ArrayList<Double[]> curValues = dataset.get( categoryName );
					Double[] rowValues = curValues.get( i );
					for ( int k =0; k < rowValues.length; k++ ) {
						writer.write( rowValues[k].toString() + ",");
					}
				}
				writer.newLine();
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
		PerformanceDemonstrationBox plot = new PerformanceDemonstrationBox();
		
		String[] attrName = { "percentBugsDetected", "percentSavedEffort", "F1"};
		plot.prepareBoxPlotData( "data/output/bestPerformance", attrName);
	}
}
