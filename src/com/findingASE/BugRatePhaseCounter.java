package com.findingASE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

public class BugRatePhaseCounter {
	//phaseNum表示分为几段
	public ArrayList<Integer> countBugRatePhase (TestProject project , int phaseNum ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		int reportNumPerPhase = reportList.size() / phaseNum;
		int remainReports = reportList.size() - reportNumPerPhase * phaseNum;
		
		HashSet<String> noDupReports = new HashSet<String>();
		ArrayList<String> finalBugTag = new ArrayList<String>();
		for ( int i =0; i < reportList.size(); i++ ) {
			TestReport report = reportList.get( i );
			String bugTag = report.getBugTag();
			String dupTag = report.getDupTag();
			if ( bugTag.equals( "审核通过")) {
				if ( !noDupReports.contains( dupTag)) {
					finalBugTag.add( "bug");
					noDupReports.add( dupTag);
				}
				else {
					finalBugTag.add( "no");
				}
			}
			else {
				finalBugTag.add ("no");
			}
		}
		
		int phaseCount = 0;
		int beginIndex = 0;
		ArrayList<Integer> bugNumPerPhase = new ArrayList<Integer>();
		//int count = 0;
		while ( true ) {
			int reportNumThres = reportNumPerPhase;
			if ( phaseCount < remainReports )
				reportNumThres = reportNumPerPhase + 1;
			
			int count = 0;
			for ( int j =0; j < reportNumThres && j + beginIndex < reportList.size(); j++ ) {
				String bugTag = finalBugTag.get( j + beginIndex );
				if ( bugTag.equals( "bug")) {
					count++;
				}
			}
			bugNumPerPhase.add( count );
			
			beginIndex += reportNumThres;
			if ( beginIndex >= reportList.size() ) {
				break;
			}
		}
		
		return bugNumPerPhase;
	}
	
	public void countBugRatePhaseForProjects ( String folderName ) {
		TestProjectReader reader = new TestProjectReader();
		
		TreeMap<String, ArrayList<Integer>> resultList = new TreeMap<String, ArrayList<Integer>>();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				System.out.println ( "================================== " + fileName );
				
				TestProject project = reader.loadTestProject( fileName );
				ArrayList<Integer> result = this.countBugRatePhase(project, 10);
				resultList.put( project.getProjectName(), result );
			}
		}
		
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/bugRate.csv" )));
			writer.write( "project" + "," + "cost" + "," + "hours" );
			writer.newLine();
			
			for ( String projectName : resultList.keySet() ) {
				writer.write( projectName + ",");
				
				for ( int i =0; i < resultList.get( projectName).size(); i++ ) {
					writer.write( resultList.get( projectName).get( i ) + ",");
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
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/bugRate-plot.csv" )));
			writer.write( "thres" + "," + "value" );
			writer.newLine();
			
			String[] thres = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
			for ( String projectName : resultList.keySet() ) {
				for ( int i =0; i < resultList.get( projectName).size(); i++ ) {
					writer.write( thres[i] + "," + resultList.get( projectName).get( i ) );
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
		BugRatePhaseCounter counter = new BugRatePhaseCounter();
		String folderName = "data/input/projects";
		counter.countBugRatePhaseForProjects(folderName);
	}
}
