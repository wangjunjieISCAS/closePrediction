package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.csvreader.CsvReader;

public class PerformanceThresLearningChart {
	public void prepareChartData ( String folderName, String[] attrName, String outFileName ) {
		HashMap<Integer, Double[]> boxPlotDataset = new HashMap<Integer, Double[]>();
		//<fileName, <median values>>
		
		TreeMap<Integer, String> categoryMap = new TreeMap<Integer, String>();
		Integer catIndex = 1;
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = projectFileList[i];
				String categoryName = fileName.substring(0, fileName.length() - 4);
				String[] temp = categoryName.split( "-");
				categoryName = temp[1];
				/*
				if ( folderName.contains( "Knee") || folderName.contains( "Arrival") || folderName.contains( "Trend")) {
					categoryName = temp[1];
				}
				else{
					categoryName = temp[1] + "-" + temp[2];
				}
				*/
				
				categoryMap.put( catIndex, categoryName );
				
				catIndex = Integer.parseInt( categoryName );
				Double[] medianAttrValue = this.readMedianPerformance( folderName + "/" + projectFileList[i], attrName );
				boxPlotDataset.put( catIndex, medianAttrValue );
				catIndex++;
			}
		}
		
		String[] outputAttrName = { "%bug", "%reducedCost", "F1"};
		this.generatePlotDataFile( outFileName,  boxPlotDataset, outputAttrName, categoryMap);
		System.out.println( categoryMap.toString() );
		
		this.findOptimalParameter(boxPlotDataset, folderName, categoryMap);
	}
	
	public List<HashMap.Entry<Integer, Double[]>> rankBasedAttrName1 ( HashMap<Integer, Double[]>  boxPlotDataSet ) {
		List<HashMap.Entry<Integer, Double[]>> newDataSetList = new ArrayList<HashMap.Entry<Integer, Double[]>>(boxPlotDataSet.entrySet());

		Collections.sort( newDataSetList, new Comparator<HashMap.Entry<Integer, Double[]>>() {   
			public int compare(HashMap.Entry<Integer, Double[]> o1, HashMap.Entry<Integer, Double[]> o2) {      
			        //return (o2.getValue() - o1.getValue()); 
			       // return o1.getValue()[0].compareTo(o2.getValue()[0] ) ;
					return o1.getKey() - o2.getKey();
			    }
			}); 
		return newDataSetList;
	}
	
	public void findOptimalParameter ( HashMap<Integer, Double[]> boxPlotDataset, String folderName, TreeMap<Integer, String> categoryMap ) {
		Double minBugPercent = 0.9;
		Double minReportPercent = 0.3;
		Integer index = -1;
		Double maxF1 = 0.0, bug = 0.0, report=0.0;
		for ( Integer i : boxPlotDataset.keySet() ) {
			Double[] values = boxPlotDataset.get( i );
			if ( values[0] < minBugPercent || values[1] < minReportPercent )
				continue;
			
			if ( values[2] > maxF1 ) {
				maxF1 = values[2];
				bug = values[0];
				report = values[1];
				
				index = i;
			}
		}
		System.out.println( "======= " + folderName + " " + categoryMap.get( index) + " " + bug.toString() + " " + report.toString() + " " + maxF1.toString() );
	}
	
	public void generatePlotDataFile ( String outFileName, HashMap<Integer, Double[]> dataset, String[] attrName, TreeMap<Integer, String> categoryMap ) {
		List<HashMap.Entry<Integer, Double[]>> newDataSetList = this.rankBasedAttrName1( dataset );
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			writer.write( "rank" + "," + "index" + "," + "type" + "," );
			for ( int i =0; i < attrName.length; i++ )
				writer.write( attrName[i] + ",");
			writer.write( "y=0.9" + "," + "y=0.3" + ",");
			writer.newLine();
			
			for ( int j =0; j < newDataSetList.size(); j++  ) {
				Integer categoryName = newDataSetList.get(j).getKey();
				Double[] medianValues = newDataSetList.get(j).getValue();
				
				String displayCategoryName = categoryName.toString();
				if ( outFileName.contains( "Knee")) {
					Double temp = categoryName / 10.0;
					displayCategoryName = temp.toString();
				}
				
				writer.write( j+1 + "," + categoryName.toString() + ","  + displayCategoryName + ",");
				for ( int i =0; i < medianValues.length; i++ ) {
					writer.write( medianValues[i] + ",");					
				}
				writer.write( "0.9" + "," + "0.3" + ",");
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Double[] readMedianPerformance ( String fileName, String[] attrName ) {
		TreeMap<Integer, ArrayList<Double>> attrValuesList = new TreeMap<Integer, ArrayList<Double>>();
		for ( int i =0; i < attrName.length; i++ ) {
			ArrayList<Double> attrValue = new ArrayList<Double>();
			attrValuesList.put( i, attrValue );
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( fileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
	        while ( reader.readRecord() ){
	        	for ( int i =0; i < attrName.length; i++ ) {
	        		Double value = Double.parseDouble( reader.get( attrName[i] ) ) ;        
	        		ArrayList<Double> attrValue = attrValuesList.get( i );
	        		attrValue.add( value );
	        		attrValuesList.put( i, attrValue );
	        	}
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
		
		Double[] medianAttrValues = new Double[attrName.length];
		for ( Integer attrIndex : attrValuesList.keySet() ) {
			ArrayList<Double> attrValue = attrValuesList.get( attrIndex );
			Collections.sort( attrValue );
			
			Double median = 0.0;
			int midIndex = attrValue.size() / 2;
			if ( attrValue.size() % 2 == 1 ) {
				median = attrValue.get( midIndex );
			}
			else {
				median = ( attrValue.get( midIndex -1) + attrValue.get(midIndex)) / 2;
			}

			medianAttrValues[attrIndex] = median;
		}
		return medianAttrValues;
	}
	
	public static void main ( String args[] ) {
		PerformanceThresLearningChart plot = new PerformanceThresLearningChart();
		
		String[] attrName = { "percentBugsDetected", "percentSavedEffort", "F1" };
		String[] typeList = { "Trend", "Arrival", "Knee", "M0", "Mth", "MhJK", "MhCH", "MtCH"}; //MhJK, MtCH, MhCH, Trend
		for ( int i =0; i < typeList.length; i++ ) {
			String type = typeList[i];
			String outFile =  "data/output/thresLearning" + type + ".csv";
			plot.prepareChartData( "data/output/performance" + type , attrName, outFile );
		}
		
	}
}
