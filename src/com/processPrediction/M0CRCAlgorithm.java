package com.processPrediction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestReport;
import com.prediction.CRCAlgorithm;

//这些方法都是不考虑一次capture之间的重复，也就是说对于每次capture，只是看bug的数目，不统计重复的数目
public class M0CRCAlgorithm extends CRCAlgorithm{
	int cutPointThres = 3;

	@Override
	public Integer[] obtainRecaptureResults(TreeMap<Integer, ArrayList<TestReport>> captureProcess) {
		// TODO Auto-generated method stub
		int cutPoint = captureProcess.size() / 2;    //the first part is equal with beginCut
		
		ArrayList<TestReport> firstRoundReports = new ArrayList<TestReport>();
		ArrayList<TestReport> secondRoundReports = new ArrayList<TestReport>();
		
		int firstRoundCaptureSize = 0;
		for ( Integer index : captureProcess.keySet() ) {
			ArrayList<TestReport> reports = captureProcess.get( index );
			
			if ( firstRoundCaptureSize <= cutPoint ) {
				firstRoundCaptureSize ++;
				firstRoundReports.addAll( reports );
			}else {
				secondRoundReports.addAll( reports );
			}
		}
		//System.out.println( "***************************** " + firstRoundCaptureSize );			
		
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
		//System.out.println( firstCaptureSize + " " + secondCaptureSize + "  " + reCapSize + " " + firstRoundReports.size() + " " + secondRoundReports.size() + " " + estimatePopSize);
		
		Integer[] results = { estimatePopSize, estimatePopSize, estimatePopSize};
		
		return results;		
	}
	
	
	/*
	 * estimate based on several cut points
	public Integer[] obtainRecaptureResults(TreeMap<Integer, ArrayList<TestReport>> captureProcess) {
		// TODO Auto-generated method stub	
		Integer[] results = new Integer[cutPointThres];
				
		int beginCut = captureProcess.size() / 2 -1;    //the first part is equal with beginCut
		int sideNum = cutPointThres / 2;
		Integer[] cutPointList = new Integer[cutPointThres];
		cutPointList[0] = beginCut;
		int pointNum = 0;
		for ( int i =1; i <= sideNum; i++ ) {
			int cutPoint = beginCut + i;
			if ( cutPoint >= captureProcess.size() )
				cutPoint = captureProcess.size()-1;
			cutPointList[++pointNum] = cutPoint;
			
			cutPoint = beginCut - i ;
			if ( cutPoint < 0 )
				cutPoint = 0;
			cutPointList[++pointNum] = cutPoint;
		}
		
		System.out.print ( "========================= " );
		for ( int i =0; i < cutPointList.length; i++ ) {
			System.out.print (  cutPointList[i] + " " );
		}
		System.out.println ( ); 
		
		for ( int i = 0; i < cutPointList.length; i++ ) {
			int cut = cutPointList[i];
			ArrayList<TestReport> firstRoundReports = new ArrayList<TestReport>();
			ArrayList<TestReport> secondRoundReports = new ArrayList<TestReport>();
			
			int firstRoundCaptureSize = 0;
			for ( Integer index : captureProcess.keySet() ) {
				ArrayList<TestReport> reports = captureProcess.get( index );
				
				if ( firstRoundCaptureSize <= cut ) {
					firstRoundCaptureSize ++;
					firstRoundReports.addAll( reports );
				}else {
					secondRoundReports.addAll( reports );
				}
			}
			//System.out.println( "***************************** " + firstRoundCaptureSize );			
			
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
			//int firstCaptureSize = firstRoundReports.size();    
			
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
			//int secondCaptureSize = secondRoundReports.size();
			
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
			System.out.println( firstCaptureSize + " " + secondCaptureSize + "  " + reCapSize + " " + firstRoundReports.size() + " " + secondRoundReports.size() + " " + estimatePopSize);
			results[i] = estimatePopSize ;			
		}
		return results;		
	}
*/
	
}
