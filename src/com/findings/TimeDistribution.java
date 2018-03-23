package com.findings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

public class TimeDistribution {
	Integer timeIntervalThres = 1;   //hours
	
	//统计当前报告和前一个报告的间隔时间
	public void countTimeDistribution ( TestProject project , String fileName) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		
		TreeMap<Integer, Integer> timeDistMap = new TreeMap<Integer, Integer>();
		TreeMap<Integer, HashSet<String>> noDupTimeDistMap = new TreeMap<Integer, HashSet<String>>();
		
		Date beginTime = reportList.get(0).getSubmitTime();
	
		for ( int i =0; i < reportList.size(); i++ ) {    //from the second report
			TestReport report = reportList.get( i );
			
			Date submitTime = report.getSubmitTime();
			int hours = (int) (( submitTime.getTime() - beginTime.getTime()) / (1000*60*60)); 
			int interval = hours / timeIntervalThres;
			
			String tag = report.getBugTag();
			if ( !tag.equals( "审核通过")) {
				continue;
			}

			int count = 1;
			if ( timeDistMap.containsKey( interval )) {
				count += timeDistMap.get( interval );
			}
			timeDistMap.put( interval, count );
			
			String noDupTag = report.getDupTag();
			HashSet<String> priorList = new HashSet<String>();
			for ( int j = 0; j < interval; j++ ) {
				if ( noDupTimeDistMap.containsKey( j )) {
					priorList.addAll( noDupTimeDistMap.get( j ) ) ;
				}
			}
			
			HashSet<String> curList = new HashSet<String>();
			if ( noDupTimeDistMap.containsKey( interval )) {
				curList = noDupTimeDistMap.get( interval );
			}
			if ( !priorList.contains( noDupTag )) {
				curList.add( noDupTag );
			}
			noDupTimeDistMap.put( interval, curList );
		}
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( fileName )));
			for ( Integer interval : timeDistMap.keySet() ) {
				writer.write( interval + "," + timeDistMap.get( interval ) + "," + noDupTimeDistMap.get( interval ).size() );
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void obtainTimeDistributionForProjects ( String folderName ) {
		TestProjectReader reader = new TestProjectReader();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				
				TestProject project = reader.loadTestProject( fileName );
				
				String outFile = "data/output/timeDist/timeDist-" + projectFileList[i];
				this.countTimeDistribution(project, outFile );
			} 
		}
	}
	
	public static void main ( String args[] ) {
		TimeDistribution dist = new TimeDistribution();
		
		String folderName = "data/input/projects";
		dist.obtainTimeDistributionForProjects(folderName);
	}
	
}
