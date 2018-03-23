package com.findings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

public class BugNumberCurve {
	/*
	 * 默认所有的项目都是排序好的，不需要进行排序了
	 */
	public TreeMap<Integer, Integer> obtainBugNumberCurve ( TestProject project , String fileName  ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		
		TreeMap<Integer, Integer> bugNumForReports = new TreeMap<Integer, Integer>();
		HashSet<String> noDupBugs = new HashSet<String>();
		
		int bugNum = 0;
		for ( int i =0; i < reportList.size(); i++ ) {
			TestReport report = reportList.get( i );
			//System.out.println ( report.getSubmitTime() );
			
			String bugTag = report.getBugTag();
			String dupTag = report.getDupTag();
			if ( bugTag.equals( "审核通过")) {
				if ( !noDupBugs.contains( dupTag )) {
					bugNum ++;
					noDupBugs.add( dupTag );
				}
			}
			bugNumForReports.put( i+1, bugNum );
		}
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( fileName )));
			writer.write( "reportNum" + "," + "bugNum");
			writer.newLine();
			for ( Integer reportNum : bugNumForReports.keySet() ) {
				writer.write( reportNum + "," + bugNumForReports.get( reportNum ));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bugNumForReports;
	}
	
	public Object[] obtainBugCurveStatistics ( TreeMap<Integer, Integer> bugNumForReports ) {
		int lastValidReportNum = 0, maxBugNum = 0;
		for ( Integer reportNum : bugNumForReports.keySet() ) {
			Integer bugNum = bugNumForReports.get( reportNum );
			if ( bugNum > maxBugNum ) {
				maxBugNum = bugNum;
				lastValidReportNum = reportNum;
			}
		}
		Object[] result = new Object[2];
		result[0] = lastValidReportNum;
		result[1] = maxBugNum;
		
		return result;
	}
	
	public void obtainBugNumberCurveAndStatisticsForProjects ( String folderName, String statisFileName ) {
		TestProjectReader reader = new TestProjectReader();
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( statisFileName )));
			writer.write( "project" + "," + "bugNum" + "," + "inspectionReportNum" + "," + "totalReportNum");
			writer.newLine();
			
			File projectsFolder = new File ( folderName );
			if ( projectsFolder.isDirectory() ){
				String[] projectFileList = projectsFolder.list();
				for ( int i = 0; i< projectFileList.length; i++ ){
					String fileName = folderName + "/" + projectFileList[i];
					
					TestProject project = reader.loadTestProject( fileName );
					
					String outFile = "data/output/trend/bugNumCurve-" + projectFileList[i];
					TreeMap<Integer, Integer> bugNumForReports = this.obtainBugNumberCurve(project, outFile );
					
					Object[] statistics = this.obtainBugCurveStatistics(bugNumForReports);
					writer.write( projectFileList[i] + "," +  statistics[1] + "," + statistics[0] + "," +  bugNumForReports.size());
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main ( String args[] ) {
		BugNumberCurve curveTool = new BugNumberCurve();
		
		String folderName = "data/input/projects";
		curveTool.obtainBugNumberCurveAndStatisticsForProjects( folderName, "data/input/bugCurveStatistics.csv");
	}
}
