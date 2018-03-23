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

public class CombinedPredictionMeasuredBugNum {
	Double[] markPoints = {0.8, 0.85, 0.9, 0.95, 1.0};
	/*
	 * 对应这几个衡量指标的 whenPredictTrueBugNumber，predictPerformanceForSpecificBugPercentage，predictPerformanceForBugPercentageVariance
	 */
	public void predictBugNumberPerformance ( TestProject project, int captureSize, String performanceFolder ) {
		PerformanceEvaluation evaluation = new PerformanceEvaluation();
		
		ArrayList<Double> whenPerformance = new ArrayList<Double>();	
		ArrayList<TreeMap<Integer, Integer[]>> resultList  = this.obtainResultList(project, captureSize, performanceFolder);
		for ( int i =0; i < resultList.size(); i++ ) {
			Integer reportNum = evaluation.whenPredictTrueBugNumberRemain( resultList.get( i ), project.getTestReportsInProj().size() );
			whenPerformance.add( reportNum*1.0 / project.getTestReportsInProj().size() );
		}
		this.outputPerformanceSummarize( whenPerformance, performanceFolder + "/when.csv", project.getProjectName() );
		
		ArrayList<Integer[]> bugPercentPerformance = new ArrayList<Integer[]>();
		ArrayList<Integer[]> bugPercentPerformanceAbs = new ArrayList<Integer[]>();
		for ( int i =0; i < resultList.size(); i++ ) {
			Integer[] reportNumBugPerc = evaluation.predictPerformanceForSpecificBugPercentage( resultList.get(i), project, markPoints, false);
			bugPercentPerformance.add( reportNumBugPerc );
			
			Integer[] reportNumBugPercAbs = evaluation.predictPerformanceForSpecificBugPercentage( resultList.get(i), project, markPoints, true );
			bugPercentPerformanceAbs.add( reportNumBugPercAbs );
		}
		this.outputPerformanceSummarizeArray( bugPercentPerformance, performanceFolder + "/reportNum.csv", project.getProjectName() );
		this.outputPerformanceSummarizeArray( bugPercentPerformanceAbs, performanceFolder + "/reportNumAbs.csv", project.getProjectName() );
		
		ArrayList<Double> variancePerformance = new ArrayList<Double>();
		ArrayList<Double> variancePerformanceAbs = new ArrayList<Double>();
		for ( int i =0; i < resultList.size(); i++ ) {
			Double variance = evaluation.predictPerformanceForBugPercentageVariance( resultList.get(i), project, markPoints, false );
			variancePerformance.add( variance );
			
			Double varianceAbs = evaluation.predictPerformanceForBugPercentageVariance( resultList.get(i), project, markPoints, true );
			variancePerformanceAbs.add( varianceAbs );
		}
		this.outputPerformanceSummarize( variancePerformance, performanceFolder + "/variance.csv", project.getProjectName() );
		this.outputPerformanceSummarize( variancePerformance, performanceFolder + "/varianceAbs.csv", project.getProjectName() );
	}
	
	public ArrayList<TreeMap<Integer, Integer[]>> obtainResultList ( TestProject project, int captureSize, String performanceFolder ) {
		ArrayList<TreeMap<Integer, Integer[]>> resultList = new ArrayList<TreeMap<Integer, Integer[]>>();
		
		TotalBugPredictionM0 M0Prediction = new TotalBugPredictionM0();
		TreeMap<Integer, Integer[]> M0Result = M0Prediction.predictTotalBug(project, captureSize, "");
		resultList.add( M0Result );
		
		TotalBugPredictionMth MthPrediction = new TotalBugPredictionMth();
		TreeMap<Integer, Integer[]> MthResult = MthPrediction.predictTotalBug(project, captureSize, "");
		resultList.add( MthResult );
		
		TotalBugPredictionMtCH MtCHPrediction = new TotalBugPredictionMtCH();
		TreeMap<Integer, Integer[]> MtCHResult = MtCHPrediction.predictTotalBug(project, captureSize, "");
		resultList.add( MtCHResult );
		
		TotalBugPredictionMhCH MhCHPrediction = new TotalBugPredictionMhCH();
		TreeMap<Integer, Integer[]> MhCHResult = MhCHPrediction.predictTotalBug(project, captureSize, "");
		resultList.add( MhCHResult );
		
		TotalBugPredictionMhJK MhJKPrediction = new TotalBugPredictionMhJK();
		TreeMap<Integer, Integer[]> MhJKResult = MhJKPrediction.predictTotalBug(project, captureSize, "");
		resultList.add( MhJKResult );
		
		this.outputPerformance(resultList, performanceFolder + "/detail/" + project.getProjectName() );
		return resultList;
	}
	
	public void outputPerformance ( ArrayList<TreeMap<Integer, Integer[]>> resultList, String fileName ) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter( new FileWriter ( new File ( fileName  )));
			writer.write( "capture" + "," + "detectedBugNum" + "," + "totalBugNum" + "," + "M0" + "," + "Mth" + "," + "MtCH"  + "," + 
					"MhCH" +  "," + "MhJK");
			writer.newLine();
			
			for ( Integer index : resultList.get(0).keySet() ) {
				writer.write( index + "," );
				
				for ( int j =1; j < resultList.get(0).get( index).length; j++ ) {    //from j =1
					writer.write( resultList.get(0).get( index )[j] + "," );
				}
				for ( int i = 0; i < resultList.size(); i++ ) {
					writer.write( resultList.get( i ).get(index)[0] + ",");
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
	
	public void outputPerformanceSummarize ( ArrayList<Double> performance, String fileName, String projectName ) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter( new FileWriter ( new File ( fileName ), true ));
			writer.write( projectName + "," );
			for ( int i =0 ; i < performance.size(); i++ ) {
				writer.write( performance.get( i ) + ",");
			}
			writer.newLine();
			writer.flush();
			writer.close();
			
			//for pyplot
			int index = fileName.lastIndexOf( ".");
			String plotFileName = fileName.substring( 0, index ) + "-plot.csv";
			String[] attrName =  { "M0", "Mth",  "MtCH", "MhCH", "MhJK"};
			writer = new BufferedWriter( new FileWriter ( new File ( plotFileName ), true ));
			//writer.write( "method" + "," + "performance");
			//writer.newLine();
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
	
	public void outputPerformanceSummarizeArray ( ArrayList<Integer[]> performance, String fileName, String projectName ) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter( new FileWriter ( new File ( fileName), true ));
			writer.write( projectName + "," );
			for ( int i =0 ; i < performance.size(); i++ ) {
				for ( int j =0; j < performance.get(i).length; j++ ) {
					writer.write( performance.get( i )[j] + ",");
				}		
				writer.write( ",");
			}
			writer.newLine();
			writer.flush();
			writer.close();
			
			//for pyplot
			int index = fileName.lastIndexOf( ".");
			String plotFileName = fileName.substring( 0, index ) + "-plot.csv";
			String[] attrName =  { "M0", "Mth",  "MtCH", "MhCH", "MhJK"};
			writer = new BufferedWriter( new FileWriter ( new File ( plotFileName ), true ));
			//writer.write( "method" + "," + "para" + "," + "performance" );
			//writer.newLine();
			for ( int i =0 ; i < performance.size(); i++ ) {
				for ( int j = 0; j < performance.get(i).length; j++ ) {
					writer.write( attrName[i] + "," + markPoints[j] + "," + performance.get( i )[j] );
					writer.newLine();
				}				
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void predictBugNumberCombinedForProjects ( String folderName, int captureSize, String performanceFolder ) {
		TestProjectReader reader = new TestProjectReader();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				System.out.println ( "================================== " + fileName );
				
				TestProject project = reader.loadTestProject( fileName );
				this.predictBugNumberPerformance(project, captureSize, performanceFolder);
			}
		}
	}
	
	public static void main ( String[] args ) {
		CombinedPredictionMeasuredBugNum prediction = new CombinedPredictionMeasuredBugNum();
	
		String folderName = "data/input/projects";
		String performanceFolder = "data/output/performance";
		
		prediction.predictBugNumberCombinedForProjects(folderName, 7, performanceFolder );
	}
}
