package com.fitWeibull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

public class FitWeibullDistribution {
	public Double[] fitWeibullDistribution (TestProject project, int stepSize ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		
		TreeMap<Integer, String> noDupReportList = new TreeMap<Integer, String>();
		HashSet<String> noDupReport = new HashSet<String>();
		for ( int i =0; i < reportList.size(); i++ ) {
			TestReport report = reportList.get(i);
			String tag = report.getBugTag();
			String dupTag = report.getDupTag();
			
			if ( tag.equals( "审核通过") && !noDupReport.contains( dupTag )) {
				noDupReportList.put( i, "bug");
				noDupReport.add( dupTag );
			}else {
				noDupReportList.put( i, "no");
			}
		}
		
		
		try {
			String[] temp = project.getProjectName().split("-");
			String fileName = temp[0];
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/bugData/" + fileName + ".csv")));
			
			for ( int report : noDupReportList.keySet() ) {
				if ( noDupReportList.get( report).equals( "bug"))
					writer.write( report + "," + "1");
				else 
					writer.write( report + "," + "0");
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		TreeMap<Integer, Integer> stepReportsList = new TreeMap<Integer, Integer>();
		for ( int i =0; i < reportList.size(); i++ ) {
			String finalTag = noDupReportList.get( i );
			
			int interval = i / stepSize + 1;
			int count = 0;
			if ( finalTag.equals( "bug")) {
				count = 1;			
			}
			if ( stepReportsList.containsKey( interval )) {
				count += stepReportsList.get( interval );
			}	
			stepReportsList.put( interval, count );
		}
		System.out.println( stepReportsList.toString() );
		int confTotalBug = 0;
		for ( Integer step : stepReportsList.keySet() ) {
			confTotalBug += stepReportsList.get( step );
		}
		System.out.println ( "Confidentity check: " +  confTotalBug + " " + noDupReport.size() );
		/*
		 * for test
		stepReportsList.clear();
		stepReportsList.put( 1, 5);
		stepReportsList.put( 2, 10);
		stepReportsList.put( 3, 27);
		stepReportsList.put( 4, 82);
		stepReportsList.put( 5, 94);
		stepReportsList.put( 6, 61);
		stepReportsList.put( 7, 77);
		stepReportsList.put( 8, 111);
		int totalBugNum = 1200;
		*/		
		
		//cumulative proportion F(t)
		int totalBugNum = noDupReport.size();
		int priorCum = 0;
		TreeMap<Integer, Double> cumProbList = new TreeMap<Integer, Double>();
		boolean isBeginZero = true;
		int trueStep = 1;
		for ( Integer step : stepReportsList.keySet() ) {
			int cum = stepReportsList.get( step ) + priorCum ;
			if ( isBeginZero == true ) {
				if ( cum == 0.0 ) 
					continue;
				else
					isBeginZero = false;
			}
		
			double cumProb = 1.0*cum / totalBugNum;
			priorCum = cum;
			
			if ( cumProb == 1.0 )
				continue;
			cumProbList.put( trueStep, cumProb );
			trueStep++;
			/*
			 * {1=0, 2=0, 3=0, 4=0, 5=11, 6=4, 7=6, 8=1, 9=2, 10=0, 11=0, 12=0}
			 * {1=0.4583333333333333, 2=0.625, 3=0.875, 4=0.9166666666666666}
			 * M为1.06
			 * 如果没有trueStep，则会转化为{5=0.458}, 得到的M为4，
			 */
		}
		System.out.println( cumProbList.toString() );
		
		//ln(Step), ln(ln(1/(1-F(t))))
		TreeMap<Double, Double> lnCumProbList = new TreeMap<Double, Double>();
		double[][] data = new double[cumProbList.size()][2];
		int index = 0;
		for ( Integer step : cumProbList.keySet() ) {
			double lnStep = Math.log( step );
			double lnCumProb = 1.0 / (1.0 - cumProbList.get( step ));
			lnCumProb = Math.log( lnCumProb );
			lnCumProb = Math.log( lnCumProb );
			
			lnCumProbList.put( lnStep, lnCumProb );
			data[index][0] = lnStep;
			data[index][1] = lnCumProb;
			index++;
		}
		System.out.println( lnCumProbList.toString() );
		
		if ( data.length == 1 ) {
			Double[] result = {1.0, 0.0};
			return result;
		}
		if ( data.length == 2 ) {
			double slope = ( data[1][1] - data[0][1]) / (data[1][0] - data[0][0]);
			Double[] result = {slope, 0.0 };
			System.out.println(  slope   );
			return result;
		}
		
		SimpleRegression regression = new SimpleRegression();
		regression.addData( data );
		regression.regress();
		
		System.out.println(  regression.getSlope() + " " +	regression.getIntercept() + " " + regression.getRSquare()  );
		Double[] result = { regression.getSlope(), regression.getRSquare() };
		return result;
	}
	
	public void fitWeibullDistributionForProjects ( String folderName , String performanceFile) {
		TestProjectReader reader = new TestProjectReader();
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( performanceFile )));
			writer.write( "project" + "," + "m" + "," + "R-square" );
			writer.newLine();
			
			File projectsFolder = new File ( folderName );
			if ( projectsFolder.isDirectory() ){
				String[] projectFileList = projectsFolder.list();
				for ( int i = 0; i< projectFileList.length; i++ ){
					String fileName = folderName + "/" + projectFileList[i];
					System.out.println ( "================================== " + fileName );
					
					TestProject project = reader.loadTestProject( fileName );
					Double[] performance = this.fitWeibullDistribution(project, 15);
					
					writer.write( project.getProjectName() + ",");
					for ( int j =0;  j< performance.length; j++ ) {
						writer.write( performance[j] + ",");
					}
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
	
	public static void main ( String args[] ) {
		FitWeibullDistribution fitTool = new FitWeibullDistribution();
		
		String folderName = "data/input/projects";
		String performanceFile = "data/output/performance/fitWeibullDistribution.csv";
		fitTool.fitWeibullDistributionForProjects(folderName, performanceFile);
	}
}
