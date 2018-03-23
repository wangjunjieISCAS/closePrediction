package com.processPrediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;
import com.prediction.CRCAlgorithm;
import com.reportsOrder.UniformDistributionOrderReports;

public class TotalBugPrediction {
	int shuffleNumber = 100;
	
	CRCAlgorithm algorithm;
	public TotalBugPrediction ( ) {
		algorithm = new CRCAlgorithm();
	}
	
	public TreeMap<Integer, Integer[]> predictTotalBug ( TestProject project, int captureSize, String fileName ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		HashSet<String> noDupTagTotal = new HashSet<String>();
		for ( int i =0; i < reportList.size() ; i++ ) {
			TestReport report = reportList.get( i );
			if ( report.getBugTag().equals( "审核通过") ) {
				noDupTagTotal.add( report.getDupTag() );
			}
		}
		int totalBugNum = noDupTagTotal.size();
		
		TreeMap<Integer, ArrayList<TestReport>> captureHistory = new TreeMap<Integer, ArrayList<TestReport>>();
		TreeMap<Integer, Integer[]> estBugList = new TreeMap<Integer, Integer[]>();
		HashSet<String> noDupTagList = new HashSet<String>();
			
		int index = -1;
		int firstBugIndex = -1;
		while ( true ) {
			index++;
			int beginReport = index * captureSize;
			int endReport = beginReport + captureSize;
			
			if ( endReport >= reportList.size() )
				break;
			
			ArrayList<TestReport> capReports = new ArrayList<TestReport>();
			for ( int i = beginReport; i < endReport && i < reportList.size(); i++ ) {
				TestReport report = reportList.get( i );
				
				if ( report.getBugTag().equals( "审核通过") && firstBugIndex == -1 ) {
					capReports.add( report );
					noDupTagList.add( report.getDupTag() );
				}
			}
			captureHistory.put( index, capReports );
			
			if ( index <= 1) {
				continue;
			}
			
			Integer estPopSize = this.estimateCurrentTotalBugDefault (captureHistory );
			//Integer estPopSize = this.estimateCurrentTotalBug(captureHistory, "");
			Integer[] result = {estPopSize, noDupTagList.size(), totalBugNum };
			estBugList.put( endReport, result );
			System.out.println( "The " +  index + " capture! Estimate bug number is: " + estPopSize + ". Total bug number is: " + totalBugNum + ". Already detected bug number is: " + noDupTagList.size() );
		}
		return estBugList;
	}
	
	public Integer estimateCurrentTotalBugDefault ( TreeMap<Integer, ArrayList<TestReport>> captureHistory  ) {
		Integer[] result = algorithm.obtainRecaptureResults( captureHistory );
		return result[2]; 
	}
	
	public Integer estimateCurrentTotalBug ( TreeMap<Integer, ArrayList<TestReport>> captureHistory , String fileName ) {
		//captureHistory = this.reviseCaptureHistory(captureHistory);
		//this.outputReorderedCapture( captureHistory, fileName);
		
		UniformDistributionOrderReports order = new UniformDistributionOrderReports();
		//RuleOrderReports order = new RuleOrderReports();
		
		ArrayList<Integer> randResultList = new ArrayList<Integer>();
		for ( int i =0; i < this.shuffleNumber; i++ ) {
			//captureHistory = order.orderReportsRandomly(captureHistory);
			captureHistory = order.orderReportsUniformly(captureHistory);
			//captureHistory = order.orderReportsBasedRule(captureHistory);
			
			//this.outputReorderedCapture(newCaptureHistory, fileName);
			
			Integer[] result = algorithm.obtainRecaptureResults( captureHistory );
			randResultList.add( result[2] );     //all algorithms (M0, Mth, MhCH, MtCH, MhJK) should put their final results on result[2]
		}		
		
		Collections.sort( randResultList );
		System.out.print ( "**************************************** ");
		for ( int i = 0; i < randResultList.size(); i++ ) {
			System.out.print( randResultList.get( i ) + " " );
		}
		System.out.println( );
		
		//quartile(3)
		/*
		int quarThree = 0;
		int quarIndex = (int) (1.0*randResultList.size() / 4.0 * 3.0 );
		if ( quarIndex >= randResultList.size() )
			quarIndex = randResultList.size() - 1;
		quarThree = randResultList.get( quarIndex );
		return quarThree;
		*/
		
		int medianResult = 0;
		int midIndex = randResultList.size() / 2;
		if ( randResultList.size() % 2 == 1 ) {
			medianResult = randResultList.get( midIndex );
		}
		else {
			medianResult = ( randResultList.get( midIndex -1) + randResultList.get(midIndex)) / 2;
		}
		return medianResult;
		
		/*
		int count = 0;
		for ( int i =0; i < randResultList.size(); i++ )
			count += randResultList.get( i );
		int averageResult = count / randResultList.size();
		
		return averageResult;
		*/
	}
	
	/*
	 * 如果前面有capture中，bug number为0，则去掉WW
	 */
	public TreeMap<Integer, ArrayList<TestReport>> reviseCaptureHistory ( TreeMap<Integer, ArrayList<TestReport>> captureHistory ) {
		TreeMap<Integer, ArrayList<TestReport>> newCaptureHistory = new TreeMap<Integer, ArrayList<TestReport>>();
		boolean isZero = true;
		for ( Integer cap : captureHistory.keySet() ) {
			ArrayList<TestReport> reportsList = captureHistory.get( cap );
			if ( isZero == true && reportsList.size() == 0 )    
				continue;
			isZero = false;
			newCaptureHistory.put( cap, reportsList );
		}
		return newCaptureHistory;
	}
	
	public void outputReorderedCapture ( TreeMap<Integer, ArrayList<TestReport>> newCaptureHistory, String fileName ) {
		HashSet<String> noDupTag = new HashSet<String>();
		int count = 0;
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( fileName)));
			for ( Integer cap : newCaptureHistory.keySet() ) {
				ArrayList<TestReport> reports = newCaptureHistory.get( cap );
				for ( int i =0; i < reports.size(); i++ ) {
					String tag = reports.get(i).getDupTag();
					if ( !noDupTag.contains( tag )) {
						count++;
						noDupTag.add( tag );
					}
					writer.write( count + ",");
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
	
	public void predictTotalBugForProjects ( String folderName, int captureSize, String performanceFolder ) {
		TestProjectReader reader = new TestProjectReader();
		
		try {
			File projectsFolder = new File ( folderName );
			if ( projectsFolder.isDirectory() ){
				String[] projectFileList = projectsFolder.list();
				for ( int i = 0; i< projectFileList.length; i++ ){
					String fileName = folderName + "/" + projectFileList[i];
					System.out.println ( "================================== " + fileName );
					
					TestProject project = reader.loadTestProject( fileName );
					TreeMap<Integer, Integer[]> estBugList = this.predictTotalBug( project, captureSize, performanceFolder + "/reorder-" + projectFileList[i] );
					
					BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( performanceFolder + "/" + projectFileList[i] )));
					for ( Integer index : estBugList.keySet() ) {
						writer.write( index + "," );
						for ( int j =0; j < estBugList.get( index).length; j++ )
							writer.write( estBugList.get( index )[j] + "," );
						writer.newLine();
					}
					writer.flush();
					writer.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
