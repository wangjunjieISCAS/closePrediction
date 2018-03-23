package com.processPrediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

/*
 * 基于capture-recapture的基本思想，结合random sampling，进行remaining bugs的预测
 */
public class RemainBugPredictionCRCRand {
	public void RemainBugPrediction ( TestProject project, int captureSize ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		HashSet<String> noDupTagTotal = new HashSet<String>();
		for ( int i =0; i < reportList.size() ; i++ ) {
			TestReport report = reportList.get( i );
			if ( report.getBugTag().equals( "审核通过") ) {
				noDupTagTotal.add( report.getDupTag() );
			}
		}
		int totalBugNum = noDupTagTotal.size();
		
		ArrayList<TestReport[]> captureHistory = new ArrayList<TestReport[]>();	
		HashSet<String> noDupTagList = new HashSet<String>();
			
		int index = -1;
		while ( true ) {
			index++;
			int beginReport = index * captureSize;
			int endReport = beginReport + captureSize;
			
			if ( endReport >= reportList.size() )
				break;
			
			TestReport[] capReports = new TestReport[captureSize];
			for ( int i = beginReport; i < endReport && i < reportList.size(); i++ ) {
				capReports[i-beginReport] = reportList.get( i );
				
				TestReport report = reportList.get( i );
				if ( report.getBugTag().equals( "审核通过")) {
					noDupTagList.add( report.getDupTag() );
				}
			}
			captureHistory.add( capReports );
			
			if ( index != 0 ) {
				ArrayList<Integer> estimatePopSizeList = this.estimateRemainBugCut(captureHistory);
				System.out.println( "The " +  index + " capture! Total bug number is: " + totalBugNum + " Already detected bug number is: " + noDupTagList.size() );
				for ( int i =0; i < estimatePopSizeList.size(); i++ ) {
					System.out.print ( estimatePopSizeList.get( i ) + " " );
				}
				System.out.println ();
			}
		}
	}
	
	/*
	 * 基于每个分割点，平均分成两份作为first round和second round
	 * 不改变report提交的顺序
	 */
	public ArrayList<Integer> estimateRemainBugCut ( ArrayList<TestReport[]> captureHistory ) {	
		ArrayList<Integer> estimatePopSizeList = new ArrayList<Integer>();
		for ( int i =1; i < captureHistory.size(); i++ ) {   //cut points
			ArrayList<TestReport> firstRoundReports = new ArrayList<TestReport>();
			ArrayList<TestReport> secondRoundReports = new ArrayList<TestReport>();
			
			for ( int j =0; j < i; j++ ) {   //first round
				TestReport[] reports = captureHistory.get( j );
				for ( int k =0; k < reports.length; k++  ) {
					firstRoundReports.add( reports[k] );
				}				
			}
			for ( int j = i; j < captureHistory.size(); j++ ) {    //second round
				TestReport[] reports = captureHistory.get( j );
				for ( int k =0; k < reports.length; k++ ) {
					secondRoundReports.add( reports[k] );
				}
			}
			
			//统计n1，n2，m
			HashSet<String> noDupTagFirst = new HashSet<String>();
			for ( int j =0; j < firstRoundReports.size(); j++) {
				TestReport report = firstRoundReports.get( j );
				String tag = report.getBugTag();
				String dupTag = report.getDupTag();
				if ( tag.equals("审核通过") ) {
					noDupTagFirst.add( dupTag );
				}				
			}
			int firstCaptureSize = noDupTagFirst.size();
			
			HashSet<String> noDupTagSecond = new HashSet<String>();
			for ( int j =0; j < secondRoundReports.size(); j++ ) {
				TestReport report = secondRoundReports.get( j );
				String tag = report.getBugTag();
				String dupTag = report.getDupTag();
				if ( tag.equals( "审核通过")) {
					noDupTagSecond.add( dupTag );
				}
			}
			int secondCaptureSize = noDupTagSecond.size();
			
			int reCapSize = 0;
			for ( String tag : noDupTagSecond ) {
				if ( noDupTagFirst.contains( tag )) {
					reCapSize++;
				}
			}
			
			int estimatePopSize = firstCaptureSize * secondCaptureSize ;
			if ( reCapSize != 0 ) {
				estimatePopSize = estimatePopSize / reCapSize;
			}
			//System.out.println( firstCaptureSize + " " + secondCaptureSize + "  " + reCapSize );
			estimatePopSizeList.add( estimatePopSize );			
		}
		
		return estimatePopSizeList;
	}
	
	public void predictCloseTimeForProjects ( String folderName ) {
		TestProjectReader reader = new TestProjectReader();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				System.out.println ( "================================== " + fileName );
				
				TestProject project = reader.loadTestProject( fileName );
				this.RemainBugPrediction(project, 8);
			}
		}
	}
	
	public static void main ( String args[] ) {
		RemainBugPredictionCRCRand prediction = new RemainBugPredictionCRCRand();
		
		String projectFolder = "data/input/test";
		prediction.predictCloseTimeForProjects( projectFolder );
	}
}
