package com.processPrediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import com.data.TestProject;
import com.dataProcess.TestProjectReader;
import com.processPerformance.PerformanceEvaluation;

public class CombinedTotalBugPrediction {
	
	public void predictPercentageBugCombined ( TestProject project, int captureSize, double custThres, String performanceFolder ) {
		PerformanceEvaluation evaluation = new PerformanceEvaluation();
		ArrayList<Double> performance = new ArrayList<Double>();
		
		TotalBugPredictionM0 M0Prediction = new TotalBugPredictionM0();
		TreeMap<Integer, Integer[]> M0Result = M0Prediction.predictTotalBug(project, captureSize, "");
		Double specifDif = evaluation.obtainSpecificDifference( M0Result, custThres);
		performance.add( specifDif);
		
		TotalBugPredictionMth MthPrediction = new TotalBugPredictionMth();
		TreeMap<Integer, Integer[]> MthResult = MthPrediction.predictTotalBug(project, captureSize, "");
		specifDif = evaluation.obtainSpecificDifference( MthResult, custThres);
		performance.add( specifDif);
		
		TotalBugPredictionMtCH MtCHPrediction = new TotalBugPredictionMtCH();
		TreeMap<Integer, Integer[]> MtCHResult = MtCHPrediction.predictTotalBug(project, captureSize, "");
		specifDif = evaluation.obtainSpecificDifference( MtCHResult, custThres);
		performance.add( specifDif);
		
		TotalBugPredictionMhCH MhCHPrediction = new TotalBugPredictionMhCH();
		TreeMap<Integer, Integer[]> MhCHResult = MhCHPrediction.predictTotalBug(project, captureSize, "");
		specifDif = evaluation.obtainSpecificDifference( MhCHResult, custThres);
		performance.add( specifDif);
		
		TotalBugPredictionMhJK MhJKPrediction = new TotalBugPredictionMhJK();
		TreeMap<Integer, Integer[]> MhJKResult = MhJKPrediction.predictTotalBug(project, captureSize, "");
		specifDif = evaluation.obtainSpecificDifference( MhJKResult, custThres);
		performance.add( specifDif);
		
		this.outputPerformance(M0Result, MthResult, MhCHResult, MhJKResult, MtCHResult, performanceFolder + "/" + project.getProjectName() );
		this.outputPerformanceSummarize(performance, performanceFolder, project.getProjectName() );	
	}
	
	
	public void predictTotalBugCombined ( TestProject project, int captureSize , String performanceFolder ) {
		PerformanceEvaluation evaluation = new PerformanceEvaluation();
		ArrayList<Double> performance = new ArrayList<Double>();
		
		TotalBugPredictionM0 M0Prediction = new TotalBugPredictionM0();
		TreeMap<Integer, Integer[]> M0Result = M0Prediction.predictTotalBug(project, captureSize, "");
		Double aveDif= evaluation.obtainAverageDifference( M0Result );
		performance.add( aveDif );
		
		TotalBugPredictionMth MthPrediction = new TotalBugPredictionMth();
		TreeMap<Integer, Integer[]> MthResult = MthPrediction.predictTotalBug(project, captureSize, "");
		aveDif = evaluation.obtainAverageDifference( MthResult );
		performance.add( aveDif );
		
		TotalBugPredictionMtCH MtCHPrediction = new TotalBugPredictionMtCH();
		TreeMap<Integer, Integer[]> MtCHResult = MtCHPrediction.predictTotalBug(project, captureSize, "");
		aveDif = evaluation.obtainAverageDifference( MtCHResult );
		performance.add( aveDif );
		
		TotalBugPredictionMhCH MhCHPrediction = new TotalBugPredictionMhCH();
		TreeMap<Integer, Integer[]> MhCHResult = MhCHPrediction.predictTotalBug(project, captureSize, "");
		aveDif = evaluation.obtainAverageDifference( MhCHResult );
		performance.add( aveDif );
		
		TotalBugPredictionMhJK MhJKPrediction = new TotalBugPredictionMhJK();
		TreeMap<Integer, Integer[]> MhJKResult = MhJKPrediction.predictTotalBug(project, captureSize, "");
		aveDif = evaluation.obtainAverageDifference( MhJKResult );
		performance.add( aveDif );
		
		this.outputPerformance(M0Result, MthResult, MhCHResult, MtCHResult, MhJKResult, performanceFolder + "/" + project.getProjectName() );
		this.outputPerformanceSummarize(performance, performanceFolder, project.getProjectName() );		
	}
	
	public void outputPerformance ( TreeMap<Integer, Integer[]> M0Result, TreeMap<Integer, Integer[]> MthResult, 
			TreeMap<Integer, Integer[]> MhCHResult, TreeMap<Integer, Integer[]> MhJKResult, TreeMap<Integer, Integer[]> MtCHResult, 
			String fileName ) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter( new FileWriter ( new File ( fileName  )));
			writer.write( "capture" + "," + "detectedBugNum" + "," + "totalBugNum" + "," + "M0" + "," + "Mth" + "," + "MtCH"  + "," + 
					"MhCH" +  "," + "MhJK");
			writer.newLine();
			
			for ( Integer index : M0Result.keySet() ) {
				writer.write( index + "," );
				for ( int j =1; j < M0Result.get(index).length; j++ ) {    //from j =1
					writer.write( M0Result.get( index )[j] + "," );
				}
				writer.write( M0Result.get(index)[0] + ",");
				writer.write( MthResult.get(index)[0] + ",");
				writer.write( MhCHResult.get(index)[0] + ",");
				writer.write( MhJKResult.get(index)[0] + ",");
				writer.write( MtCHResult.get(index)[0] + ",");
								
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void outputPerformanceSummarize ( ArrayList<Double> performance, String performanceFolder, String projectName ) {
		int index = performanceFolder.lastIndexOf( "/");
		String prefix = performanceFolder.substring( index+1 );
		System.out.println( index + " " + prefix );
		BufferedWriter writer;
		try {
			writer = new BufferedWriter( new FileWriter ( new File ( performanceFolder + "/" + prefix + "-summarize.csv" ), true ));
			writer.write( projectName + "," );
			for ( int i =0 ; i < performance.size(); i++ ) {
				writer.write( performance.get( i ) + ",");
			}
			writer.newLine();
			writer.flush();
			writer.close();
			
			//for pyplot
			String[] attrName =  { "M0", "Mth",  "MtCH", "MhCH", "MhJK"};
			writer = new BufferedWriter( new FileWriter ( new File ( performanceFolder + "/" + prefix + "-summarizePlot.csv" ), true ));
			for ( int i =0 ; i < performance.size(); i++ ) {
				writer.write( attrName[i] + "," + performance.get( i ));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void predictTotalBugCombinedForProjects ( String folderName, int captureSize, double custThres, String performanceFolder ) {
		TestProjectReader reader = new TestProjectReader();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				System.out.println ( "================================== " + fileName );
				
				TestProject project = reader.loadTestProject( fileName );
				//this.predictTotalBugCombined(project, captureSize, performanceFolder);
				this.predictPercentageBugCombined(project, captureSize, custThres, performanceFolder);
			}
		}
	}
	
	
	public static void main ( String[] args ) {
		CombinedTotalBugPrediction prediction = new CombinedTotalBugPrediction();
		
		Integer[] capSize = {7};    //{5, 6, 7, 8, 9, 10 };
		Double[] custThres = {0.95};    //{ 0.80, 0.85, 0.90, 0.95, 1.00 };
		String folderName = "data/input/test";
		String performancePrefix = "data/output/performance/";
		
		for ( int i =0;  i < capSize.length; i ++ ) {
			for ( int j =0; j < custThres.length; j++ ) {
				String tag = "default-" + capSize[i].toString() + "-" + custThres[j].toString();
				String performanceFolder = performancePrefix +  tag;
				
				File folder = new File ( performanceFolder );
				if ( !folder.exists() ) {
					folder.mkdir();
				}
				
				prediction.predictTotalBugCombinedForProjects(folderName, capSize[i], custThres[j], performanceFolder);
			}
		}
	}
}
