package com.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.csvreader.CsvReader;

public class BestParameterForEachTask {
	public void obtainBestParameterForEachTask ( String folderName ) {
		String[] attrName = { "percentBugsDetected", "percentSavedEffort", "F1" };
		
		//<ProjectId, <Parameter, value>
		HashMap<Integer, HashMap<Integer, Double[]>> projectPerformanceList = new HashMap<Integer, HashMap<Integer, Double[]>>();
		File projectFolder = new File ( folderName );
		if ( projectFolder.isDirectory() ) {
			String[] projectFileList = projectFolder.list();
			for ( int i =0; i < projectFileList.length; i++ ) {
				String fileName = projectFileList[i];
				String[] temp = fileName.split( "-");
				Integer para = Integer.parseInt( temp[1] );
				
				HashMap<Integer, Double[]> attrValues = this.readPerformance( folderName+"/"+fileName, attrName);
				for ( Integer project : attrValues.keySet() ) {
					Double[] value = attrValues.get( project );
					HashMap<Integer, Double[]> performance = new HashMap<Integer, Double[]>();
					if ( projectPerformanceList.containsKey( project )) {
						performance = projectPerformanceList.get( project );
					}
					performance.put( para, value );
					projectPerformanceList.put( project, performance );
				}
			}
		}
			
		//parameter, number
		HashMap<Integer, Integer> bestPara = new HashMap<Integer, Integer>();
		for ( Integer project: projectPerformanceList.keySet() ) {
			HashMap<Integer, Double[]> performance = projectPerformanceList.get( project );
			Integer optPara = this.findMaxF1(performance);
			int count = 1;
			if ( bestPara.containsKey( optPara )) {
				count += bestPara.get( optPara );
			}
			bestPara.put( optPara, count );
		}
		
		System.out.println( folderName + ": " + bestPara.toString() );
	}
	
	//bugs > 0.9, report > 0.3, maximize F1
	public Integer findMaxF1 ( HashMap<Integer, Double[]> performance ) {
		double maxF1 = -1.0;
		int optPara = -1;
		for ( Integer para : performance.keySet() ) {
			Double[] value = performance.get( para );
			if ( value[0] >= 0.9 && value[1] >= 0.3 ) {
				if ( value[2] > maxF1 ) {
					maxF1 = value[2];
					optPara = para;
				}
			}
		}
		
		//没有满足bugs>0.9, report>0.3的限制
		if ( maxF1 == -1.0 ) {
			for ( Integer para : performance.keySet() ) {
				Double[] value = performance.get( para );
				if ( value[2] > maxF1 ) {
					maxF1 = value[2];
					optPara = para;
				}
			}
		}
		return optPara;
	}
	
	public HashMap<Integer, Double[]> readPerformance ( String fileName , String[] attrName) {
		HashMap<Integer, Double[]> attrValues = new HashMap<Integer, Double[]>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( fileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
	        while ( reader.readRecord() ){
        		Double[] values = new Double[attrName.length];
        		String projectName = reader.get( "project");
        		String[] temp = projectName.split( "-");
        		Integer projectId = Integer.parseInt( temp[0] );
        		
	        	for ( int i =0; i < attrName.length; i++ ) {
	        		Double value = Double.parseDouble( reader.get( attrName[i] ) ) ;        
	        		values[i] = value;
	        	}
	        	attrValues.put( projectId, values );
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
	
	public static void main ( String[] args ) {
		BestParameterForEachTask para = new BestParameterForEachTask();
		
		String[] typeList = { "Trend", "M0", "Knee", "Arrival", "Mth", "MhJK", "MhCH", "MtCH"} ;  //MhJK, MtCH, MhCH, Trend
		for ( int i =0; i < typeList.length; i++ ) {
			String type = typeList[i];
			para.obtainBestParameterForEachTask( "data/output/performance" + type  );
		}
		
		
	}
}
