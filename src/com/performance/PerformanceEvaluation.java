package com.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class PerformanceEvaluation {
	HashMap<String, Integer[]> groundTruthMap ;
	
	public PerformanceEvaluation ( String fileName ) {
		groundTruthMap = new HashMap<String, Integer[]>();
		try {
			BufferedReader reader = new BufferedReader ( new FileReader ( new File ( fileName ) ));
			
			String line = "";
			boolean isHeader = true;
			while ( (line = reader.readLine()) != null ) {
				if ( isHeader == true ) {
					isHeader = false;
					continue;
				}
				String[] temp = line.split( ",");
				String project = temp[0].trim();
				
				Integer bugNum = Integer.parseInt( temp[1].trim() );
				Integer insNum = Integer.parseInt( temp[2].trim() );
				Integer reportNum = Integer.parseInt( temp[3].trim() );
				
				Integer[] groundTruth = new Integer[3];
				groundTruth[0] = bugNum;
				groundTruth[1] = insNum;
				groundTruth[2] = reportNum;

				groundTruthMap.put( project, groundTruth );
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Double[] evaluatePerformance ( Integer[] results, String project ) {
		Integer[] groundTruth = groundTruthMap.get( project );     //bugNum, insNum, reportNum
		if ( results == null || results.length == 0 ) {
			Double[] performance = { 0.0, 1.0*groundTruth[0], 0.0, 0.0, 1.0*groundTruth[2], 0.0, 1.0, 
					0.0, 0.0,  1.0*groundTruth[1], 0.0  };
			return performance;
		}
			
		
		int bugDetected = results[0];   
		if ( bugDetected > groundTruth[0] )
			bugDetected = groundTruth[0];
		double percentBug = (1.0*bugDetected) / (1.0* groundTruth[0]);
		
		int reportSubmit = results[1];
		double percentReport = (1.0* reportSubmit) / ( 1.0* groundTruth[2] );
		double percentSavedEffort = 1.0 - percentReport;
		
		double F1 = ( 2.0* percentBug * percentSavedEffort) / (percentBug + percentSavedEffort);
		//double F1 = ( percentBug + percentSavedEffort) / 2.0;
		double F1M = ( 2.0* percentBug * percentSavedEffort) / (percentBug + percentSavedEffort);
		
		//double weight = 1.2;
		//double E1 = 1.0 - ( 1.0 + weight*weight) / ( weight*weight / percentBug + 1.0 / percentSavedEffort);
		
		double difWithOptimal = reportSubmit - groundTruth[1];
		
		Double[] performance = { 1.0*bugDetected, 1.0*groundTruth[0], percentBug, 1.0*reportSubmit, 1.0*groundTruth[2], percentReport, percentSavedEffort, F1, F1M,  1.0*groundTruth[1], difWithOptimal  };
		
		return performance;
	}
	
	
	
	public Double[] evaluatePerformanceDistribution ( Integer[] results, String project ) {  
		Double[] performance = this.evaluatePerformance(results, project);
		
		Double[] extendPerformance = new Double[performance.length + results.length - 2];
		for ( int i =0; i < performance.length; i++ )
			extendPerformance[i] = performance[i];
	
		int length = performance.length;
		for ( int i =2; i < results.length; i++ )
			extendPerformance[length+i-2] = 1.0*results[i];   //from result[2] 
		return extendPerformance; 
	}
}
