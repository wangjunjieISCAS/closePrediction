package com.findingASE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

public class TimeCostCounter {
	public Integer[] countTimeCostForSpecificBugRate ( TestProject project, double bugRateThres ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		
		HashSet<String> noDupReports = new HashSet<String>();
		for ( int i =0; i < reportList.size(); i++ ) {
			TestReport report = reportList.get( i );
			String bugTag = report.getBugTag();
			String dupTag = report.getDupTag();
			if ( bugTag.equals( "审核通过") ) {
				if ( !noDupReports.contains( dupTag )) {
					noDupReports.add( dupTag );
				}
			}
		}
		int totalBugs = noDupReports.size();
		
		noDupReports.clear();
		int i = 0;
		for ( ; i < reportList.size(); i++ ) {
			TestReport report = reportList.get( i );
			String bugTag = report.getBugTag();
			String dupTag = report.getDupTag();
			if ( bugTag.equals( "审核通过") ) {
				if ( !noDupReports.contains( dupTag )) {
					noDupReports.add( dupTag );
				}
			}
			double bugRate = 1.0* noDupReports.size() / totalBugs;
			if ( bugRate >= bugRateThres ) {
				break;
			}
		}
		
		Date beginTime = reportList.get(0).getSubmitTime();
		Date endThresTime = reportList.get( i ).getSubmitTime();
		int hours = (int) (( endThresTime.getTime() - beginTime.getTime()) / (1000*3600));    //半小时
		
		Integer[] result = {i, hours};
		return result;
	}
	
	public void countTimeCostForProjects ( String folderName ) {
		TestProjectReader reader = new TestProjectReader();
		
		Double[] bugRateThres = {0.6, 0.7, 0.8, 0.9, 1.0};
		TreeMap<String, Integer[][]> resultList = new TreeMap<String, Integer[][]>();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				System.out.println ( "================================== " + fileName );
				
				TestProject project = reader.loadTestProject( fileName );
				
				Integer[][] resultArray = new Integer[bugRateThres.length][];
				for ( int j =0; j < bugRateThres.length; j++ ) {
					Integer[] result = this.countTimeCostForSpecificBugRate(project, bugRateThres[j] );	
					resultArray[j] = result;
				}
				resultList.put( project.getProjectName(), resultArray );
			}
		}
		
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/timeCost.csv" )));
			writer.write( "project" + "," + "cost" + "," + "hours" );
			writer.newLine();
			
			for ( String projectName : resultList.keySet() ) {
				writer.write( projectName + ",");
				
				Integer[][] resultArray = resultList.get( projectName );
				for ( int i =0; i < resultArray.length; i++ ) {
					for ( int j =0; j < resultArray[i].length; j++ ) {
						writer.write( resultArray[i][j] + ",");
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
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/timeCost-plot.csv" )));
			writer.write( "type" + "," + "thres" + "," + "value" );
			writer.newLine();
			
			String[] types = {"cost", "duration"};
			String[] thres = { "0.6", "0.7", "0.8", "0.9", "1.0"};
			for ( String projectName : resultList.keySet() ) {
				Integer[][] resultArray = resultList.get( projectName );
				for ( int i =0; i < resultArray.length; i++ ) {
					for ( int j =0; j < resultArray[i].length; j++ ) {
						writer.write( types[j] + "," + thres[i] + "," + resultArray[i][j]);
						writer.newLine();
					}
				}
				//writer.newLine();
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main ( String args[] ) {
		TimeCostCounter counter = new TimeCostCounter();
		String folderName = "data/input/projects";
		counter.countTimeCostForProjects(folderName);
	}
}
